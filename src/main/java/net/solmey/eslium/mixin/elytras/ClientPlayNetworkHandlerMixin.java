package net.solmey.eslium.mixin.elytras;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.solmey.eslium.config.ConfigManager;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
    private void eslium$handleEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().elytras.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().elytras.servers)) {
            // Elytra status updates are already handled client-side; hook kept for prediction tuning.
        }
    }
}
