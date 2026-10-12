package fr.solmey.clienthings.mixin.anticheat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.solmey.clienthings.ClienThings;
import fr.solmey.clienthings.rollback.InteractionManager;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class MinecraftMixin {

    // pick is inside render for the outlines on blocks too
    @Inject(method = "render", at = @At("HEAD"))
    private void eslium$renderFrameHEAD(
        float tickDelta,
        long startTime,
        boolean tick,
        CallbackInfo ci
    ) {
        if (!ClienThings.shouldWork()) return;

        InteractionManager.showEntities();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void eslium$renderFrameTAIL(
        float tickDelta,
        long startTime,
        boolean tick,
        CallbackInfo ci
    ) {
        if (!ClienThings.shouldWork()) return;

        InteractionManager.hideEntities();
    }
}
