package net.solmey.eslium.mixin.elytras;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.solmey.eslium.config.ConfigManager;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void eslium$updateFallFlying(CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().elytras.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().elytras.servers)) {
            LivingEntity entity = (LivingEntity) (Object) this;
            ItemStack chestStack = entity.getItemBySlot(EquipmentSlot.CHEST);
            if (chestStack.is(Items.ELYTRA)) {
                // Elytra flight start is already client-predicted; hook kept for prediction tuning.
            }
        }
    }
}
