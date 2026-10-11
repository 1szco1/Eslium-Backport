package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnchorBuffer {
    private static final Map<BlockPos, Long> interacted = new ConcurrentHashMap<>();

    public static void addInteracted(BlockPos pos) {
        if (pos == null) return;
        interacted.put(pos.toImmutable(), System.currentTimeMillis());
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

    public static void handleExplosion(ExplosionS2CPacket packet) {
        if (packet == null) return;
        if (packet.getAffectedBlocks() != null) {
            for (BlockPos pos : packet.getAffectedBlocks()) {
                removeInteracted(pos);
            }
        }
        double px = packet.getX();
        double py = packet.getY();
        double pz = packet.getZ();
        interacted.keySet().removeIf(pos -> pos.getSquaredDistance(px, py, pz, false) <= 16.0);
    }

    public static void cleanupExpired() {
        long maxTime = 3000L;
        if (JsonConfig.config != null && JsonConfig.config.anchors != null && JsonConfig.config.anchors.maxTime != null) {
            maxTime = JsonConfig.config.anchors.maxTime;
        }
        long now = System.currentTimeMillis();
        final long limit = maxTime;
        interacted.entrySet().removeIf(entry -> (now - entry.getValue()) > limit);
    }

    public static void clear() {
        interacted.clear();
    }
}
