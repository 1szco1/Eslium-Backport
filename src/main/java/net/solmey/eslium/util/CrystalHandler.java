package net.solmey.eslium.util;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.solmey.eslium.config.ConfigManager;

public class CrystalHandler {
    private static final Map<Entity, Integer> HIDDEN = new IdentityHashMap<>();
    private static final Map<Entity, Long> SCHEDULED = new IdentityHashMap<>();

    public static boolean tryHit(EndCrystal crystal, DamageSource source, float amount) {
        if (crystal == null || source == null) return false;

        Minecraft client = Minecraft.getInstance();
        if (client == null || client.player == null) return false;
        if (!(crystal.level() instanceof ClientLevel)) return false;
        if (crystal.level() != client.player.level()) return false;
        if (crystal.isRemoved() || !crystal.isAlive()) return false;
        if (!(amount > 0.0F)) return false;

        if (ConfigManager.getConfig() == null || ConfigManager.getConfig().crystals == null) return false;
        if (!ConfigManager.getConfig().crystals.enabled) return false;
        if (!ConfigManager.shouldWork(ConfigManager.getConfig().crystals.servers)) return false;

        if (client.isLocalServer()) return false;

        LocalPlayer player = client.player;
        Entity attacker = source.getEntity();
        if (attacker == null || attacker != player) return false;

        double attackDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (!(attackDamage > 0.0D)) return false;

        byte type = Entities.getType(crystal);
        if (type == Entities.FAKE || type == Entities.TO_DESTROY) {
            Entities.remove(crystal);
            HIDDEN.remove(crystal);
            SCHEDULED.remove(crystal);
            return true;
        }

        long delay;
        if (ConfigManager.getConfig().crystals.dynamicDelay) {
            delay = LatencyEstimator.estimateMs(0, 200);
        } else {
            delay = ConfigManager.getConfig().crystals.delay;
            if (delay > 200) delay = 200;
        }
        int resync = ConfigManager.getConfig().crystals.resync;
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
            entity.discard();
        }
    }

    public static boolean isHidden(Entity entity) {
        return entity != null && HIDDEN.containsKey(entity);
    }

    public static void tick() {
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.level == null) {
            HIDDEN.clear();
            SCHEDULED.clear();
            return;
        }

        if (!HIDDEN.isEmpty()) {
            Iterator<Map.Entry<Entity, Integer>> it = HIDDEN.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Entity, Integer> entry = it.next();
                Entity entity = entry.getKey();
                if (entity.isRemoved() || !entity.isAlive() || entity.level() != client.level) {
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
            if (ConfigManager.getConfig() != null && ConfigManager.getConfig().crystals != null) {
                resync = ConfigManager.getConfig().crystals.resync;
            }
            if (resync < 0) resync = 0;
            if (resync > 50) resync = 50;

            long now = System.nanoTime();
            Iterator<Map.Entry<Entity, Long>> it = SCHEDULED.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Entity, Long> entry = it.next();
                Entity entity = entry.getKey();
                if (entity.isRemoved() || !entity.isAlive() || entity.level() != client.level) {
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
