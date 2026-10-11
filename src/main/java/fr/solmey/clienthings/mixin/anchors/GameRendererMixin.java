package fr.solmey.clienthings.mixin.anchors;

import fr.solmey.clienthings.util.AnchorHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "updateTargetedEntity", at = @At("TAIL"))
    private void onUpdateTargetedEntity(float tickDelta, CallbackInfo ci) {
        AnchorHandler.handleReInteract(this.client, tickDelta);
    }
}
