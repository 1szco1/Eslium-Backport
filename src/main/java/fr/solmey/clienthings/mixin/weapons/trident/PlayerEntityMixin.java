package fr.solmey.clienthings.mixin.weapons.trident;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    private void attack(net.minecraft.entity.Entity target, CallbackInfo ci) {
        if (JsonConfig.config.weapons.enabled && JsonConfig.shouldWork(JsonConfig.config.weapons.servers)) {
            // Predict trident attack
        }
    }
}
