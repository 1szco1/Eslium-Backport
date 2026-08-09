package fr.solmey.clienthings.mixin.throwables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderPearlItem.class)
public class EnderPearlItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (JsonConfig.config.throwables.enabled && JsonConfig.shouldWork(JsonConfig.config.throwables.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = user.getStackInHand(hand);
                EnderPearlEntity pearl = new EnderPearlEntity(world, user);
                pearl.setItem(stack);
                pearl.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);

                int fakeId = Entities.getNextFakeId();
                pearl.setEntityId(fakeId);
                ((ClientWorld) world).addEntity(fakeId, pearl);
                Entities.set(System.currentTimeMillis(), pearl, pearl, Entities.FAKE);
            }
        }
    }
}
