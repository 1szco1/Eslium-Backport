package net.solmey.eslium.mixin;

import java.util.Iterator;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PingDebugMonitor;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.rollback.InteractionManager;
import net.solmey.eslium.rollback.Rollback;
import net.solmey.eslium.util.AnchorBuffer;
import net.solmey.eslium.util.BlockPrediction;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Shadow
    private PingDebugMonitor pingDebugMonitor;

    @Inject(method = "tick", at = @At("HEAD"))
    // Vanilla only ticks the ping monitor while the debug network charts are visible.
    // Ticking it here the rest of the time keeps latency samples fresh (previously a @Redirect,
    // which is malformed on old MixinExtras builds and crashed the game).
    private void eslium$keepLatencyFresh(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null && !minecraft.getDebugOverlay().showNetworkCharts()) {
            this.pingDebugMonitor.tick();
        }
    }

    @Inject(method = "handleBlockUpdate", at = @At("HEAD"))
    private void eslium$handleBlockUpdate(ClientboundBlockUpdatePacket packet, CallbackInfo ci) {
        if (packet != null) {
            AnchorBuffer.removeInteracted(packet.getPos());
            BlockPrediction.absorb(packet.getPos(), packet.getBlockState());
        }
    }

    @Inject(method = "handleExplosion", at = @At("HEAD"))
    private void eslium$handleExplosion(ClientboundExplodePacket packet, CallbackInfo ci) {
        if (packet != null) {
            AnchorBuffer.handleExplosion(packet);
        }
    }

    /*@Inject(
        method = "handleAddEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
            shift = At.Shift.AFTER
        )
    )*/
    @Inject(method = "handleAddEntity", at = @At("TAIL"))
    private void eslium$handleAddEntity(
        ClientboundAddEntityPacket packet,
        CallbackInfo ci
    ) {
        if (!Eslium.shouldWork()) return;

        if(Data.extractedToBeValidedPackets.containsValue(packet))
            return;

        Iterator<Map.Entry<Long, Packet<ClientGamePacketListener>>> iterator =
                Data.extractedToBeValidedPackets.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Packet<ClientGamePacketListener>> entry = iterator.next();
            Packet<ClientGamePacketListener> packetToCheck = entry.getValue();


            if(packetToCheck instanceof ClientboundAddEntityPacket cPacket) {
                if(
                    packet.getType().equals( cPacket.getType() )  &&
                    packet.getX() == cPacket.getX()               &&
                    packet.getY() == cPacket.getY()               &&
                    packet.getZ() == cPacket.getZ()               &&
                    packet.getXRot() == cPacket.getXRot()         &&
                    packet.getYRot() == cPacket.getYRot()         &&
                    packet.getYHeadRot() == cPacket.getYHeadRot()
                ) {
                    //System.out.println("ROLLBACK\n");
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
