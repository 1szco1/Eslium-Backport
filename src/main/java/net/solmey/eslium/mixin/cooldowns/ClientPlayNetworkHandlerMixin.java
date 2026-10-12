package net.solmey.eslium.mixin.cooldowns;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.solmey.eslium.config.ConfigManager;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "handleItemCooldown", at = @At("HEAD"), cancellable = true)
    private void eslium$handleItemCooldown(ClientboundCooldownPacket packet, CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().cooldowns.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().cooldowns.servers)) {
            // Cooldowns are server-driven; hook kept for client-side prediction tuning.
        }
    }
}
