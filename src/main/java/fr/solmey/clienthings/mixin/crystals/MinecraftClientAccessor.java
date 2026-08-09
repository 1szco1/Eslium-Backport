package fr.solmey.clienthings.mixin.crystals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor("interactionManager")
    net.minecraft.client.network.ClientPlayerInteractionManager getInteractionManager();
}
