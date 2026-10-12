package fr.solmey.clienthings.rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.solmey.clienthings.data.Data;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

public class InteractionManager {

    private static List<Entity> anticheatBypassEntities = new ArrayList<>();

    public static void showEntities() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientWorld clientLevel = minecraft.world;
        if (clientLevel == null) return;

        List<Integer> ids = new ArrayList<>();
        for (Map.Entry<Long, Packet<?>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<?> packet = entry.getValue();

            if (packet instanceof EntitySpawnS2CPacket) {
                ids.add(((EntitySpawnS2CPacket) packet).getId());
            }
        }

        for (Entity entity : anticheatBypassEntities) {
            if (ids.contains(entity.getEntityId())) {
                clientLevel.addEntity(entity.getEntityId(), entity);
                entity.removed = false;
            }
        }
        anticheatBypassEntities.clear();
    }

    public static void hideEntities() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientWorld clientLevel = minecraft.world;
        if (clientLevel == null) return;

        for (Map.Entry<Long, Packet<?>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<?> packet = entry.getValue();

            if (packet instanceof EntitySpawnS2CPacket) {
                Entity entity = clientLevel.getEntityById(((EntitySpawnS2CPacket) packet).getId());
                if (entity != null) {
                    anticheatBypassEntities.add(entity);
                    clientLevel.removeEntity(entity.getEntityId());
                }
            }
        }
    }
}
