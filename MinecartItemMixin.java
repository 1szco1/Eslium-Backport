package fr.solmey.clienthings.mixin.minecarts;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecartItem.class)
public abstract class MinecartItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        if (JsonConfig.config.minecart.enabled && JsonConfig.shouldWork(JsonConfig.config.minecart.servers)) {
            if (info.getReturnValue() == ActionResult.SUCCESS) {
                ClientWorld clientWorld = (ClientWorld) context.getWorld();
                World world = context.getWorld();
                BlockPos blockPos = context.getBlockPos();
                BlockState blockState = world.getBlockState(blockPos);
                RailShape railShape = (blockState.getBlock() instanceof AbstractRailBlock) ? blockState.get(((AbstractRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                double d = 0.0D;
                if (railShape.isAscending()) {
                    d = 0.5D;
                }
                Vec3d vec3d = new Vec3d(blockPos.getX() + 0.5D, blockPos.getY() + 0.0625D + d, blockPos.getZ() + 0.5D);

                // In 1.16.5, create default minecart entity
                AbstractMinecartEntity abstractMinecartEntity = new MinecartEntity(clientWorld, vec3d.x, vec3d.y, vec3d.z);
                AbstractMinecartEntity initialAbstractMinecartEntity = new MinecartEntity(clientWorld, vec3d.x, vec3d.y, vec3d.z);

                int fakeId = Entities.getNextFakeId();
                abstractMinecartEntity.setEntityId(fakeId);
                clientWorld.addEntity(fakeId, abstractMinecartEntity);
                Entities.set(System.currentTimeMillis(), abstractMinecartEntity, initialAbstractMinecartEntity, Entities.FAKE);
            }
        }
    }
}
