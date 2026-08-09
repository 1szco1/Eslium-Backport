package fr.solmey.clienthings.mixin.elytras;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("roll")
    int getRoll();

    @Accessor("roll")
    void setRoll(int roll);
}
