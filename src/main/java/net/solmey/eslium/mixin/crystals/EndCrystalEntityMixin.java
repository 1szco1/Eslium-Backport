package net.solmey.eslium.mixin.crystals;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.solmey.eslium.util.CrystalHandler;

@Mixin(EndCrystal.class)
public abstract class EndCrystalEntityMixin {
    @Inject(method = "hurtClient", at = @At("HEAD"), cancellable = true)
    private void eslium$hurtClient(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (CrystalHandler.tryHit((EndCrystal) (Object) this, source, 1.0F)) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }
}
