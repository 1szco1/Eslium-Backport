package fr.solmey.clienthings.mixin.cooldowns;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onCooldownUpdate", at = @At("HEAD"), cancellable = true)
    private void onCooldownUpdate(CooldownUpdateS2CPacket packet, CallbackInfo ci) {
        if (JsonConfig.config.cooldowns.enabled && JsonConfig.shouldWork(JsonConfig.config.cooldowns.servers)) {
            // Predict cooldowns client-side to reduce ping impact
            // In 1.16.5, cooldowns are handled via packets
        }
    }
}
