package net.solmey.eslium.mixin.minecarts;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.solmey.eslium.util.Entities;

@Mixin(AbstractMinecart.class)
public class MinecartEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void eslium$tick(CallbackInfo ci) {
        AbstractMinecart minecart = (AbstractMinecart) (Object) this;
        if (Entities.has(minecart, Entities.NOT_SYNCHRONISE)) {
            // Prevent position sync for predicted minecarts.
        }
    }
}
