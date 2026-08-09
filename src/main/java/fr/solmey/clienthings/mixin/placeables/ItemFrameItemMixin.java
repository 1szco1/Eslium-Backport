package fr.solmey.clienthings.mixin.placeables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// In 1.16.5, item frames are placed via the base Item class interaction
@Mixin(Item.class)
public class ItemFrameItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.placeables.enabled && JsonConfig.shouldWork(JsonConfig.config.placeables.servers)) {
            if (cir.getReturnValue() == ActionResult.SUCCESS && context.getWorld() instanceof ClientWorld) {
                ItemStack stack = context.getStack();
                // Check if this is an item frame (ITEM_FRAME item)
                if (stack.getItem().toString().contains("item_frame") || stack.getTranslationKey().contains("item_frame")) {
                    Direction side = context.getSide();
                    ItemFrameEntity frame = new ItemFrameEntity(context.getWorld(), context.getBlockPos().offset(side), side);

                    int fakeId = Entities.getNextFakeId();
                    frame.setEntityId(fakeId);
                    ((ClientWorld) context.getWorld()).addEntity(fakeId, frame);
                    Entities.set(System.currentTimeMillis(), frame, frame, Entities.FAKE);
                }
            }
        }
    }
}
