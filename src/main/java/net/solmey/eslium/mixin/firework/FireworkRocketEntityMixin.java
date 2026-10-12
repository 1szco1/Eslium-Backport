package net.solmey.eslium.mixin.firework;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.solmey.eslium.util.Entities;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void eslium$tick(CallbackInfo ci) {
        FireworkRocketEntity firework = (FireworkRocketEntity) (Object) this;
        if (Entities.has(firework, Entities.NOT_SYNCHRONISE)) {
            // Predicted firework handling is tracked through Entities.
        }
    }
}
