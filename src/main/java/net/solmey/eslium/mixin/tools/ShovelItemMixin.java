package net.solmey.eslium.mixin.tools;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.solmey.eslium.config.ConfigManager;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {
    @Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
    private void eslium$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().tools.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().tools.servers)) {
            if (cir.getReturnValue() == InteractionResult.SUCCESS && context.getLevel() instanceof ClientLevel clientLevel) {
                BlockState state = context.getLevel().getBlockState(context.getClickedPos());
                if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) {
                    clientLevel.setBlock(context.getClickedPos(), Blocks.DIRT_PATH.defaultBlockState(), 3);
                }
            }
        }
    }
}
