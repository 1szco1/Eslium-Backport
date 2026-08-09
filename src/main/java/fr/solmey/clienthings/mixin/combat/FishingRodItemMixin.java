package fr.solmey.clienthings.mixin.combat;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (JsonConfig.config.combat.enabled && JsonConfig.shouldWork(JsonConfig.config.combat.servers)) {
            if (world instanceof ClientWorld) {
                // Only predict the cast, not the retrieve
                if (user.fishHook == null) {
                    FishingBobberEntity bobber = new FishingBobberEntity(user, world, 0, 0);

                    int fakeId = Entities.getNextFakeId();
                    bobber.setEntityId(fakeId);
                    ((ClientWorld) world).addEntity(fakeId, bobber);
                    Entities.set(System.currentTimeMillis(), bobber, bobber, Entities.FAKE);
                }
            }
        }
    }
}
