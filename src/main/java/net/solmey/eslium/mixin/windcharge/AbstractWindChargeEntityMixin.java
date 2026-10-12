package net.solmey.eslium.mixin.windcharge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public class AbstractWindChargeEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void eslium$tick(CallbackInfo ci) {
        // Forward-compatibility hook; wind charge handling is a no-op here.
    }
}
