package fr.solmey.clienthings.mixin.throwables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceBottleItem.class)
public class ExperienceBottleItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (JsonConfig.config.throwables.enabled && JsonConfig.shouldWork(JsonConfig.config.throwables.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = user.getStackInHand(hand);
                ExperienceBottleEntity bottle = new ExperienceBottleEntity(world, user);
                bottle.setItem(stack);
                bottle.setProperties(user, user.pitch, user.yaw, -20.0F, 0.7F, 1.0F);

                int fakeId = Entities.getNextFakeId();
                bottle.setEntityId(fakeId);
                ((ClientWorld) world).addEntity(fakeId, bottle);
                Entities.set(System.currentTimeMillis(), bottle, bottle, Entities.FAKE);
            }
        }
    }
}
