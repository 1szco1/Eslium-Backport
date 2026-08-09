package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class Swap {
    public static void swap(int slot) {
        if (!JsonConfig.config.swap.enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        PlayerInventory inventory = player.inventory;
        if (slot >= 0 && slot < inventory.main.size()) {
            client.interactionManager.clickSlot(
                player.playerScreenHandler.syncId,
                slot,
                player.inventory.selectedSlot,
                SlotActionType.SWAP,
                player
            );
        }
    }
}
