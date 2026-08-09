package fr.solmey.clienthings.mixin.tools;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.tools.enabled && JsonConfig.shouldWork(JsonConfig.config.tools.servers)) {
            if (cir.getReturnValue() == ActionResult.SUCCESS && context.getWorld() instanceof ClientWorld) {
                BlockState state = context.getWorld().getBlockState(context.getBlockPos());
                if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) {
                    ((ClientWorld) context.getWorld()).setBlockState(context.getBlockPos(), Blocks.GRASS_PATH.getDefaultState());
                }
            }
        }
    }
}
