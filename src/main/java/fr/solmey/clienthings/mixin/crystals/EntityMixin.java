package fr.solmey.clienthings.mixin.crystals;

import fr.solmey.clienthings.util.CrystalHandler;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    private static final Box EMPTY_BOX = new Box(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

    // Collapse the bounding box of destroyed/removed/hidden crystals so the crosshair,
    // raycasts and the world renderer treat them as gone while the server catches up.
    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    private void onGetBoundingBox(CallbackInfoReturnable<Box> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof EndCrystalEntity && entity.world != null && entity.world.isClient()) {
            if (entity.removed || Entities.has(entity, Entities.TO_DESTROY) || CrystalHandler.isHidden(entity)) {
                cir.setReturnValue(EMPTY_BOX);
            }
        }
    }
}
