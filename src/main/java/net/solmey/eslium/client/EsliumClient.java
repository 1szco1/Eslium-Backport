package net.solmey.eslium.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.config.gui.ConfigGui;
import net.solmey.eslium.util.AnchorHandler;
import net.solmey.eslium.util.BlockPrediction;
import net.solmey.eslium.util.CrystalHandler;
import net.solmey.eslium.util.Entities;

public class EsliumClient implements ClientModInitializer {

    public static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(Identifier.fromNamespaceAndPath("eslium", "main"));

    public static KeyMapping openConfigKey;

    @Override
    public void onInitializeClient() {
        if (ConfigManager.getConfig() == null) {
            ConfigManager.initializeConfig();
        }

        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.eslium.open_config",
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Entities.clear();
            CrystalHandler.tick();
            BlockPrediction.tick(client.level);

            while (openConfigKey.consumeClick()) {
                client.setScreen(ConfigGui.create(client.screen));
            }
        });

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(AnchorHandler::renderGhostAnchors);
    }
}
