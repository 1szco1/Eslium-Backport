package net.solmey.eslium.mixin.system;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.solmey.eslium.util.Entities;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handleAddEntity", at = @At("HEAD"))
    private void eslium$handleAddEntity(ClientboundAddEntityPacket packet, CallbackInfo ci) {
        Entities.needToCancel(packet);
    }

    @Inject(method = "handleMoveEntity", at = @At("HEAD"), cancellable = true)
    private void eslium$handleMoveEntity(ClientboundMoveEntityPacket packet, CallbackInfo ci) {
        if (Entities.needToCancel(packet)) {
            ci.cancel();
        }
    }
}
