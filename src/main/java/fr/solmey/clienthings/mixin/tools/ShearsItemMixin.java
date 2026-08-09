package fr.solmey.clienthings.mixin.tools;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PumpkinBlock.class)
public class ShearsItemMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.tools.enabled && JsonConfig.shouldWork(JsonConfig.config.tools.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.getItem() == Items.SHEARS) {
                    // Predict pumpkin carving - in 1.16.5, carved pumpkin doesn't have FACING property
                    ((ClientWorld) world).setBlockState(pos, Blocks.CARVED_PUMPKIN.getDefaultState());
                }
            }
        }
    }
}
