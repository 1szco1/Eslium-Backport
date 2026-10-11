package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

public class LatencyEstimator {
    private static boolean hasSample = false;
    private static double echoMs = 50.0;

    public static synchronized void sample(long roundTripMs) {
        if (roundTripMs < 0L) {
            return;
        }
        if (!hasSample) {
            echoMs = roundTripMs;
            hasSample = true;
        } else {
            echoMs = echoMs * 0.8 + roundTripMs * 0.2;
        }
    }

    public static int pingMs() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.getNetworkHandler() == null) {
            return 0;
        }
        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        return entry == null ? 0 : entry.getLatency();
    }

    public static long estimateRawMs() {
        if (hasSample) {
            return Math.round(echoMs);
        }
        return pingMs() + 50L;
    }

    public static long estimateMs(int min, int max) {
        long value = estimateRawMs();
        return Math.max(min, Math.min(max, value));
    }

    public static long placementWindowMs() {
        if (JsonConfig.config == null || JsonConfig.config.placement == null
                || !Boolean.TRUE.equals(JsonConfig.config.placement.dynamic)) {
            Integer fixed = JsonConfig.config == null || JsonConfig.config.placement == null
                    ? null : JsonConfig.config.placement.fixedWindowMs;
            return fixed == null ? 1000L : fixed;
        }
        Integer floor = JsonConfig.config.placement.floorMs;
        Integer cap = JsonConfig.config.placement.capMs;
        return estimateMs(floor == null ? 50 : floor, cap == null ? 300 : cap);
    }

    public static synchronized void reset() {
        hasSample = false;
        echoMs = 50.0;
    }
}
