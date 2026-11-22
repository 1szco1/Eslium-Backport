package fr.solmey.clienthings.rollback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.solmey.clienthings.data.Data;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

public class InteractionManager {

    private static List<Entity> anticheatBypassEntities = new ArrayList<>();

    public static void clear() {
        anticheatBypassEntities.clear();
    }

    public static void showEntities() {
        if (Data.extractedToBeValidedPackets.isEmpty() && anticheatBypassEntities.isEmpty()) return;

        ClientWorld clientLevel = MinecraftClient.getInstance().world;
        if (clientLevel == null) {
            anticheatBypassEntities.clear();
            return;
        }

        Set<Integer> ids = new HashSet<>();
        for (Map.Entry<Long, Packet<?>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<?> packet = entry.getValue();
            if (packet instanceof EntitySpawnS2CPacket) {
                ids.add(((EntitySpawnS2CPacket) packet).getId());
            }
        }

        for (Entity entity : anticheatBypassEntities) {
            int id = entity.getEntityId();
            if (ids.contains(id) && clientLevel.getEntityById(id) == null) {
                clientLevel.addEntity(id, entity);
                entity.removed = false;
            }
        }
        anticheatBypassEntities.clear();
    }

    public static void hideEntities() {
        if (Data.extractedToBeValidedPackets.isEmpty()) return;

        ClientWorld clientLevel = MinecraftClient.getInstance().world;
        if (clientLevel == null) return;

        for (Map.Entry<Long, Packet<?>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<?> packet = entry.getValue();
            if (packet instanceof EntitySpawnS2CPacket) {
                Entity entity = clientLevel.getEntityById(((EntitySpawnS2CPacket) packet).getId());
                if (entity != null && !anticheatBypassEntities.contains(entity)) {
                    anticheatBypassEntities.add(entity);
                    clientLevel.removeEntity(entity.getEntityId());
                }
            }
        }
    }
}
