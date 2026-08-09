package fr.solmey.clienthings.mixin.windcharge;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Wind charges don't exist in 1.16.5 - stub for forward compatibility
@Mixin(Item.class)
public class WindChargeItemMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        // Stub - wind charges not available in 1.16.5
    }
}
