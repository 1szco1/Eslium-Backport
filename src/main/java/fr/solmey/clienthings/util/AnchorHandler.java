package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Set;

public class AnchorHandler {

    public static void handleAnchorDetonation(MinecraftClient client, World world, BlockPos pos, BlockHitResult hit) {
        if (world == null || pos == null) return;

        // 1. Immediately remove anchor block on client-side (no waiting for server packet)
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);

        // 2. Add to buffer for raycast retention & ghost anchor rendering
        AnchorBuffer.addInteracted(pos);

        // 3. Clear adjacent glowstone blocks to prevent placement blockage (Walksy optimization)
        if (JsonConfig.config != null && JsonConfig.config.anchors != null && Boolean.TRUE.equals(JsonConfig.config.anchors.clientExplosion)) {
            destroyAdjacentGlowstone(world, pos);
        }
    }

    public static void handleReInteract(MinecraftClient client, float tickDelta) {
        if (client == null || client.world == null) return;
        if (JsonConfig.config == null || JsonConfig.config.anchors == null || !Boolean.TRUE.equals(JsonConfig.config.anchors.enabled)) return;
        if (!Boolean.TRUE.equals(JsonConfig.config.anchors.reInteract)) return;
        if (!JsonConfig.shouldWork(JsonConfig.config.anchors.servers)) return;

        Entity camera = client.getCameraEntity();
        if (camera == null) return;

        Set<BlockPos> interacted = AnchorBuffer.getInteractedPositions();
        if (interacted.isEmpty()) return;

        Vec3d cameraPos = camera.getCameraPosVec(tickDelta);
        Vec3d rotation = camera.getRotationVec(tickDelta);
        double reach = client.interactionManager != null ? client.interactionManager.getReachDistance() : 4.5;
        Vec3d reachVec = cameraPos.add(rotation.multiply(reach));

        HitResult currentHit = client.crosshairTarget;
        double maxDistSq = currentHit == null || currentHit.getType() == HitResult.Type.MISS
                ? reach * reach
                : cameraPos.squaredDistanceTo(currentHit.getPos());

        BlockHitResult closestAnchorHit = null;
        double closestDistSq = maxDistSq;

        for (BlockPos pos : interacted) {
            if (!client.world.getBlockState(pos).isAir()) {
                continue;
            }
            Box box = new Box(pos);
            Optional<Vec3d> intersection = box.raycast(cameraPos, reachVec);
            if (intersection.isPresent()) {
                double distSq = cameraPos.squaredDistanceTo(intersection.get());
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    Vec3d hitPoint = intersection.get();
                    Direction hitSide = getHitSide(pos, hitPoint, cameraPos);
                    closestAnchorHit = new BlockHitResult(hitPoint, hitSide, pos, false);
                }
            }
        }

        if (closestAnchorHit != null) {
            client.crosshairTarget = closestAnchorHit;
            client.targetedEntity = null;
        }
    }

    private static Direction getHitSide(BlockPos pos, Vec3d hitPos, Vec3d cameraPos) {
        if (Math.abs(hitPos.y - (pos.getY() + 1.0)) < 1e-4) return Direction.UP;
        if (Math.abs(hitPos.y - pos.getY()) < 1e-4) return Direction.DOWN;
        if (Math.abs(hitPos.z - (pos.getZ() + 1.0)) < 1e-4) return Direction.SOUTH;
        if (Math.abs(hitPos.z - pos.getZ()) < 1e-4) return Direction.NORTH;
        if (Math.abs(hitPos.x - (pos.getX() + 1.0)) < 1e-4) return Direction.EAST;
        if (Math.abs(hitPos.x - pos.getX()) < 1e-4) return Direction.WEST;
        return Direction.getFacing(cameraPos.x - (pos.getX() + 0.5),
                cameraPos.y - (pos.getY() + 0.5),
                cameraPos.z - (pos.getZ() + 0.5));
    }

    public static void destroyAdjacentGlowstone(World world, BlockPos center) {
        if (world == null || center == null) return;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= 8) {
                        BlockPos p = center.add(dx, dy, dz);
                        if (world.getBlockState(p).getBlock() == Blocks.GLOWSTONE) {
                            world.setBlockState(p, Blocks.AIR.getDefaultState(), 3);
                        }
                    }
                }
            }
        }
    }
}
