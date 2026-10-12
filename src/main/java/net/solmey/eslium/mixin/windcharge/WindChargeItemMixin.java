package net.solmey.eslium.mixin.windcharge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.item.Item;

@Mixin(Item.class)
public class WindChargeItemMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void eslium$init(CallbackInfo ci) {
        // Forward-compatibility hook; wind charge handling is a no-op here.
    }
}
