package fr.solmey.clienthings.mixin.elytras;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("onGround")
    boolean getOnGround();

    @Accessor("onGround")
    void setOnGround(boolean onGround);
}
