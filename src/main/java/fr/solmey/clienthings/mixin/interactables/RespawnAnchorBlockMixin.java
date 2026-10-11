package fr.solmey.clienthings.mixin.interactables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.AnchorHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"))
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient) return;
        if (JsonConfig.config == null || JsonConfig.config.anchors == null || !Boolean.TRUE.equals(JsonConfig.config.anchors.enabled)) return;
        if (!JsonConfig.shouldWork(JsonConfig.config.anchors.servers)) return;

        MinecraftClient client = MinecraftClient.getInstance();
        boolean isSingleplayer = client != null && client.isInSingleplayer();
        if (isSingleplayer) {
            return;
        }

        ItemStack mainHandStack = player.getStackInHand(Hand.MAIN_HAND);
        ItemStack offHandStack = player.getStackInHand(Hand.OFF_HAND);
        boolean holdingGlowstoneMainHand = mainHandStack.getItem() == Items.GLOWSTONE;
        boolean holdingGlowstoneOffHand = offHandStack.getItem() == Items.GLOWSTONE;
        boolean isGlowstoneInteraction = holdingGlowstoneMainHand || (hand == Hand.OFF_HAND && holdingGlowstoneOffHand);

        int currentCharges = state.get(RespawnAnchorBlock.CHARGES);

        // Immediate glowstone charge sound feedback
        if (isGlowstoneInteraction) {
            if (currentCharges < 4 && Boolean.TRUE.equals(JsonConfig.config.anchors.glowstoneSound)) {
                world.playSound(player, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return;
        }

        // Anchor detonation: only when un-sneaking, not holding glowstone, and anchor is charged
        if (player.isSneaking()) {
            return;
        }

        if (currentCharges <= 0) {
            return;
        }

        // Check if dimension causes anchor to explode (!isRespawnAnchorWorking)
        if (world.getDimension().isRespawnAnchorWorking()) {
            return; // In Nether, anchors set spawn point, they do not explode
        }

        // Execute optimized client-side anchor detonation
        AnchorHandler.handleAnchorDetonation(client, world, pos, hit);
    }
}
