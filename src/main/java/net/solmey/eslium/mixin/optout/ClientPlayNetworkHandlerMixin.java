package net.solmey.eslium.mixin.optout;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.solmey.eslium.config.ConfigManager;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "handleSystemChat", at = @At("HEAD"), cancellable = true)
    private void eslium$handleSystemChat(ClientboundSystemChatPacket packet, CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().optout) {
            String message = packet.content().getString();
            if (message.contains("Eslium") || message.contains("clienthings")) {
                // Handle opt-out messages.
            }
        }
    }
}
