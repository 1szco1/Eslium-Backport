package fr.solmey.clienthings.rollback;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

public class Rollback {

    public static void rollback(Packet<?> packet, @Nullable Packet<?> realPacket) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientWorld clientLevel = mc.world;
        if (clientLevel == null) return;

        if (packet instanceof EntitySpawnS2CPacket) {
            EntitySpawnS2CPacket clientboundAddEntityPacket = (EntitySpawnS2CPacket) packet;
            Entity entity = null;
            Entity realEntity = null;

            EntitySpawnS2CPacket realClientboundAddEntityPacket = null;
            if (realPacket instanceof EntitySpawnS2CPacket) {
                realClientboundAddEntityPacket = (EntitySpawnS2CPacket) realPacket;
            }

            entity = clientLevel.getEntityById(clientboundAddEntityPacket.getId());
            if (realClientboundAddEntityPacket != null) {
                realEntity = clientLevel.getEntityById(realClientboundAddEntityPacket.getId());
            }

            if (entity instanceof EndCrystalEntity && realEntity instanceof EndCrystalEntity) {
                ((EndCrystalEntity) realEntity).endCrystalAge = ((EndCrystalEntity) entity).endCrystalAge;
            }
            clientLevel.removeEntity(clientboundAddEntityPacket.getId());
        }
    }

    public static void rollback(Packet<?> packet) {
        rollback(packet, null);
    }
}
