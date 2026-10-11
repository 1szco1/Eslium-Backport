package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class CrystalHandler {
    private static final Map<Entity, Integer> HIDDEN = new IdentityHashMap<>();
    private static final Map<Entity, Long> SCHEDULED = new IdentityHashMap<>();

    public static boolean tryHit(EndCrystalEntity crystal, DamageSource source, float amount) {
        if (crystal == null || source == null) return false;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return false;
        if (!(crystal.world instanceof ClientWorld)) return false;
        if (crystal.world != client.player.world) return false;
        if (crystal.removed || !crystal.isAlive()) return false;
        if (!(amount > 0.0F)) return false;

        if (JsonConfig.config == null || JsonConfig.config.crystals == null) return false;
        if (!Boolean.TRUE.equals(JsonConfig.config.crystals.enabled)) return false;
        if (!JsonConfig.shouldWork(JsonConfig.config.crystals.servers)) return false;

        if (client.isInSingleplayer()) return false;

        ClientPlayerEntity player = client.player;
        Entity attacker = source.getAttacker();
        if (attacker == null || attacker != player) return false;
        if (crystal.isInvulnerableTo(source)) return false;

        double attackDamage = player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (!(attackDamage > 0.0D)) return false;

        byte type = Entities.getType(crystal);
        if (type == Entities.FAKE || type == Entities.TO_DESTROY) {
            Entities.remove(crystal);
            HIDDEN.remove(crystal);
            SCHEDULED.remove(crystal);
            return true;
        }

        long delay;
        if (Boolean.TRUE.equals(JsonConfig.config.crystals.dynamicDelay)) {
            delay = LatencyEstimator.estimateMs(0, 200);
        } else {
            delay = JsonConfig.config.crystals.delay != null ? JsonConfig.config.crystals.delay : 0;
            if (delay > 200) delay = 200;
        }
        int resync = JsonConfig.config.crystals.resync != null ? JsonConfig.config.crystals.resync : 20;
        if (resync < 0) resync = 0;
        if (resync > 50) resync = 50;

        if (delay > 0) {
            SCHEDULED.put(crystal, System.nanoTime() + delay * 1_000_000L);
            return true;
        }

        removeOrHide(crystal, resync);
        return true;
    }

    private static void removeOrHide(Entity entity, int resync) {
        if (entity == null) return;
        if (resync > 0) {
            HIDDEN.put(entity, resync);
        } else {
            entity.remove();
        }
    }

    public static boolean isHidden(Entity entity) {
        return entity != null && HIDDEN.containsKey(entity);
    }

    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) {
            HIDDEN.clear();
            SCHEDULED.clear();
            return;
        }

        if (!HIDDEN.isEmpty()) {
            Iterator<Map.Entry<Entity, Integer>> it = HIDDEN.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Entity, Integer> entry = it.next();
                Entity entity = entry.getKey();
                if (entity.removed || !entity.isAlive() || entity.world != client.world) {
                    it.remove();
                    continue;
                }
                int remaining = entry.getValue() - 1;
                if (remaining <= 0) {
                    it.remove();
                } else {
                    entry.setValue(remaining);
                }
            }
        }

        if (!SCHEDULED.isEmpty()) {
            int resync = 20;
            if (JsonConfig.config != null && JsonConfig.config.crystals != null && JsonConfig.config.crystals.resync != null) {
                resync = JsonConfig.config.crystals.resync;
            }
            if (resync < 0) resync = 0;
            if (resync > 50) resync = 50;

            long now = System.nanoTime();
            Iterator<Map.Entry<Entity, Long>> it = SCHEDULED.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Entity, Long> entry = it.next();
                Entity entity = entry.getKey();
                if (entity.removed || !entity.isAlive() || entity.world != client.world) {
                    it.remove();
                    continue;
                }
                if (entry.getValue() - now > 0L) continue;
                it.remove();
                removeOrHide(entity, resync);
            }
        }
    }
}
