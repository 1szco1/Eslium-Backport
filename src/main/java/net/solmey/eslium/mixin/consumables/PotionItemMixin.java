package net.solmey.eslium.mixin.consumables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void eslium$finishUsingItem(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().consumables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().consumables.servers)) {
            if (world.isClientSide()) {
                PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
                if (contents != null) {
                    for (MobEffectInstance effect : contents.getAllEffects()) {
                        user.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier()));
                    }
                }
            }
        }
    }
}
