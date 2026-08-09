package fr.solmey.clienthings.mixin.tools;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.tools.enabled && JsonConfig.shouldWork(JsonConfig.config.tools.servers)) {
            if (cir.getReturnValue() == ActionResult.SUCCESS && context.getWorld() instanceof ClientWorld) {
                BlockState state = context.getWorld().getBlockState(context.getBlockPos());
                if (state.getBlock() instanceof Fertilizable) {
                    // Bone meal growth is random - we can show particles but not predict exact growth
                    ((ClientWorld) context.getWorld()).addParticle(
                        net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
                        context.getBlockPos().getX() + 0.5,
                        context.getBlockPos().getY() + 1.0,
                        context.getBlockPos().getZ() + 0.5,
                        0.0, 0.0, 0.0
                    );
                }
            }
        }
    }
}
