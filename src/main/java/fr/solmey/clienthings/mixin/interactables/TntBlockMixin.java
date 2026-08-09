package fr.solmey.clienthings.mixin.interactables;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.TntBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TntBlock.class)
public class TntBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.interactables.enabled && JsonConfig.shouldWork(JsonConfig.config.interactables.servers)) {
            if (world.isClient && world instanceof ClientWorld) {
                // Predict TNT ignition
                TntEntity tnt = new TntEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player);
                ((ClientWorld) world).addEntity(tnt.getEntityId(), tnt);
            }
        }
    }
}
