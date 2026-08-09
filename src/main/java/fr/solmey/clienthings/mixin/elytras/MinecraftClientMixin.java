package fr.solmey.clienthings.mixin.elytras;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Elytras;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (JsonConfig.config.elytras.enabled && JsonConfig.shouldWork(JsonConfig.config.elytras.servers)) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.options.keyJump.isPressed()) {
                // Predict elytra deployment
            }
        }
    }
}
