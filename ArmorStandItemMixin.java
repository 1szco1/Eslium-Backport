package fr.solmey.clienthings.mixin.placeables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.placeables.enabled && JsonConfig.shouldWork(JsonConfig.config.placeables.servers)) {
            if (cir.getReturnValue() == ActionResult.SUCCESS && context.getWorld() instanceof ClientWorld) {
                ArmorStandEntity armorStand = new ArmorStandEntity(context.getWorld(), context.getHitPos().x, context.getHitPos().y, context.getHitPos().z);
                armorStand.yaw = context.getPlayerYaw();

                int fakeId = Entities.getNextFakeId();
                armorStand.setEntityId(fakeId);
                ((ClientWorld) context.getWorld()).addEntity(fakeId, armorStand);
                Entities.set(System.currentTimeMillis(), armorStand, armorStand, Entities.FAKE);
            }
        }
    }
}
