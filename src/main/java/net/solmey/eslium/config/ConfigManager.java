package net.solmey.eslium.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.solmey.eslium.Eslium;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private static Config config;

    public static void initializeConfig() {
        loadConfig();
    }

    public static Config getConfig() {
        return config;
    }

    private static void loadConfig() {
        Path file = Eslium.getConfigDir().toPath().resolve("eslium.json");

        try {
            Files.createDirectories(file.getParent());

            if (Files.exists(file)) {
                try (var reader = Files.newBufferedReader(file)) {
                    config = GSON.fromJson(reader, Config.class);
                }
            }
            else {
                config = new Config();
            }
        } catch (Exception e) {
            e.printStackTrace();
            config = new Config();
        }

        guard();
        saveConfig(config);
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
        if (config.placement == null) config.placement = new Placement();

        if (config.crystals.servers == null) config.crystals.servers = new Servers();
        if (config.crystals.autoDestroy == null) config.crystals.autoDestroy = new AutoDestroy();
        if (config.minecart.servers == null) config.minecart.servers = new Servers();
        if (config.anchors.servers == null) config.anchors.servers = new Servers();
        if (config.placement.servers == null) config.placement.servers = new Servers();
        if (config.consumables.servers == null) config.consumables.servers = new Servers();
        if (config.cooldowns.servers == null) config.cooldowns.servers = new Servers();
        if (config.elytras.servers == null) config.elytras.servers = new Servers();
        if (config.firework.servers == null) config.firework.servers = new Servers();
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
    }

    public static void saveConfig(Config config) {
        Path file = Eslium.getConfigDir().toPath().resolve("eslium.json");

        try (var writer = Files.newBufferedWriter(file)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldWork(Servers servers) {
        if (config == null || !config.enabled) return false;
        if (servers == null) return true;
        return servers.any();
    }
}
