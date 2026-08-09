package fr.solmey.clienthings.mixin.throwables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SplashPotionItem.class)
public class SplashPotionItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (JsonConfig.config.throwables.enabled && JsonConfig.shouldWork(JsonConfig.config.throwables.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = user.getStackInHand(hand);
                PotionEntity potion = new PotionEntity(world, user);
                potion.setItem(stack);
                potion.setProperties(user, user.pitch, user.yaw, -20.0F, 0.5F, 1.0F);

                int fakeId = Entities.getNextFakeId();
                potion.setEntityId(fakeId);
                ((ClientWorld) world).addEntity(fakeId, potion);
                Entities.set(System.currentTimeMillis(), potion, potion, Entities.FAKE);
            }
        }
    }
}
