package fr.solmey.clienthings.mixin.placeables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (JsonConfig.config.placeables.enabled && JsonConfig.shouldWork(JsonConfig.config.placeables.servers)) {
            if (world instanceof ClientWorld) {
                ItemStack stack = user.getStackInHand(hand);
                EntityType<?> type = ((SpawnEggItem)(Object)this).getEntityType(stack.getTag());
                if (type != null) {
                    BlockHitResult hit = (BlockHitResult) user.raycast(5.0, 0.0F, false);
                    BlockPos pos = hit.getBlockPos().offset(hit.getSide());
                    Entity entity = type.create(world);
                    if (entity != null) {
                        entity.updatePosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        entity.yaw = user.yaw;

                        int fakeId = Entities.getNextFakeId();
                        entity.setEntityId(fakeId);
                        ((ClientWorld) world).addEntity(fakeId, entity);
                        Entities.set(System.currentTimeMillis(), entity, entity, Entities.FAKE);
                    }
                }
            }
        }
    }
}
