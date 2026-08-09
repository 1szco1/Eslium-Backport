package fr.solmey.clienthings.mixin.firework;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FireworkRocketEntity.class)
public interface FireworkRocketEntityAccessor {
    @Accessor("shooter")
    void setShooter(net.minecraft.entity.LivingEntity shooter);

    @Accessor("shooter")
    net.minecraft.entity.LivingEntity getShooter();
}
