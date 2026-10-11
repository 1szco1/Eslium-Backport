package fr.solmey.clienthings.mixin.system;

import fr.solmey.clienthings.util.AnchorBuffer;
import fr.solmey.clienthings.util.BlockPrediction;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
    private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || !client.isOnThread()) {
            return;
        }
        if (Entities.needToCancel(packet)) {
            ci.cancel();
        }
    }

    @Inject(method = "onBlockUpdate", at = @At("HEAD"), cancellable = true)
    private void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci) {
        if (packet != null && packet.getPos() != null) {
            AnchorBuffer.removeInteracted(packet.getPos());
            if (BlockPrediction.absorb(packet.getPos(), packet.getState())) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "onExplosion", at = @At("HEAD"))
    private void onExplosion(ExplosionS2CPacket packet, CallbackInfo ci) {
        if (packet != null) {
            AnchorBuffer.handleExplosion(packet);
        }
    }
}
