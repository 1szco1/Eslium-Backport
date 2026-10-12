package net.solmey.eslium.mixin.firework;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
    @Inject(method = "use", at = @At("TAIL"), cancellable = true)
    private void eslium$use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().firework.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().firework.servers)) {
            if (world instanceof ClientLevel clientLevel) {
                ItemStack stack = user.getItemInHand(hand);
                FireworkRocketEntity firework = new FireworkRocketEntity(clientLevel, stack, user);
                firework.setPos(user.getX(), user.getEyeY() - 0.15D, user.getZ());

                int fakeId = Entities.getNextFakeId();
                firework.setId(fakeId);
                clientLevel.addEntity(firework);
                Entities.set(System.currentTimeMillis(), firework, firework, Entities.FAKE);
            }
        }
    }
}
