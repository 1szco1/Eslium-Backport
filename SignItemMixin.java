package fr.solmey.clienthings.mixin.placeables;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class SignItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.placeables.enabled && JsonConfig.shouldWork(JsonConfig.config.placeables.servers)) {
            if (cir.getReturnValue() == ActionResult.SUCCESS && context.getWorld() instanceof ClientWorld) {
                // Sign block is placed as a block - client already predicts this
                BlockState state = context.getWorld().getBlockState(context.getBlockPos().offset(context.getSide()));
                if (state.getBlock() instanceof AbstractSignBlock) {
                    // Sign placement is already client-predicted by Minecraft
                }
            }
        }
    }
}
