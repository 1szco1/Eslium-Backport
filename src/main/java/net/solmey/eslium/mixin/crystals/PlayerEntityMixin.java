package net.solmey.eslium.mixin.crystals;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Player;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Inject(method = "resetAttackStrengthTicker", at = @At("HEAD"), cancellable = true)
    private void eslium$resetAttackStrengthTicker(CallbackInfo info) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().crystals.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().crystals.servers)) {
            Player player = (Player) (Object) this;
            Entities.attackCooldownProgress = player.getAttackStrengthScale(0.5F);
        }
    }
}
