package net.solmey.eslium.mixin.elytras;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.solmey.eslium.config.ConfigManager;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void eslium$tick(CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().elytras.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().elytras.servers)) {
            Minecraft client = Minecraft.getInstance();
            if (client.player != null && client.options.keyJump.isDown()) {
                // Elytra deployment is already client-predicted; hook kept for prediction tuning.
            }
        }
    }
}
