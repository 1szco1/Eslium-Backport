package net.solmey.eslium.mixin.crystals;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.AABB;
import net.solmey.eslium.util.CrystalHandler;
import net.solmey.eslium.util.Entities;

@Mixin(Entity.class)
public abstract class EntityMixin {
    private static final AABB EMPTY_BOX = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    private void eslium$getBoundingBox(CallbackInfoReturnable<AABB> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof EndCrystal && entity.level() != null && entity.level().isClientSide()) {
            if (entity.isRemoved() || Entities.has(entity, Entities.TO_DESTROY) || CrystalHandler.isHidden(entity)) {
                cir.setReturnValue(EMPTY_BOX);
            }
        }
    }
}
