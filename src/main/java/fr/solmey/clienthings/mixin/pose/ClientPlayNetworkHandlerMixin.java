package fr.solmey.clienthings.mixin.pose;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onEntityTrackerUpdate", at = @At("HEAD"), cancellable = true)
    private void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci) {
        if (JsonConfig.config.pose.enabled && JsonConfig.shouldWork(JsonConfig.config.pose.servers)) {
            // Predict entity pose updates
        }
    }
}
