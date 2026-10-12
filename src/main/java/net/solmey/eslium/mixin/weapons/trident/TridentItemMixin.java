package net.solmey.eslium.mixin.weapons.trident;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @Inject(method = "releaseUsing", at = @At("TAIL"), cancellable = true)
    private void eslium$releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseDuration, CallbackInfoReturnable<Boolean> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (!ConfigManager.getConfig().weapons.enabled || !ConfigManager.shouldWork(ConfigManager.getConfig().weapons.servers)) return;
        if (!cir.getReturnValueZ()) return;
        if (world instanceof ClientLevel clientLevel && user instanceof net.minecraft.world.entity.player.Player) {
            ThrownTrident trident = new ThrownTrident(clientLevel, user, stack);
            trident.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 2.5F, 1.0F);
            int fakeId = Entities.getNextFakeId();
            trident.setId(fakeId);
            clientLevel.addEntity(trident);
            Entities.set(System.currentTimeMillis(), trident, trident, Entities.FAKE);
        }
    }
}
