package net.solmey.eslium.mixin.interactables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(TntBlock.class)
public class TntBlockMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void eslium$useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().interactables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().interactables.servers)) {
            if (world instanceof ClientLevel clientLevel) {
                PrimedTnt tnt = new PrimedTnt(clientLevel, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, player);
                int fakeId = Entities.getNextFakeId();
                tnt.setId(fakeId);
                clientLevel.addEntity(tnt);
                Entities.set(System.currentTimeMillis(), tnt, tnt, Entities.FAKE);
            }
        }
    }
}
