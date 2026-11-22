package fr.solmey.clienthings.mixin.rollback;

import java.util.Iterator;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.solmey.clienthings.ClienThings;
import fr.solmey.clienthings.data.Data;
import fr.solmey.clienthings.rollback.InteractionManager;
import fr.solmey.clienthings.rollback.Rollback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onEntitySpawn", at = @At("TAIL"))
    private void eslium$onEntitySpawn(
        EntitySpawnS2CPacket packet,
        CallbackInfo ci
    ) {
        if (!ClienThings.shouldWork()) return;

        if (Data.extractedToBeValidedPackets.containsValue(packet))
            return;

        // A 1.16.5 client on a translated 1.21.11 server gets authoritative
        // packets whose coordinates and entity data differ from the prediction.
        // Match by type and proximity instead of exact field equality, otherwise
        // the predicted spawn is re-applied later as a duplicate (ghost) entity.
        Long bestKey = null;
        Packet<?> bestValue = null;
        EntitySpawnS2CPacket bestPacket = null;
        double bestDistanceSq = 4.0D * 4.0D;

        for (Map.Entry<Long, Packet<?>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<?> packetToCheck = entry.getValue();
            if (!(packetToCheck instanceof EntitySpawnS2CPacket)) continue;

            EntitySpawnS2CPacket cPacket = (EntitySpawnS2CPacket) packetToCheck;
            if (packet.getEntityTypeId() != cPacket.getEntityTypeId()) continue;

            double dx = packet.getX() - cPacket.getX();
            double dy = packet.getY() - cPacket.getY();
            double dz = packet.getZ() - cPacket.getZ();
            double distanceSq = dx * dx + dy * dy + dz * dz;
            if (distanceSq < bestDistanceSq) {
                bestDistanceSq = distanceSq;
                bestKey = entry.getKey();
                bestValue = entry.getValue();
                bestPacket = cPacket;
            }
        }

        if (bestPacket != null) {
            InteractionManager.showEntities();
            Rollback.rollback(bestPacket, packet);
            InteractionManager.hideEntities();
            Data.extractedToBeValidedPackets.remove(bestKey, bestValue);
        }
    }
}
