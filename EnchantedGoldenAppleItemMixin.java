package fr.solmey.clienthings.mixin.consumables;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.EnchantedGoldenAppleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class EnchantedGoldenAppleItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers)) {
            if (world.isClient && stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                // Predict enchanted golden apple effects
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0));
            }
        }
    }
}
