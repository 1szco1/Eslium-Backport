package net.solmey.eslium.mixin.anchors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.solmey.eslium.util.AnchorHandler;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "pick", at = @At("TAIL"))
    private void eslium$pick(float partialTicks, CallbackInfo ci) {
        AnchorHandler.handleReInteract(Minecraft.getInstance(), partialTicks);
    }
}
