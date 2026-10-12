package net.solmey.eslium.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.solmey.eslium.config.ConfigManager;

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
        if (ConfigManager.getConfig() == null || ConfigManager.getConfig().placement == null) {
            return false;
        }
        if (!ConfigManager.getConfig().placement.enabled) {
            return false;
        }
        return ConfigManager.shouldWork(ConfigManager.getConfig().placement.servers);
    }

    public static void record(BlockState previous, BlockPos pos) {
        if (!enabled() || previous == null || pos == null) {
            return;
        }
        BlockPos key = pos.immutable();
        Entry existing = PREDICTIONS.get(key);
        BlockState base = existing != null && existing.previous != null ? existing.previous : previous;
        PREDICTIONS.put(key, new Entry(base, System.currentTimeMillis()));
    }

    public static void absorb(BlockPos pos, BlockState state) {
        if (!enabled() || pos == null || state == null) {
            return;
        }
        Entry entry = PREDICTIONS.get(pos);
        if (entry == null) {
            return;
        }
        if (!entry.sampled) {
            entry.sampled = true;
            LatencyEstimator.sample(System.currentTimeMillis() - entry.createdMs);
        }
        entry.serverState = state;
    }

    public static void tick(ClientLevel world) {
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

            if (!world.hasChunk(pos.getX() >> 4, pos.getZ() >> 4)) {
                continue;
            }

            BlockState target = entry.serverState != null ? entry.serverState : entry.previous;
            if (target != null && world.getBlockState(pos) != target) {
                world.setBlock(pos, target, 11);
            }
        }
    }

    public static void clear() {
        PREDICTIONS.clear();
    }
}
