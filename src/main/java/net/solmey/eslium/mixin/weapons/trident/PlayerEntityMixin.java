package net.solmey.eslium.mixin.weapons.trident;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.solmey.eslium.config.ConfigManager;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    private void eslium$attack(Entity target, CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().weapons.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().weapons.servers)) {
            // Trident melee attack is already client-predicted; no extra work needed.
        }
    }
}
