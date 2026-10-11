package fr.solmey.clienthings;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.config.gui.ConfigGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class ClienThings implements ClientModInitializer {
    public static final String MOD_ID = "clienthings";
    public static KeyBinding openConfigKey;

    @Override
    public void onInitializeClient() {
        JsonConfig.loadConfig();

        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.clienthings.open_config", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "category.clienthings"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKey.wasPressed()) {
                client.openScreen(ConfigGui.create(client.currentScreen));
            }
        });

        if (JsonConfig.config.debug && JsonConfig.config.enabled) {
            System.out.println("[Eslium 1.16.5] Debug mode enabled");
        }
    }
}
