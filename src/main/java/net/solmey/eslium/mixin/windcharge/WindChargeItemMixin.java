package net.solmey.eslium.mixin.windcharge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(net.minecraft.world.item.WindChargeItem.class)
public class WindChargeItemMixin {
    @Inject(method = "use", at = @At("RETURN"))
    private void eslium$use(Level world, Player user, InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null || ConfigManager.getConfig().windcharge == null) return;
        if (!ConfigManager.getConfig().windcharge.enabled) return;
        if (!ConfigManager.shouldWork(ConfigManager.getConfig().windcharge.servers)) return;
        if (!(world instanceof ClientLevel clientLevel) || !(user instanceof LocalPlayer)) return;
        if (!cir.getReturnValue().consumesAction()) return;

        WindCharge charge = new WindCharge(user, clientLevel, user.getX(), user.getEyeY() - 0.1D, user.getZ());
        charge.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
        int fakeId = Entities.getNextFakeId();
        charge.setId(fakeId);
        clientLevel.addEntity(charge);
        Entities.set(System.currentTimeMillis(), charge, charge, Entities.FAKE);
    }
}
