package fr.solmey.clienthings.predictions;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

public class UseItemOnPacket {

    public static void onSentPacket(Packet<?> packet) {
        if (packet instanceof PlayerInteractBlockC2SPacket) {
            PlayerInteractBlockC2SPacket sPacket = (PlayerInteractBlockC2SPacket) packet;
            MinecraftClient minecraft = MinecraftClient.getInstance();
            PlayerEntity player = minecraft.player;
            if (player == null) return;
            World level = player.world;
            Hand hand = sPacket.getHand();
            ItemStack itemStack = player.getStackInHand(hand);

            if (
                !itemStack.getItem().equals(Items.END_CRYSTAL) &&
                !itemStack.getItem().equals(Items.MINECART)
            ) return;

            if (JsonConfig.config == null) return;

            BlockHitResult blockHit = sPacket.getBlockHitResult();
            BlockPos pos = blockHit.getBlockPos();

            BlockState state = level.getBlockState(pos);
            if (state.isAir()) {
                // No-op: nothing to predict on air.
            }
            if (minecraft.interactionManager != null &&
                minecraft.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR) {
                return;
            }

            boolean haveSomethingInOurHands =
                !player.getMainHandStack().isEmpty() ||
                !player.getOffHandStack().isEmpty();
            boolean suppressUsingBlock =
                player.isSneaking() &&
                haveSomethingInOurHands;

            if (!suppressUsingBlock) {
                // Always assume that this part never cancels the prediction.
            }

            if (
                !itemStack.isEmpty() &&
                !player.getItemCooldownManager().isCoolingDown(itemStack.getItem())
            ) {
                ItemUsageContext context = new ItemUsageContext(
                    player,
                    hand,
                    blockHit
                );
                if (player.abilities.creativeMode) {
                    int count = itemStack.getCount();
                    itemStack.useOnBlock(context);
                    itemStack.setCount(count);
                } else {
                    itemStack.useOnBlock(context);
                }
            }
        }
    }
}
