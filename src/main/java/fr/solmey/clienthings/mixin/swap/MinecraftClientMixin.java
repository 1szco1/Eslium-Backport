package fr.solmey.clienthings.mixin.swap;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Swap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void handleInputEvents(CallbackInfo ci) {
        if (JsonConfig.config.swap.enabled && JsonConfig.shouldWork(JsonConfig.config.swap.servers)) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if (player == null) return;

            // Access key bindings through options without importing KeyBinding class
            try {
                Object[] hotbarKeys = client.options.keysHotbar;
                for (int i = 0; i < hotbarKeys.length; i++) {
                    java.lang.reflect.Method isPressed = hotbarKeys[i].getClass().getMethod("isPressed");
                    if ((Boolean) isPressed.invoke(hotbarKeys[i])) {
                        // Predict hotbar swap
                    }
                }
            } catch (Exception e) {
                // Fallback if reflection fails
            }
        }
    }
}
