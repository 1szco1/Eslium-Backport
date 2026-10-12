package fr.solmey.clienthings.mixin.anticheat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.solmey.clienthings.ClienThings;
import fr.solmey.clienthings.rollback.InteractionManager;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void eslium$tickEntitiesHEAD(CallbackInfo ci) {
        if (!ClienThings.shouldWork()) return;

        InteractionManager.showEntities();
    }

    @Inject(method = "tickEntities", at = @At("TAIL"))
    private void eslium$tickEntitiesTAIL(CallbackInfo ci) {
        if (!ClienThings.shouldWork()) return;

        InteractionManager.hideEntities();
    }
}
