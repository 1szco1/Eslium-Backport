package net.solmey.eslium;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.solmey.eslium.config.ConfigManager;

public class Eslium {

    public static final Logger LOGGER = LogManager.getLogger("eslium");

    public static File configDir;

    public static void commonInit() {
        ensureDirectoriesReady();
        ConfigManager.initializeConfig();
    }

    private static void ensureDirectoriesReady() {
        if (configDir == null) {
            configDir = FabricLoader.getInstance().getConfigDir().toFile();
        }

        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    public static File getConfigDir() {
        ensureDirectoriesReady();
        return configDir;
    }

    public static boolean shouldWork() {
        Minecraft mc = Minecraft.getInstance();
        if (
            mc.level == null ||
            mc.player == null ||
            //mc.player.getInventory() == null ||
            mc.isLocalServer() ||
            mc.getConnection() == null
        ) {
            return false;
        }
        // The connection keeps its own level reference; on disconnect/dimension change
        // it can be cleared while mc.level is still set, and applying packets then NPEs.
        if (mc.getConnection().getLevel() != mc.level) return false;

        return ConfigManager.getConfig() != null
                && Boolean.TRUE.equals(ConfigManager.getConfig().enabled);
    }
}
