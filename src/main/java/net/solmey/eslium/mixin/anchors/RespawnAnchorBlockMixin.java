package net.solmey.eslium.mixin.anchors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.AnchorHandler;

@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorBlockMixin {

    @Inject(method = "useItemOn", at = @At("HEAD"))
    private void eslium$onUseItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (level == null || !level.isClientSide()) return;
        if (ConfigManager.getConfig() == null || ConfigManager.getConfig().anchors == null
                || !ConfigManager.getConfig().anchors.enabled) return;
        if (!ConfigManager.shouldWork(ConfigManager.getConfig().anchors.servers)) return;

        Minecraft client = Minecraft.getInstance();
        if (client != null && client.isLocalServer()) return;
        if (stack == null || !stack.is(Items.GLOWSTONE)) return;
        if (!ConfigManager.getConfig().anchors.glowstoneSound) return;

        int charges = state.getValue(RespawnAnchorBlock.CHARGE);
        if (charges < 4) {
            level.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Inject(method = "useWithoutItem", at = @At("HEAD"))
    private void eslium$onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (level == null || !level.isClientSide()) return;
        if (ConfigManager.getConfig() == null || ConfigManager.getConfig().anchors == null
                || !ConfigManager.getConfig().anchors.enabled) return;
        if (!ConfigManager.shouldWork(ConfigManager.getConfig().anchors.servers)) return;

        Minecraft client = Minecraft.getInstance();
        if (client != null && client.isLocalServer()) return;

        if (player.isShiftKeyDown()) return;
        int charges = state.getValue(RespawnAnchorBlock.CHARGE);
        if (charges <= 0) return;

        boolean anchorWorks = level.environmentAttributes()
                .getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, pos);
        if (anchorWorks) return;

        AnchorHandler.handleAnchorDetonation(client, level, pos, hit);
    }
}
