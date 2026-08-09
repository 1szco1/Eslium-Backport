package fr.solmey.clienthings.mixin.interactables;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.interactables.enabled && JsonConfig.shouldWork(JsonConfig.config.interactables.servers)) {
            if (world.getDimension().isBedWorking()) {
                // Normal bed behavior - client already predicts sleep
            } else {
                // Predict explosion in Nether/End
                if (world.isClient) {
                    world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5.0F, Explosion.DestructionType.DESTROY);
                }
            }
        }
    }
}
