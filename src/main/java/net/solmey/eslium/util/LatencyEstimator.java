package net.solmey.eslium.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.config.Placement;

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
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.player == null || client.getConnection() == null) {
            return 0;
        }
        PlayerInfo entry = client.getConnection().getPlayerInfo(client.player.getUUID());
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
        Placement placement = ConfigManager.getConfig() == null ? null : ConfigManager.getConfig().placement;
        if (placement == null || !placement.dynamic) {
            return placement == null ? 1000L : placement.fixedWindowMs;
        }
        return estimateMs(placement.floorMs, placement.capMs);
    }

    public static synchronized void reset() {
        hasSample = false;
        echoMs = 50.0;
    }
}
