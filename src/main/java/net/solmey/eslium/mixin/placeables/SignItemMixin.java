package net.solmey.eslium.mixin.placeables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.solmey.eslium.config.ConfigManager;

@Mixin(Item.class)
public class SignItemMixin {
    @Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
    private void eslium$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().placeables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().placeables.servers)) {
            if (cir.getReturnValue() == InteractionResult.SUCCESS && context.getLevel() instanceof ClientLevel) {
                // Sign placement is already predicted by the vanilla client.
            }
        }
    }
}
