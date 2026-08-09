package fr.solmey.clienthings.mixin.firework;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkItem.class)
public class FireworkRocketItemMixin {
    @Inject(method = "use", at = @At("TAIL"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        if (JsonConfig.config.firework.enabled && JsonConfig.shouldWork(JsonConfig.config.firework.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = user.getStackInHand(hand);
                FireworkRocketEntity firework = new FireworkRocketEntity(world, stack, user);
                firework.updatePosition(user.getX(), user.getY() + user.getStandingEyeHeight() - 0.15, user.getZ());

                int fakeId = Entities.getNextFakeId();
                firework.setEntityId(fakeId);
                ((ClientWorld) world).addEntity(fakeId, firework);
                Entities.set(System.currentTimeMillis(), firework, firework, Entities.FAKE);
            }
        }
    }
}
