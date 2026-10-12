package net.solmey.eslium.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.phys.Vec3;
import net.solmey.eslium.config.ConfigManager;

public class AnchorBuffer {

    private static final Map<BlockPos, Long> interacted = new ConcurrentHashMap<>();

    public static void addInteracted(BlockPos pos) {
        if (pos == null) return;
        interacted.put(pos.immutable(), System.currentTimeMillis());
    }

    public static void removeInteracted(BlockPos pos) {
        if (pos == null) return;
        interacted.remove(pos);
    }

    public static boolean isInteracted(BlockPos pos) {
        if (pos == null) return false;
        cleanupExpired();
        return interacted.containsKey(pos);
    }

    public static Set<BlockPos> getInteractedPositions() {
        cleanupExpired();
        return interacted.keySet();
    }

    public static void handleExplosion(ClientboundExplodePacket packet) {
        if (packet == null) return;
        Vec3 center = packet.center();
        if (center == null) return;
        double radius = packet.radius() + 1.0;
        double limitSq = Math.max(16.0, radius * radius);
        interacted.keySet().removeIf(pos -> pos.distToCenterSqr(center) <= limitSq);
    }

    public static void cleanupExpired() {
        long maxTime = 3000L;
        if (ConfigManager.getConfig() != null && ConfigManager.getConfig().anchors != null) {
            maxTime = ConfigManager.getConfig().anchors.maxTime;
        }
        long now = System.currentTimeMillis();
        final long limit = maxTime;
        interacted.entrySet().removeIf(entry -> (now - entry.getValue()) > limit);
    }

    public static void clear() {
        interacted.clear();
    }
}
