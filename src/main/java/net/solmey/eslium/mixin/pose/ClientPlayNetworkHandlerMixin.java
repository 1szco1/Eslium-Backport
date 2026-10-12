package net.solmey.eslium.mixin.pose;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.solmey.eslium.config.ConfigManager;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "handleSetEntityData", at = @At("HEAD"), cancellable = true)
    private void eslium$handleSetEntityData(ClientboundSetEntityDataPacket packet, CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().pose.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().pose.servers)) {
            // Pose updates are server-synced; hook kept for client-side prediction tuning.
        }
    }
}
