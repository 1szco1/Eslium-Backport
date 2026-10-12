package fr.solmey.clienthings.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.solmey.clienthings.ClienThings;
import fr.solmey.clienthings.data.Data;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(
        method = "send(Lnet/minecraft/network/Packet;)V",
        at = @At("TAIL")
    ) // When a packet is sent from the client
    private void eslium$send(
        Packet<?> packet,
        CallbackInfo callback
    ) {
        if (!ClienThings.shouldWork()) return;

        synchronized (Data.sentPackets) {
            Data.sentPackets.add(packet);
        }
    }
}
