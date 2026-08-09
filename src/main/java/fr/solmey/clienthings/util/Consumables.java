package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;

public class Consumables {
    public static void consume(ItemStack stack, Hand hand) {
        if (!JsonConfig.config.consumables.enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        // 1.16.5: Use ItemStack methods directly
        if (stack.getItem().isFood()) {
            player.getHungerManager().eat(stack.getItem(), stack);
        }
    }

    public static void playSound(SoundEvent sound, SoundCategory category, float volume, float pitch) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        client.world.playSound(player.getX(), player.getY(), player.getZ(), sound, category, volume, pitch, false);
    }
}
