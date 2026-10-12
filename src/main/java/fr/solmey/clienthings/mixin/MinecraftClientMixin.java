package fr.solmey.clienthings.mixin;

import java.util.Iterator;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.solmey.clienthings.ClienThings;
import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.data.Data;
import fr.solmey.clienthings.predictions.UseItemOnPacket;
import fr.solmey.clienthings.rollback.InteractionManager;
import fr.solmey.clienthings.server.MixinMode;
import fr.solmey.clienthings.server.SimulatedInventory;
import fr.solmey.clienthings.server.SimulatedLevel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "tick", at = @At("HEAD")) // Start of the client tick
    private void eslium$tickHEAD(CallbackInfo callback) {
        if (!ClienThings.shouldWork()) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        MixinMode.lastTimestamp = System.currentTimeMillis();
        SimulatedInventory.saveServerInventory(player);

        // Calculate the timestamp of the next server tick.
        // 1.16.5 has no tick rate manager, so a fixed 20 TPS interval is used.
        long MSPTnano = 50_000_000L;

        int desync = 100;
        if (JsonConfig.config != null && JsonConfig.config.simulatedDesync != null) {
            desync = JsonConfig.config.simulatedDesync;
        }
        if (desync < 0) desync = 0;
        if (desync > 100) desync = 100;
        MSPTnano = MSPTnano * desync / 100;

        Data.timestampNanoNextServerTick = System.nanoTime() + MSPTnano;
    }

    @Inject(method = "tick", at = @At("TAIL")) // End of the client tick, start of the server tick
    private void eslium$tickTAIL(CallbackInfo callback) {
        if (!ClienThings.shouldWork()) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        ClientWorld clientLevel = (ClientWorld) player.world;

        MixinMode.mixinMode = true;
        InteractionManager.showEntities();

        SimulatedInventory.startServerTick(player);

        // Server handling packet tick or whatever I should call that
        synchronized (Data.sentPackets) {
            for (Packet<?> packet : Data.sentPackets) {
                UseItemOnPacket.onSentPacket(packet);
            }
            Data.sentPackets.clear();
        }

        // Server tick
        SimulatedInventory.endServerTick(player);
        SimulatedLevel.tick(clientLevel);

        InteractionManager.hideEntities();
        MixinMode.mixinMode = false;

        // If the real packet from the server is received before handling it, we need to rollback anyways
        Data.extractPackets(Data.predictedPackets);
        Data.extractedToBeValidedPackets.putAll(Data.predictedPackets);
    }

    @Inject(method = "render", at = @At("HEAD")) // Each frame
    private void eslium$render(boolean tick, CallbackInfo ci) {
        if (!ClienThings.shouldWork()) return;

        if (
            Data.timestampNanoNextServerTick != -1 &&
            System.nanoTime() >= Data.timestampNanoNextServerTick
        ) {
            Data.timestampNanoNextServerTick = -1;

            ClientPlayNetworkHandler connection = MinecraftClient.getInstance().getNetworkHandler();

            for (Map.Entry<Long, Packet<?>> entry : Data.predictedPackets.entries()) {
                Packet<?> packet = entry.getValue();

                if (Data.extractedToBeValidedPackets.containsValue(packet)) {
                    ((Packet<ClientPlayPacketListener>) packet).apply(connection);
                }
            }
            Data.predictedPackets.clear();

            InteractionManager.hideEntities();
        }

        long MSPT = 50L;
        Iterator<Map.Entry<Long, Packet<?>>> iterator =
                Data.extractedToBeValidedPackets.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Packet<?>> entry = iterator.next();

            long timestamp = entry.getKey();

            if (MixinMode.lastTimestamp > timestamp + MSPT * 2) {
                iterator.remove();
            }
        }
    }
}
