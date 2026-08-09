package fr.solmey.clienthings.mixin.consumables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Consumables;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "consumeItem", at = @At("HEAD"))
    private void onConsumeItem(CallbackInfo ci) {
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers)) {
            LivingEntity entity = (LivingEntity) (Object) this;
            // Track consumption for prediction
        }
    }
}
