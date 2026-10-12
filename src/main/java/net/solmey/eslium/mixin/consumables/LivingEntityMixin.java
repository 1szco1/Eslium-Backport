package net.solmey.eslium.mixin.consumables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.LivingEntity;
import net.solmey.eslium.config.ConfigManager;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "completeUsingItem", at = @At("HEAD"))
    private void eslium$completeUsingItem(CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().consumables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().consumables.servers)) {
            // Consumption is already tracked client-side; hook kept for prediction tuning.
        }
    }
}
