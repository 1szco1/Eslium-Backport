package net.solmey.eslium.mixin.tools;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;

@Mixin(BucketItem.class)
public class BucketItemMixin {
    @Inject(method = "use", at = @At("TAIL"), cancellable = true)
    private void eslium$use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().tools.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().tools.servers)) {
            if (world instanceof ClientLevel) {
                // Bucket use is already predicted reasonably well by the client.
            }
        }
    }
}
