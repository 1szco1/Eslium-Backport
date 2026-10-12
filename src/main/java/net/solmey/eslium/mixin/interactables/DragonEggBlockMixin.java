package net.solmey.eslium.mixin.interactables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.solmey.eslium.config.ConfigManager;

@Mixin(DragonEggBlock.class)
public class DragonEggBlockMixin {
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void eslium$useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().interactables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().interactables.servers)) {
            // Dragon egg teleport is random and server-authoritative.
        }
    }
}
