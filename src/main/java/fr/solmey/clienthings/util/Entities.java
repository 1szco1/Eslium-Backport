package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

public class Entities {
    public static float attackCooldownProgress = 0.0F;
    private static long[] timestamps = new long[256];
    private static Entity[] entities = new Entity[256];
    private static Entity[] initialEntities = new Entity[256];
    private static byte[] type = new byte[256];
    private static int fakeIdCounter = Integer.MAX_VALUE - 1000;

    public static final byte FAKE = 0;
    public static final byte TO_DESTROY = 1;
    public static final byte TO_CREATE = 2;
    public static final byte INITIAL = 3;
    public static final byte NOT_SYNCHRONISE = 4;
    public static final byte UNKNOWN = 127;

    public static final int CONSUMABLES = 0;
    public static final int FIREWORK = 1;
    public static final int WEAPONS = 2;
    public static final int WINDCHARGE = 3;

    public static int getNextFakeId() {
        return fakeIdCounter++;
    }

    public static void set(long _timestamp, Entity _Entity, Entity _initialEntity, byte _type) {
        clear();
        int cursor = -1;
        for (int i = 0; i < 256; i++) {
            if (timestamps[i] == 0) {
                cursor = i;
                break;
            }
        }
        if (cursor == -1) {
            long oldest = Long.MAX_VALUE;
            cursor = 0;
            for (int i = 0; i < 256; i++) {
                if (timestamps[i] < oldest) {
                    oldest = timestamps[i];
                    cursor = i;
                }
            }
            if (entities[cursor] != null) {
                remove(entities[cursor]);
            }
        }
        timestamps[cursor] = _timestamp;
        entities[cursor] = _Entity;
        initialEntities[cursor] = _initialEntity;
        type[cursor] = _type;
    }

    public static void clear() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < 256; i++) {
            if (entities[i] != null) {
                boolean shouldRemove = false;
                int maxTime = 5000;

                if (entities[i] instanceof EndCrystalEntity) {
                    if (JsonConfig.config != null && JsonConfig.config.crystals != null && JsonConfig.config.crystals.autoDestroy != null && JsonConfig.config.crystals.autoDestroy.maxTime != null) {
                        maxTime = JsonConfig.config.crystals.autoDestroy.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else if (entities[i] instanceof FireworkRocketEntity) {
                    if (JsonConfig.config != null && JsonConfig.config.firework != null && JsonConfig.config.firework.maxTime != null) {
                        maxTime = JsonConfig.config.firework.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else if (entities[i] instanceof TridentEntity) {
                    if (JsonConfig.config != null && JsonConfig.config.weapons != null && JsonConfig.config.weapons.maxTime != null) {
                        maxTime = JsonConfig.config.weapons.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else if (entities[i] instanceof AbstractMinecartEntity) {
                    if (JsonConfig.config != null && JsonConfig.config.minecart != null && JsonConfig.config.minecart.maxTime != null) {
                        maxTime = JsonConfig.config.minecart.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else {
                    shouldRemove = (now - timestamps[i] >= 5000) && timestamps[i] != 0;
                }

                if (shouldRemove) {
                    remove(entities[i]);
                }
            }
        }
    }

    public static void remove(Entity entity) {
        if (entity == null) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) return;
        ClientPlayerEntity player = mc.player;
        if (player == null || !(player.world instanceof ClientWorld)) return;
        ClientWorld clientWorld = (ClientWorld) player.world;
        entity.remove();
        clientWorld.removeEntity(entity.getEntityId());

        for (int i = 0; i < entities.length; i++) {
            if (entities[i] == entity) {
                timestamps[i] = 0;
                entities[i] = null;
                initialEntities[i] = null;
                type[i] = UNKNOWN;
            }
        }
    }

    public static boolean has(Entity entity, byte _type) {
        if (entity == null) return false;
        for (int i = 0; i < entities.length; i++)
            if (entities[i] == entity && type[i] == _type)
                return true;
        return false;
    }

    public static byte getType(Entity entity) {
        if (entity == null) return UNKNOWN;
        for (int i = 0; i < entities.length; i++)
            if (entities[i] == entity)
                return type[i];
        return UNKNOWN;
    }

    public static boolean needToCancel(EntityS2CPacket packet) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null) return false;
        ClientPlayerEntity player = mc.player;
        if (player == null || !(player.world instanceof ClientWorld)) return false;
        ClientWorld clientWorld = (ClientWorld) player.world;

        Entity entity = packet.getEntity(clientWorld);
        if (entity != null && packet.isPositionChanged() && has(entity, NOT_SYNCHRONISE)) {
            return true;
        }
        return false;
    }

    public static boolean needToCancel(EntitySpawnS2CPacket packet) {
        clear();
        boolean needed = false;
        int cursorFake = -1;
        double bestDistance = Double.MAX_VALUE;

        for (int i = 0; i < 256; i++) {
            if (initialEntities[i] != null && type[i] == FAKE) {
                double dx = packet.getX() - initialEntities[i].getX();
                double dy = packet.getY() - initialEntities[i].getY();
                double dz = packet.getZ() - initialEntities[i].getZ();
                double dist = dx * dx + dy * dy + dz * dz;

                if (dist < bestDistance) {
                    bestDistance = dist;
                    cursorFake = i;
                    needed = true;
                }
            }
        }

        if (needed && bestDistance <= 16.0D) {
            if (JsonConfig.config != null && JsonConfig.config.debug) {
                System.out.println("[Eslium] Matched fake entity, removing it. Distance: " + Math.sqrt(bestDistance));
            }
            if (cursorFake >= 0 && entities[cursorFake] != null) {
                remove(entities[cursorFake]);
            }
            timestamps[cursorFake] = 0;
            entities[cursorFake] = null;
            initialEntities[cursorFake] = null;
            type[cursorFake] = UNKNOWN;
            return false;
        }

        return false;
    }
}
