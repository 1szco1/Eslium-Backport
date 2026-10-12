package net.solmey.eslium.mixin.consumables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;

@Mixin(Item.class)
public class EnchantedGoldenAppleItemMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void eslium$finishUsingItem(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().consumables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().consumables.servers)) {
            if (world.isClientSide() && stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
                user.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3));
                user.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
                user.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0));
                user.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 6000, 0));
            }
        }
    }
}
