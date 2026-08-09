package fr.solmey.clienthings.mixin.weapons.trident;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @Inject(method = "use", at = @At("TAIL"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        if (JsonConfig.config.weapons.enabled && JsonConfig.shouldWork(JsonConfig.config.weapons.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = user.getStackInHand(hand);
                TridentEntity trident = new TridentEntity(world, user, stack);
                trident.setProperties(user, user.pitch, user.yaw, 0.0F, 2.5F, 1.0F);

                ((ClientWorld)world).addEntity(trident.getEntityId(), trident);
                Entities.set(System.currentTimeMillis(), trident, trident, Entities.FAKE);
            }
        }
    }
}
