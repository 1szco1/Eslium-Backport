package fr.solmey.clienthings.mixin.throwables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EyeOfEnderItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (JsonConfig.config.throwables.enabled && JsonConfig.shouldWork(JsonConfig.config.throwables.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = user.getStackInHand(hand);
                BlockPos pos = new BlockPos(user.getX(), user.getY(), user.getZ());
                EyeOfEnderEntity eye = new EyeOfEnderEntity(world, user.getX(), user.getY() + user.getStandingEyeHeight() - 0.25, user.getZ());
                eye.initTargetPos(pos);

                int fakeId = Entities.getNextFakeId();
                eye.setEntityId(fakeId);
                ((ClientWorld) world).addEntity(fakeId, eye);
                Entities.set(System.currentTimeMillis(), eye, eye, Entities.FAKE);
            }
        }
    }
}
