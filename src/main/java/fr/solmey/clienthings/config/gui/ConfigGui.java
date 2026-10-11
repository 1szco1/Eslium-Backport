package fr.solmey.clienthings.config.gui;

import fr.solmey.clienthings.config.Config;
import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.config.Servers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigGui {
    private ConfigGui() {
    }

    public static Screen create(Screen parent) {
        if (JsonConfig.config == null) {
            JsonConfig.loadConfig();
        }
        Config config = JsonConfig.config;

        OptionsScreen main = new OptionsScreen(Text.of("Eslium Config"), parent);

        main.addNav("General", null, null, () -> general(main, config))
                .addNav("Consumables", () -> config.consumables.enabled, v -> config.consumables.enabled = v,
                        () -> standard(main, "Consumables", () -> config.consumables.enabled, v -> config.consumables.enabled = v,
                                config.consumables.servers,
                                () -> config.consumables.maxTime, v -> config.consumables.maxTime = v,
                                () -> config.consumables.maxDistance, v -> config.consumables.maxDistance = v))
                .addNav("Cooldowns", () -> config.cooldowns.enabled, v -> config.cooldowns.enabled = v,
                        () -> standard(main, "Cooldowns", () -> config.cooldowns.enabled, v -> config.cooldowns.enabled = v,
                                config.cooldowns.servers, null, null, null, null))
                .addNav("Crystals", () -> config.crystals.enabled, v -> config.crystals.enabled = v,
                        () -> crystals(main, config))
                .addNav("Elytras", () -> config.elytras.enabled, v -> config.elytras.enabled = v,
                        () -> standard(main, "Elytras", () -> config.elytras.enabled, v -> config.elytras.enabled = v,
                                config.elytras.servers,
                                () -> config.elytras.maxTime, v -> config.elytras.maxTime = v,
                                () -> config.elytras.maxDistance, v -> config.elytras.maxDistance = v))
                .addNav("Firework", () -> config.firework.enabled, v -> config.firework.enabled = v,
                        () -> standard(main, "Firework", () -> config.firework.enabled, v -> config.firework.enabled = v,
                                config.firework.servers,
                                () -> config.firework.maxTime, v -> config.firework.maxTime = v,
                                () -> config.firework.maxDistance, v -> config.firework.maxDistance = v))
                .addNav("Minecart", () -> config.minecart.enabled, v -> config.minecart.enabled = v,
                        () -> standard(main, "Minecart", () -> config.minecart.enabled, v -> config.minecart.enabled = v,
                                config.minecart.servers,
                                () -> config.minecart.maxTime, v -> config.minecart.maxTime = v, null, null))
                .addNav("Pose", () -> config.pose.enabled, v -> config.pose.enabled = v,
                        () -> standard(main, "Pose", () -> config.pose.enabled, v -> config.pose.enabled = v,
                                config.pose.servers, null, null, null, null))
                .addNav("Swap", () -> config.swap.enabled, v -> config.swap.enabled = v,
                        () -> standard(main, "Swap", () -> config.swap.enabled, v -> config.swap.enabled = v,
                                config.swap.servers, null, null, null, null))
                .addNav("Weapons", () -> config.weapons.enabled, v -> config.weapons.enabled = v,
                        () -> standard(main, "Weapons", () -> config.weapons.enabled, v -> config.weapons.enabled = v,
                                config.weapons.servers,
                                () -> config.weapons.maxTime, v -> config.weapons.maxTime = v,
                                () -> config.weapons.maxDistance, v -> config.weapons.maxDistance = v))
                .addNav("Windcharge", () -> config.windcharge.enabled, v -> config.windcharge.enabled = v,
                        () -> standard(main, "Windcharge", () -> config.windcharge.enabled, v -> config.windcharge.enabled = v,
                                config.windcharge.servers,
                                () -> config.windcharge.maxTime, v -> config.windcharge.maxTime = v,
                                () -> config.windcharge.maxDistance, v -> config.windcharge.maxDistance = v))
                .addNav("Throwables", () -> config.throwables.enabled, v -> config.throwables.enabled = v,
                        () -> standard(main, "Throwables", () -> config.throwables.enabled, v -> config.throwables.enabled = v,
                                config.throwables.servers,
                                () -> config.throwables.maxTime, v -> config.throwables.maxTime = v,
                                () -> config.throwables.maxDistance, v -> config.throwables.maxDistance = v))
                .addNav("Placeables", () -> config.placeables.enabled, v -> config.placeables.enabled = v,
                        () -> standard(main, "Placeables", () -> config.placeables.enabled, v -> config.placeables.enabled = v,
                                config.placeables.servers,
                                () -> config.placeables.maxTime, v -> config.placeables.maxTime = v,
                                () -> config.placeables.maxDistance, v -> config.placeables.maxDistance = v))
                .addNav("Interactables", () -> config.interactables.enabled, v -> config.interactables.enabled = v,
                        () -> standard(main, "Interactables", () -> config.interactables.enabled, v -> config.interactables.enabled = v,
                                config.interactables.servers,
                                () -> config.interactables.maxTime, v -> config.interactables.maxTime = v,
                                () -> config.interactables.maxDistance, v -> config.interactables.maxDistance = v))
                .addNav("Tools", () -> config.tools.enabled, v -> config.tools.enabled = v,
                        () -> standard(main, "Tools", () -> config.tools.enabled, v -> config.tools.enabled = v,
                                config.tools.servers,
                                () -> config.tools.maxTime, v -> config.tools.maxTime = v,
                                () -> config.tools.maxDistance, v -> config.tools.maxDistance = v))
                .addNav("Combat", () -> config.combat.enabled, v -> config.combat.enabled = v,
                        () -> standard(main, "Combat", () -> config.combat.enabled, v -> config.combat.enabled = v,
                                config.combat.servers,
                                () -> config.combat.maxTime, v -> config.combat.maxTime = v,
                                () -> config.combat.maxDistance, v -> config.combat.maxDistance = v))
                .addNav("Redstone", () -> config.redstone.enabled, v -> config.redstone.enabled = v,
                        () -> standard(main, "Redstone", () -> config.redstone.enabled, v -> config.redstone.enabled = v,
                                config.redstone.servers,
                                () -> config.redstone.maxTime, v -> config.redstone.maxTime = v,
                                () -> config.redstone.maxDistance, v -> config.redstone.maxDistance = v))
                .addNav("Anchors", () -> config.anchors.enabled, v -> config.anchors.enabled = v,
                        () -> anchors(main, config))
                .addNav("Placement", () -> config.placement.enabled, v -> config.placement.enabled = v,
                        () -> placement(main, config));

        return main;
    }

    private static OptionsScreen general(OptionsScreen parent, Config config) {
        OptionsScreen screen = new OptionsScreen(Text.of("General"), parent);
        screen.addToggle("Mod enabled", () -> config.enabled, v -> config.enabled = v);
        screen.addToggle("Opt out", () -> config.optout, v -> config.optout = v);
        screen.addToggle("Debug", () -> config.debug, v -> config.debug = v);
        screen.addToggle("Experimental", () -> config.experimental, v -> config.experimental = v);
        screen.addToggle("Safe mode", () -> config.safeMode, v -> config.safeMode = v);
        screen.addInt("Latency margin (ms)", () -> config.latencyMargin, v -> config.latencyMargin = v, 0, 500);
        screen.addAction("Reset latency estimate", () -> fr.solmey.clienthings.util.LatencyEstimator.reset());
        screen.addAction("Reload config", JsonConfig::loadConfig);
        screen.addAction("Save config", JsonConfig::saveConfig);
        return screen;
    }

    private static OptionsScreen crystals(OptionsScreen parent, Config config) {
        OptionsScreen screen = new OptionsScreen(Text.of("Crystals"), parent);
        screen.addToggle("Enabled", () -> config.crystals.enabled, v -> config.crystals.enabled = v);
        addServers(screen, config.crystals.servers);
        screen.addInt("Max time (ms)", () -> config.crystals.maxTime, v -> config.crystals.maxTime = v, 0, 20000);
        screen.addToggle("Dynamic delay", () -> config.crystals.dynamicDelay, v -> config.crystals.dynamicDelay = v);
        screen.addInt("Delay (ms)", () -> config.crystals.delay, v -> config.crystals.delay = v, 0, 200);
        screen.addInt("Resync (ticks)", () -> config.crystals.resync, v -> config.crystals.resync = v, 0, 50);
        screen.addAction("AutoDestroy", () -> screen.openScreen(autoDestroy(parent, config)));
        return screen;
    }

    private static OptionsScreen autoDestroy(OptionsScreen parent, Config config) {
        OptionsScreen screen = new OptionsScreen(Text.of("AutoDestroy"), parent);
        screen.addToggle("Enabled", () -> config.crystals.autoDestroy.enabled, v -> config.crystals.autoDestroy.enabled = v);
        screen.addInt("Max time (ms)", () -> config.crystals.autoDestroy.maxTime, v -> config.crystals.autoDestroy.maxTime = v, 0, 20000);
        return screen;
    }

    private static OptionsScreen anchors(OptionsScreen parent, Config config) {
        OptionsScreen screen = new OptionsScreen(Text.of("Anchors"), parent);
        screen.addToggle("Enabled", () -> config.anchors.enabled, v -> config.anchors.enabled = v);
        addServers(screen, config.anchors.servers);
        screen.addInt("Max time (ms)", () -> config.anchors.maxTime, v -> config.anchors.maxTime = v, 0, 20000);
        screen.addToggle("Glowstone sound", () -> config.anchors.glowstoneSound, v -> config.anchors.glowstoneSound = v);
        screen.addToggle("Client explosion", () -> config.anchors.clientExplosion, v -> config.anchors.clientExplosion = v);
        screen.addToggle("Re-interact", () -> config.anchors.reInteract, v -> config.anchors.reInteract = v);
        screen.addToggle("Ghost anchor", () -> config.anchors.ghostAnchor, v -> config.anchors.ghostAnchor = v);
        return screen;
    }

    private static OptionsScreen placement(OptionsScreen parent, Config config) {
        OptionsScreen screen = new OptionsScreen(Text.of("Placement"), parent);
        screen.addToggle("Enabled", () -> config.placement.enabled, v -> config.placement.enabled = v);
        addServers(screen, config.placement.servers);
        screen.addToggle("Dynamic window", () -> config.placement.dynamic, v -> config.placement.dynamic = v);
        screen.addInt("Fixed window (ms)", () -> config.placement.fixedWindowMs, v -> config.placement.fixedWindowMs = v, 0, 5000);
        screen.addInt("Floor (ms)", () -> config.placement.floorMs, v -> config.placement.floorMs = v, 0, 1000);
        screen.addInt("Cap (ms)", () -> config.placement.capMs, v -> config.placement.capMs = v, 0, 5000);
        return screen;
    }

    private static OptionsScreen standard(OptionsScreen parent, String title,
            Supplier<Boolean> enabledGet, Consumer<Boolean> enabledSet, Servers servers,
            Supplier<Integer> maxTimeGet, Consumer<Integer> maxTimeSet,
            Supplier<Integer> maxDistanceGet, Consumer<Integer> maxDistanceSet) {
        OptionsScreen screen = new OptionsScreen(Text.of(title), parent);
        screen.addToggle("Enabled", enabledGet, enabledSet);
        addServers(screen, servers);
        if (maxTimeGet != null) {
            screen.addInt("Max time (ms)", maxTimeGet, maxTimeSet, 0, 20000);
        }
        if (maxDistanceGet != null) {
            screen.addInt("Max distance", maxDistanceGet, maxDistanceSet, 0, 64);
        }
        return screen;
    }

    private static void addServers(OptionsScreen screen, Servers servers) {
        screen.addToggle("Server: Custom", () -> servers.CUSTOM, v -> servers.CUSTOM = v);
        screen.addToggle("Server: Modded", () -> servers.MODDED, v -> servers.MODDED = v);
        screen.addToggle("Server: Plugin", () -> servers.PLUGIN, v -> servers.PLUGIN = v);
        screen.addToggle("Server: Vanilla", () -> servers.VANILLA, v -> servers.VANILLA = v);
    }
}
