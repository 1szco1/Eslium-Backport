package fr.solmey.clienthings.mixin.consumables;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers)) {
            if (world.isClient) {
                // Chorus fruit teleport is random - we can't predict exact location
                // But we can predict that teleportation will happen
                // Show particles at current location
                for (int i = 0; i < 16; i++) {
                    world.addParticle(
                        net.minecraft.particle.ParticleTypes.PORTAL,
                        user.getX() + (world.random.nextDouble() - 0.5) * 2.0,
                        user.getY() + world.random.nextDouble() * 2.0,
                        user.getZ() + (world.random.nextDouble() - 0.5) * 2.0,
                        0.0, 0.0, 0.0
                    );
                }
            }
        }
    }
}
