package net.solmey.eslium.mixin.optout;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.solmey.eslium.config.ConfigManager;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPlayNetworkHandlerMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void eslium$tick(CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().optout) {
            // Server-side opt-out handling.
        }
    }
}
