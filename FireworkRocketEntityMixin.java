package fr.solmey.clienthings.mixin.firework;

import fr.solmey.clienthings.util.Entities;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        FireworkRocketEntity firework = (FireworkRocketEntity) (Object) this;
        if (Entities.has(firework, Entities.NOT_SYNCHRONISE)) {
            // Handle predicted firework
        }
    }
}
