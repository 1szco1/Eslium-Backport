package net.solmey.eslium.util;

import java.util.Optional;
import java.util.Set;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.solmey.eslium.config.ConfigManager;

public class AnchorHandler {

    public static void handleAnchorDetonation(Minecraft client, Level level, BlockPos pos, BlockHitResult hit) {
        if (level == null || pos == null) return;

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        AnchorBuffer.addInteracted(pos);

        if (ConfigManager.getConfig() != null && ConfigManager.getConfig().anchors != null
                && ConfigManager.getConfig().anchors.clientExplosion) {
            destroyAdjacentGlowstone(level, pos);
        }
    }

    public static void handleReInteract(Minecraft client, float tickDelta) {
        if (client == null || client.level == null) return;
        if (ConfigManager.getConfig() == null || ConfigManager.getConfig().anchors == null
                || !ConfigManager.getConfig().anchors.enabled) return;
        if (!ConfigManager.getConfig().anchors.reInteract) return;
        if (!ConfigManager.shouldWork(ConfigManager.getConfig().anchors.servers)) return;

        Entity camera = client.getCameraEntity();
        if (camera == null) return;

        Set<BlockPos> interacted = AnchorBuffer.getInteractedPositions();
        if (interacted.isEmpty()) return;

        Vec3 cameraPos = camera.getEyePosition(tickDelta);
        Vec3 rotation = camera.getViewVector(tickDelta);
        double reach = client.player != null ? client.player.blockInteractionRange() : 4.5;
        Vec3 reachVec = cameraPos.add(rotation.scale(reach));

        HitResult currentHit = client.hitResult;
        double maxDistSq = currentHit == null || currentHit.getType() == HitResult.Type.MISS
                ? reach * reach
                : cameraPos.distanceToSqr(currentHit.getLocation());

        BlockHitResult closestAnchorHit = null;
        double closestDistSq = maxDistSq;

        for (BlockPos pos : interacted) {
            if (!client.level.getBlockState(pos).isAir()) continue;
            AABB box = new AABB(pos);
            Optional<Vec3> intersection = box.clip(cameraPos, reachVec);
            if (intersection.isPresent()) {
                double distSq = cameraPos.distanceToSqr(intersection.get());
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    Vec3 hitPoint = intersection.get();
                    closestAnchorHit = new BlockHitResult(hitPoint, getHitSide(pos, hitPoint, cameraPos), pos, false);
                }
            }
        }

        if (closestAnchorHit != null) {
            client.hitResult = closestAnchorHit;
            client.crosshairPickEntity = null;
        }
    }

    private static Direction getHitSide(BlockPos pos, Vec3 hitPos, Vec3 cameraPos) {
        if (Math.abs(hitPos.y - (pos.getY() + 1.0)) < 1e-4) return Direction.UP;
        if (Math.abs(hitPos.y - pos.getY()) < 1e-4) return Direction.DOWN;
        if (Math.abs(hitPos.z - (pos.getZ() + 1.0)) < 1e-4) return Direction.SOUTH;
        if (Math.abs(hitPos.z - pos.getZ()) < 1e-4) return Direction.NORTH;
        if (Math.abs(hitPos.x - (pos.getX() + 1.0)) < 1e-4) return Direction.EAST;
        if (Math.abs(hitPos.x - pos.getX()) < 1e-4) return Direction.WEST;
        return nearest(cameraPos.x - (pos.getX() + 0.5),
                cameraPos.y - (pos.getY() + 0.5),
                cameraPos.z - (pos.getZ() + 0.5));
    }

    private static Direction nearest(double dx, double dy, double dz) {
        double ax = Math.abs(dx), ay = Math.abs(dy), az = Math.abs(dz);
        if (ax >= ay && ax >= az) return dx > 0 ? Direction.EAST : Direction.WEST;
        if (ay >= az) return dy > 0 ? Direction.UP : Direction.DOWN;
        return dz > 0 ? Direction.SOUTH : Direction.NORTH;
    }

    public static void destroyAdjacentGlowstone(Level level, BlockPos center) {
        if (level == null || center == null) return;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= 8) {
                        BlockPos p = center.offset(dx, dy, dz);
                        if (level.getBlockState(p).getBlock() == Blocks.GLOWSTONE) {
                            level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    public static void renderGhostAnchors(WorldRenderContext context) {
        if (context == null) return;
        if (ConfigManager.getConfig() == null || ConfigManager.getConfig().anchors == null
                || !ConfigManager.getConfig().anchors.enabled) return;
        if (!ConfigManager.getConfig().anchors.ghostAnchor) return;
        if (!ConfigManager.shouldWork(ConfigManager.getConfig().anchors.servers)) return;

        Set<BlockPos> positions = AnchorBuffer.getInteractedPositions();
        if (positions.isEmpty()) return;

        PoseStack pose = context.matrices();
        MultiBufferSource consumers = context.consumers();
        if (pose == null || consumers == null) return;

        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().position();
        VertexConsumer vc = consumers.getBuffer(RenderTypes.LINES);
        int color = ARGB.colorFromFloat(0.6f, 0.7f, 0.2f, 0.9f);
        float lineWidth = 2.0f;

        for (BlockPos pos : positions) {
            ShapeRenderer.renderShape(pose, vc, Shapes.block(),
                    pos.getX() - cam.x, pos.getY() - cam.y, pos.getZ() - cam.z,
                    color, lineWidth);
        }
    }
}
