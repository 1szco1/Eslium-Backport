package fr.solmey.clienthings.mixin.tools;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.tools.enabled && JsonConfig.shouldWork(JsonConfig.config.tools.servers)) {
            if (cir.getReturnValue() == ActionResult.SUCCESS && context.getWorld() instanceof ClientWorld) {
                BlockPos pos = context.getBlockPos();
                Direction side = context.getSide();
                BlockPos firePos = pos.offset(side);
                BlockState state = context.getWorld().getBlockState(firePos);

                // Predict fire placement if air
                if (state.isAir()) {
                    ((ClientWorld) context.getWorld()).setBlockState(firePos, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }
}
