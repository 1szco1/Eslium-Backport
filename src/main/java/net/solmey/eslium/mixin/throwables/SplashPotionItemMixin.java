package net.solmey.eslium.mixin.throwables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(SplashPotionItem.class)
public class SplashPotionItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void eslium$use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().throwables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().throwables.servers)) {
            if (world instanceof ClientLevel clientLevel) {
                ItemStack stack = user.getItemInHand(hand);
                ThrownSplashPotion potion = new ThrownSplashPotion(clientLevel, user, stack);
                potion.shootFromRotation(user, user.getXRot(), user.getYRot(), -20.0F, 0.5F, 1.0F);

                int fakeId = Entities.getNextFakeId();
                potion.setId(fakeId);
                clientLevel.addEntity(potion);
                Entities.set(System.currentTimeMillis(), potion, potion, Entities.FAKE);
            }
        }
    }
}
