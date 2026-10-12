package net.solmey.eslium.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class Sounds {
    public static void play(SoundEvent sound, float volume, float pitch) {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return;
        LocalPlayer player = client.player;
        if (player == null || client.level == null) return;
        client.level.playSound(player, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, volume, pitch);
    }
}
