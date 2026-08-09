package fr.solmey.clienthings.mixin.consumables;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.item.FoodComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodComponent.class)
public class FoodComponentMixin {
    @Inject(method = "isSnack", at = @At("HEAD"))
    private void isSnack(CallbackInfoReturnable<Boolean> cir) {
        if (JsonConfig.config.consumables.enabled) {
            // Modify snack behavior for prediction
        }
    }
}
