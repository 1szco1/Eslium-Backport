package fr.solmey.clienthings.mixin.minecarts;

import fr.solmey.clienthings.util.Entities;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public class MinecartEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        AbstractMinecartEntity minecart = (AbstractMinecartEntity) (Object) this;
        if (Entities.has(minecart, Entities.NOT_SYNCHRONISE)) {
            // Prevent position sync for predicted minecarts
        }
    }
}
