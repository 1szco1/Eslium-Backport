package net.solmey.eslium.mixin.placement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.solmey.eslium.util.BlockPrediction;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "place", at = @At("HEAD"))
    private void eslium$recordPlacement(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context == null) {
            return;
        }
        Level level = context.getLevel();
        if (level == null || !level.isClientSide()) {
            return;
        }
        BlockPos pos = context.getClickedPos();
        if (pos == null) {
            return;
        }
        BlockPrediction.record(level.getBlockState(pos), pos);
    }
}
