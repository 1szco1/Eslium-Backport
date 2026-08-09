package fr.solmey.clienthings.mixin.throwables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EggItem.class)
public class EggItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (JsonConfig.config.throwables.enabled && JsonConfig.shouldWork(JsonConfig.config.throwables.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = user.getStackInHand(hand);
                EggEntity egg = new EggEntity(world, user);
                egg.setItem(stack);
                egg.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);

                int fakeId = Entities.getNextFakeId();
                egg.setEntityId(fakeId);
                ((ClientWorld) world).addEntity(fakeId, egg);
                Entities.set(System.currentTimeMillis(), egg, egg, Entities.FAKE);
            }
        }
    }
}
