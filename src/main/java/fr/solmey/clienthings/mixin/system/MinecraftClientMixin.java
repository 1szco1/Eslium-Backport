package fr.solmey.clienthings.mixin.system;

import fr.solmey.clienthings.util.BlockPrediction;
import fr.solmey.clienthings.util.CrystalHandler;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        Entities.clear();
        CrystalHandler.tick();
        BlockPrediction.tick(MinecraftClient.getInstance().world);
    }
}
