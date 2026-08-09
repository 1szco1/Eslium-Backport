package fr.solmey.clienthings.mixin.weapons.crossbow;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Inject(method = "shoot", at = @At("HEAD"))
    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, CallbackInfo ci) {
        if (JsonConfig.config.combat.enabled && JsonConfig.shouldWork(JsonConfig.config.combat.servers)) {
            if (world instanceof ClientWorld && shooter instanceof net.minecraft.entity.player.PlayerEntity) {
                net.minecraft.entity.player.PlayerEntity player = (net.minecraft.entity.player.PlayerEntity) shooter;

                ProjectileEntity proj;
                if (projectile.getItem() == net.minecraft.item.Items.FIREWORK_ROCKET) {
                    proj = new FireworkRocketEntity(world, projectile, player, player.getX(), player.getEyeY() - 0.15, player.getZ(), true);
                } else {
                    ArrowEntity arrow = new ArrowEntity(world, player);
                    arrow.setDamage(2.0);
                    proj = arrow;
                }

                // In 1.16.5, use setProperties instead of setVelocity
                proj.setProperties(player, player.pitch, player.yaw, 0.0F, speed, divergence);

                int fakeId = Entities.getNextFakeId();
                proj.setEntityId(fakeId);
                ((ClientWorld) world).addEntity(fakeId, proj);
                Entities.set(System.currentTimeMillis(), proj, proj, Entities.FAKE);
            }
        }
    }
}
