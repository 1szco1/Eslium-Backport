package net.solmey.eslium.mixin.swap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.solmey.eslium.config.ConfigManager;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void eslium$handleKeybinds(CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().swap.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().swap.servers)) {
            Minecraft client = Minecraft.getInstance();
            LocalPlayer player = client.player;
            if (player == null) return;
            // Hotbar swap prediction is handled client-side; hook kept for prediction tuning.
        }
    }
}
