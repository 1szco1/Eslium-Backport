package net.solmey.eslium.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Actions {
    public static void attack() {
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.player == null) return;

        LocalPlayer player = client.player;
        if (client.hitResult != null && client.hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) client.hitResult;
            client.gameMode.attack(player, entityHit.getEntity());
        }
        player.swing(InteractionHand.MAIN_HAND);
    }
}
