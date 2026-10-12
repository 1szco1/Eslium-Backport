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

        Iterator<Map.Entry<Long, Packet<?>>> iterator =
                Data.extractedToBeValidedPackets.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Packet<?>> entry = iterator.next();
            Packet<?> packetToCheck = entry.getValue();

            if (packetToCheck instanceof EntitySpawnS2CPacket) {
                EntitySpawnS2CPacket cPacket = (EntitySpawnS2CPacket) packetToCheck;

                if (
                    packet.getEntityTypeId() == cPacket.getEntityTypeId() &&
                    packet.getX() == cPacket.getX() &&
                    packet.getY() == cPacket.getY() &&
                    packet.getZ() == cPacket.getZ() &&
                    packet.getPitch() == cPacket.getPitch() &&
                    packet.getYaw() == cPacket.getYaw() &&
                    packet.getEntityData() == cPacket.getEntityData()
                ) {
                    InteractionManager.showEntities();
                    Rollback.rollback(cPacket, packet);
                    InteractionManager.hideEntities();

                    iterator.remove();
                    return;
                }
            }
        }
    }
}
