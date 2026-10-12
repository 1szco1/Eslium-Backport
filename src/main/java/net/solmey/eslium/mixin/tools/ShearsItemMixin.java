package net.solmey.eslium.mixin.tools;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.solmey.eslium.config.ConfigManager;

@Mixin(PumpkinBlock.class)
public class ShearsItemMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void eslium$useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().tools.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().tools.servers)) {
            if (world instanceof ClientLevel clientLevel) {
                if (stack.is(Items.SHEARS)) {
                    clientLevel.setBlock(pos, Blocks.CARVED_PUMPKIN.defaultBlockState(), 3);
                }
            }
        }
    }
}
