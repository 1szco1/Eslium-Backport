package net.solmey.eslium.mixin.weapons.crossbow;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Inject(method = "performShooting", at = @At("HEAD"))
    private void eslium$performShooting(Level world, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, float speed, float divergence, LivingEntity target, CallbackInfo ci) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().combat.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().combat.servers)) {
            if (world instanceof ClientLevel clientLevel && shooter instanceof net.minecraft.world.entity.player.Player) {
                ChargedProjectiles charged = crossbow.get(DataComponents.CHARGED_PROJECTILES);
                boolean firework = charged != null && charged.contains(Items.FIREWORK_ROCKET);

                if (firework) {
                    ItemStack projectile = charged.getItems().isEmpty() ? new ItemStack(Items.FIREWORK_ROCKET) : charged.getItems().get(0);
                    FireworkRocketEntity proj = new FireworkRocketEntity(clientLevel, projectile, shooter);
                    proj.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed, divergence);
                    int fakeId = Entities.getNextFakeId();
                    proj.setId(fakeId);
                    clientLevel.addEntity(proj);
                    Entities.set(System.currentTimeMillis(), proj, proj, Entities.FAKE);
                } else {
                    Arrow arrow = new Arrow(clientLevel, shooter, new ItemStack(Items.ARROW), crossbow);
                    arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed, divergence);
                    int fakeId = Entities.getNextFakeId();
                    arrow.setId(fakeId);
                    clientLevel.addEntity(arrow);
                    Entities.set(System.currentTimeMillis(), arrow, arrow, Entities.FAKE);
                }
            }
        }
    }
}
