package net.solmey.eslium.mixin.throwables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(EnderEyeItem.class)
public class EyeOfEnderItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void eslium$use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().throwables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().throwables.servers)) {
            if (world instanceof ClientLevel clientLevel) {
                ItemStack stack = user.getItemInHand(hand);
                EyeOfEnder eye = new EyeOfEnder(clientLevel, user.getX(), user.getY() + user.getEyeHeight() - 0.25, user.getZ());
                eye.setItem(stack);
                eye.signalTo(new Vec3(user.getX(), user.getY(), user.getZ()));

                int fakeId = Entities.getNextFakeId();
                eye.setId(fakeId);
                clientLevel.addEntity(eye);
                Entities.set(System.currentTimeMillis(), eye, eye, Entities.FAKE);
            }
        }
    }
}
