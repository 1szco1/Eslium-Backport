package net.solmey.eslium.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.solmey.eslium.config.ConfigManager;

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

    private static boolean isThrowable(Entity entity) {
        return entity instanceof ThrownEgg
                || entity instanceof ThrownEnderpearl
                || entity instanceof ThrownExperienceBottle
                || entity instanceof EyeOfEnder
                || entity instanceof ThrownLingeringPotion
                || entity instanceof ThrownSplashPotion;
    }

    private static boolean isWindCharge(Entity entity) {
        return entity instanceof WindCharge;
    }

    public static int getNextFakeId() {
        return fakeIdCounter++;
    }

    public static void set(long timestamp, Entity entity, Entity initialEntity, byte entityType) {
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
        timestamps[cursor] = timestamp;
        entities[cursor] = entity;
        initialEntities[cursor] = initialEntity;
        type[cursor] = entityType;
    }

    public static void clearAll() {
        for (Entity entity : entities) remove(entity);
    }

    public static void clear() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < 256; i++) {
            if (entities[i] != null) {
                boolean shouldRemove = false;
                int maxTime = 5000;

                if (entities[i] instanceof EndCrystal) {
                    if (ConfigManager.getConfig() != null && ConfigManager.getConfig().crystals != null
                            && ConfigManager.getConfig().crystals.autoDestroy != null) {
                        maxTime = ConfigManager.getConfig().crystals.autoDestroy.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else if (entities[i] instanceof FireworkRocketEntity) {
                    if (ConfigManager.getConfig() != null && ConfigManager.getConfig().firework != null) {
                        maxTime = ConfigManager.getConfig().firework.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else if (entities[i] instanceof ThrownTrident) {
                    if (ConfigManager.getConfig() != null && ConfigManager.getConfig().weapons != null) {
                        maxTime = ConfigManager.getConfig().weapons.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else if (entities[i] instanceof AbstractMinecart) {
                    if (ConfigManager.getConfig() != null && ConfigManager.getConfig().minecart != null) {
                        maxTime = ConfigManager.getConfig().minecart.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else if (isThrowable(entities[i])) {
                    if (ConfigManager.getConfig() != null && ConfigManager.getConfig().throwables != null) {
                        maxTime = ConfigManager.getConfig().throwables.maxTime;
                    }
                    shouldRemove = (now - timestamps[i] >= maxTime) && timestamps[i] != 0;
                } else if (isWindCharge(entities[i])) {
                    if (ConfigManager.getConfig() != null && ConfigManager.getConfig().windcharge != null) {
                        maxTime = ConfigManager.getConfig().windcharge.maxTime;
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
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc == null ? null : mc.player;
        if (player != null && player.level() instanceof ClientLevel clientLevel) {
            entity.discard();
            clientLevel.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
        }

        for (int i = 0; i < entities.length; i++) {
            if (entities[i] == entity) {
                timestamps[i] = 0;
                entities[i] = null;
                initialEntities[i] = null;
                type[i] = UNKNOWN;
            }
        }
    }

    public static boolean has(Entity entity, byte entityType) {
        if (entity == null) return false;
        for (int i = 0; i < entities.length; i++)
            if (entities[i] == entity && type[i] == entityType)
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

    public static boolean needToCancel(ClientboundMoveEntityPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return false;
        LocalPlayer player = mc.player;
        if (player == null || !(player.level() instanceof ClientLevel clientLevel)) return false;

        Entity entity = packet.getEntity(clientLevel);
        if (entity != null && packet.hasPosition() && has(entity, NOT_SYNCHRONISE)) {
            return true;
        }
        return false;
    }

    public static boolean needToCancel(ClientboundAddEntityPacket packet) {
        clear();
        boolean needed = false;
        int cursorFake = -1;
        double bestDistance = Double.MAX_VALUE;

        for (int i = 0; i < 256; i++) {
            if (initialEntities[i] == null || type[i] != FAKE) continue;

            Entity fake = initialEntities[i];
            if (packet.getType() != fake.getType()) continue;

            double maxDistance = 16.0D;
            if (isThrowable(fake) && ConfigManager.getConfig() != null
                    && ConfigManager.getConfig().throwables != null) {
                maxDistance = ConfigManager.getConfig().throwables.maxDistance;
            } else if (isWindCharge(fake) && ConfigManager.getConfig() != null
                    && ConfigManager.getConfig().windcharge != null) {
                maxDistance = ConfigManager.getConfig().windcharge.maxDistance;
            }

            double dx = packet.getX() - fake.getX();
            double dy = packet.getY() - fake.getY();
            double dz = packet.getZ() - fake.getZ();
            double dist = dx * dx + dy * dy + dz * dz;

            if (dist <= maxDistance * maxDistance && dist < bestDistance) {
                bestDistance = dist;
                cursorFake = i;
                needed = true;
            }
        }

        if (needed) {
            if (ConfigManager.getConfig() != null && ConfigManager.getConfig().debug) {
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
