package fr.solmey.clienthings.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class Sounds {
    public static void play(SoundEvent sound, float volume, float pitch) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        client.world.playSound(player.getX(), player.getY(), player.getZ(), sound, SoundCategory.PLAYERS, volume, pitch, false);
    }
}
