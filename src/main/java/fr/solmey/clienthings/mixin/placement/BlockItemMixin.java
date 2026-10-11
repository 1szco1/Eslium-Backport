package fr.solmey.clienthings.mixin.placement;

import fr.solmey.clienthings.util.BlockPrediction;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"))
    private void clienthings_recordPlacement(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (context == null) {
            return;
        }
        World world = context.getWorld();
        if (world == null || !world.isClient) {
            return;
        }
        BlockPos pos = context.getBlockPos();
        if (pos == null) {
            return;
        }
        BlockPrediction.record(world.getBlockState(pos), pos);
    }
}
