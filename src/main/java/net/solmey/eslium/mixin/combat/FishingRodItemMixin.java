package net.solmey.eslium.mixin.combat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void eslium$use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().combat.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().combat.servers)) {
            if (world instanceof ClientLevel clientLevel) {
                if (user.fishing == null) {
                    FishingHook bobber = new FishingHook(user, clientLevel, 0, 0);
                    int fakeId = Entities.getNextFakeId();
                    bobber.setId(fakeId);
                    clientLevel.addEntity(bobber);
                    Entities.set(System.currentTimeMillis(), bobber, bobber, Entities.FAKE);
                }
            }
        }
    }
}
