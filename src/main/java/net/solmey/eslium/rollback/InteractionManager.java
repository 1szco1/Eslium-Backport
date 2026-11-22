package net.solmey.eslium.rollback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.solmey.eslium.data.Data;

public class InteractionManager {

    private static List<Entity> anticheatBypassEntities = new ArrayList<>();

    public static void clear() {
        anticheatBypassEntities.clear();
    }

    public static void showEntities() {
        if (Data.extractedToBeValidedPackets.isEmpty() && anticheatBypassEntities.isEmpty()) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !(player.level() instanceof ClientLevel clientLevel)) {
            anticheatBypassEntities.clear();
            return;
        }

        Set<Integer> ids = new HashSet<>();
        for (Map.Entry<Long, Packet<ClientGamePacketListener>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<ClientGamePacketListener> packet = entry.getValue();
            if (packet instanceof ClientboundAddEntityPacket addEntityPacket) {
                ids.add(addEntityPacket.getId());
            }
        }

        for (Entity entity : anticheatBypassEntities) {
            if (ids.contains(entity.getId()) && clientLevel.getEntity(entity.getId()) == null) {
                clientLevel.addEntity(entity);
                entity.removalReason = null;
            }
        }
        anticheatBypassEntities.clear();
    }

    public static void hideEntities() {
        if (Data.extractedToBeValidedPackets.isEmpty()) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !(player.level() instanceof ClientLevel clientLevel)) return;

        for (Map.Entry<Long, Packet<ClientGamePacketListener>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<ClientGamePacketListener> packet = entry.getValue();
            if (packet instanceof ClientboundAddEntityPacket addEntityPacket) {
                Entity entity = clientLevel.getEntity(addEntityPacket.getId());
                if (entity != null && !anticheatBypassEntities.contains(entity)) {
                    anticheatBypassEntities.add(entity);
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }
}
