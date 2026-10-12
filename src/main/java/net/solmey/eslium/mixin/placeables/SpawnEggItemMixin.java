package net.solmey.eslium.mixin.placeables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void eslium$use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().placeables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().placeables.servers)) {
            if (world instanceof ClientLevel clientLevel) {
                ItemStack stack = user.getItemInHand(hand);
                SpawnEggItem item = (SpawnEggItem) (Object) this;
                EntityType<?> type = item.getType(stack);
                if (type != null) {
                    HitResult hit = user.pick(5.0D, 0.0F, false);
                    if (hit instanceof BlockHitResult blockHit && hit.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());
                        Entity entity = type.create(clientLevel, EntitySpawnReason.SPAWN_ITEM_USE);
                        if (entity != null) {
                            entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                            entity.setYRot(user.getYRot());

                            int fakeId = Entities.getNextFakeId();
                            entity.setId(fakeId);
                            clientLevel.addEntity(entity);
                            Entities.set(System.currentTimeMillis(), entity, entity, Entities.FAKE);
                        }
                    }
                }
            }
        }
    }
}
