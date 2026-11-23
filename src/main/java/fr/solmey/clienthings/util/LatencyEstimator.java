package fr.solmey.clienthings.util;

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

    public static synchronized void reset() {
        hasSample = false;
        echoMs = 50.0;
    }
}
