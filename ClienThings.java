package fr.solmey.clienthings;

import fr.solmey.clienthings.config.JsonConfig;
import net.fabricmc.api.ClientModInitializer;

public class ClienThings implements ClientModInitializer {
    public static final String MOD_ID = "clienthings";

    @Override
    public void onInitializeClient() {
        JsonConfig.loadConfig();
        if (JsonConfig.config.debug && JsonConfig.config.enabled) {
            System.out.println("[Eslium 1.16.5] Debug mode enabled");
        }
    }
}
