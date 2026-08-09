package fr.solmey.clienthings.mixin.elytras;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Elytras;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tickFallFlying", at = @At("HEAD"), cancellable = true)
    private void tickFallFlying(CallbackInfo ci) {
        if (JsonConfig.config.elytras.enabled && JsonConfig.shouldWork(JsonConfig.config.elytras.servers)) {
            LivingEntity entity = (LivingEntity) (Object) this;
            ItemStack chestStack = entity.getEquippedStack(EquipmentSlot.CHEST);
            if (chestStack.getItem() == Items.ELYTRA && ElytraItem.isUsable(chestStack)) {
                // Predict elytra flight start
            }
        }
    }
}
