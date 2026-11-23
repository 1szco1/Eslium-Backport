package fr.solmey.clienthings.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonConfig {
    public static Config config;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;

    // Resolved on first use: MinecraftClient may not exist yet when this class loads.
    private static File getConfigFile() {
        if (configFile == null) {
            configFile = FabricLoader.getInstance().getConfigDir().resolve("eslium.json").toFile();
        }
        return configFile;
    }

    public static void loadConfig() {
        File file = getConfigFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                config = GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                e.printStackTrace();
                config = new Config();
            }
        } else {
            config = new Config();
            saveConfig();
        }
        guard();
    }

    private static void guard() {
        if (config == null) config = new Config();
        if (config.consumables == null) config.consumables = new Consumables();
        if (config.cooldowns == null) config.cooldowns = new Cooldowns();
        if (config.crystals == null) config.crystals = new Crystals();
        if (config.elytras == null) config.elytras = new Elytras();
        if (config.firework == null) config.firework = new Firework();
        if (config.minecart == null) config.minecart = new Minecart();
        if (config.pose == null) config.pose = new Pose();
        if (config.swap == null) config.swap = new Swap();
        if (config.weapons == null) config.weapons = new Weapons();
        if (config.windcharge == null) config.windcharge = new Windcharge();
        if (config.throwables == null) config.throwables = new Throwables();
        if (config.placeables == null) config.placeables = new Placeables();
        if (config.interactables == null) config.interactables = new Interactables();
        if (config.tools == null) config.tools = new Tools();
        if (config.combat == null) config.combat = new Combat();
        if (config.redstone == null) config.redstone = new Redstone();
        if (config.anchors == null) config.anchors = new Anchors();
        if (config.crystals.autoDestroy == null) config.crystals.autoDestroy = new AutoDestroy();
        if (config.consumables.servers == null) config.consumables.servers = new Servers();
        if (config.cooldowns.servers == null) config.cooldowns.servers = new Servers();
        if (config.crystals.servers == null) config.crystals.servers = new Servers();
        if (config.elytras.servers == null) config.elytras.servers = new Servers();
        if (config.firework.servers == null) config.firework.servers = new Servers();
        if (config.minecart.servers == null) config.minecart.servers = new Servers();
        if (config.pose.servers == null) config.pose.servers = new Servers();
        if (config.swap.servers == null) config.swap.servers = new Servers();
        if (config.weapons.servers == null) config.weapons.servers = new Servers();
        if (config.windcharge.servers == null) config.windcharge.servers = new Servers();
        if (config.throwables.servers == null) config.throwables.servers = new Servers();
        if (config.placeables.servers == null) config.placeables.servers = new Servers();
        if (config.interactables.servers == null) config.interactables.servers = new Servers();
        if (config.tools.servers == null) config.tools.servers = new Servers();
        if (config.combat.servers == null) config.combat.servers = new Servers();
        if (config.redstone.servers == null) config.redstone.servers = new Servers();
        if (config.anchors.servers == null) config.anchors.servers = new Servers();
        if (config.enabled == null) config.enabled = true;
        if (config.optout == null) config.optout = false;
        if (config.debug == null) config.debug = false;
        if (config.experimental == null) config.experimental = false;
        if (config.latencyMargin == null) config.latencyMargin = 25;
        if (config.safeMode == null) config.safeMode = false;
        if (config.simulatedDesync == null) config.simulatedDesync = 100;
    }

    public static void saveConfig() {
        try {
            File file = getConfigFile();
            File parent = file.getParentFile();
            if (parent != null) parent.mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldWork(Servers servers) {
        if (config == null || !Boolean.TRUE.equals(config.enabled)) return false;
        if (servers == null) return true;
        return Boolean.TRUE.equals(servers.CUSTOM) || Boolean.TRUE.equals(servers.MODDED)
                || Boolean.TRUE.equals(servers.PLUGIN) || Boolean.TRUE.equals(servers.VANILLA);
    }
}
