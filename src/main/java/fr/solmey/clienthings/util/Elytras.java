package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class Elytras {
    public static void startFlying() {
        if (!JsonConfig.config.elytras.enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chestStack.getItem() == Items.ELYTRA && ElytraItem.isUsable(chestStack)) {
            player.startFallFlying();
        }
    }

    public static void useFirework() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        // Check for firework in hand
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() == Items.FIREWORK_ROCKET) {
                client.interactionManager.interactItem(player, client.world, hand);
                return;
            }
        }
    }
}
