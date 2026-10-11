package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockPrediction {
    private static final Map<BlockPos, Entry> PREDICTIONS = new ConcurrentHashMap<>();

    private static final class Entry {
        final BlockState previous;
        final long createdMs;
        volatile BlockState serverState;
        boolean sampled;

        Entry(BlockState previous, long createdMs) {
            this.previous = previous;
            this.createdMs = createdMs;
        }
    }

    private static boolean enabled() {
        if (JsonConfig.config == null || JsonConfig.config.placement == null) {
            return false;
        }
        if (!Boolean.TRUE.equals(JsonConfig.config.placement.enabled)) {
            return false;
        }
        return JsonConfig.shouldWork(JsonConfig.config.placement.servers);
    }

    public static void record(BlockState previous, BlockPos pos) {
        if (!enabled() || previous == null || pos == null) {
            return;
        }
        BlockPos key = pos.toImmutable();
        Entry existing = PREDICTIONS.get(key);
        BlockState base = existing != null && existing.previous != null ? existing.previous : previous;
        PREDICTIONS.put(key, new Entry(base, System.currentTimeMillis()));
    }

    public static boolean absorb(BlockPos pos, BlockState state) {
        if (!enabled() || pos == null || state == null) {
            return false;
        }
        Entry entry = PREDICTIONS.get(pos);
        if (entry == null) {
            return false;
        }
        if (!entry.sampled) {
            entry.sampled = true;
            LatencyEstimator.sample(System.currentTimeMillis() - entry.createdMs);
        }
        entry.serverState = state;
        return true;
    }

    public static void tick(ClientWorld world) {
        if (world == null) {
            clear();
            return;
        }
        if (PREDICTIONS.isEmpty()) {
            return;
        }

        long window = LatencyEstimator.placementWindowMs();
        long now = System.currentTimeMillis();

        Iterator<Map.Entry<BlockPos, Entry>> it = PREDICTIONS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BlockPos, Entry> mapEntry = it.next();
            BlockPos pos = mapEntry.getKey();
            Entry entry = mapEntry.getValue();
            if (now - entry.createdMs < window) {
                continue;
            }
            it.remove();

            BlockState target = entry.serverState != null ? entry.serverState : entry.previous;
            if (target == null) {
                continue;
            }
            if (!world.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4)) {
                continue;
            }
            if (world.getBlockState(pos) != target) {
                world.setBlockState(pos, target, 11);
            }
        }
    }

    public static void clear() {
        PREDICTIONS.clear();
    }
}
