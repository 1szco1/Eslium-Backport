package net.solmey.eslium.mixin.tools;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.solmey.eslium.config.ConfigManager;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    @Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
    private void eslium$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().tools.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().tools.servers)) {
            if (cir.getReturnValue() == InteractionResult.SUCCESS && context.getLevel() instanceof ClientLevel clientLevel) {
                clientLevel.addParticle(ParticleTypes.HAPPY_VILLAGER,
                    context.getClickedPos().getX() + 0.5D,
                    context.getClickedPos().getY() + 1.0D,
                    context.getClickedPos().getZ() + 0.5D,
                    0.0D, 0.0D, 0.0D);
            }
        }
    }
}
