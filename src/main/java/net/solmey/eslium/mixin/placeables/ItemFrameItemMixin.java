package net.solmey.eslium.mixin.placeables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(Item.class)
public class ItemFrameItemMixin {
    @Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
    private void eslium$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().placeables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().placeables.servers)) {
            if (cir.getReturnValue() == InteractionResult.SUCCESS && context.getLevel() instanceof ClientLevel clientLevel) {
                ItemStack stack = context.getItemInHand();
                if (stack.is(Items.ITEM_FRAME) || stack.is(Items.GLOW_ITEM_FRAME)) {
                    Direction side = context.getClickedFace();
                    BlockPos pos = context.getClickedPos().relative(side);
                    ItemFrame frame = new ItemFrame(clientLevel, pos, side);

                    int fakeId = Entities.getNextFakeId();
                    frame.setId(fakeId);
                    clientLevel.addEntity(frame);
                    Entities.set(System.currentTimeMillis(), frame, frame, Entities.FAKE);
                }
            }
        }
    }
}
