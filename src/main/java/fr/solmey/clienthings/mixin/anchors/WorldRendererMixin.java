package fr.solmey.clienthings.mixin.anchors;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.AnchorBuffer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/debug/DebugRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V"))
    private void onRenderGhostAnchors(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline,
                                      Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager,
                                      Matrix4f matrix4f, CallbackInfo ci) {
        if (JsonConfig.config == null || JsonConfig.config.anchors == null || !Boolean.TRUE.equals(JsonConfig.config.anchors.enabled)) return;
        if (!Boolean.TRUE.equals(JsonConfig.config.anchors.ghostAnchor)) return;
        if (!JsonConfig.shouldWork(JsonConfig.config.anchors.servers)) return;

        Set<BlockPos> positions = AnchorBuffer.getInteractedPositions();
        if (positions.isEmpty() || camera == null) return;

        Vec3d cam = camera.getPos();
        VertexConsumer consumer = this.bufferBuilders.getEntityVertexConsumers().getBuffer(RenderLayer.getLines());

        for (BlockPos pos : positions) {
            matrices.push();
            matrices.translate(pos.getX() - cam.x, pos.getY() - cam.y, pos.getZ() - cam.z);
            WorldRenderer.drawBox(matrices, consumer, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.7F, 0.2F, 0.9F, 0.6F);
            matrices.pop();
        }
    }
}
