package net.solmey.eslium.mixin.combat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void eslium$use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().combat.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().combat.servers)) {
            // Bow charging is handled on release; prediction happens there.
        }
    }
}
