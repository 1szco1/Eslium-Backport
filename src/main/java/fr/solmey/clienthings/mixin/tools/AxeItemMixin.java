package fr.solmey.clienthings.mixin.tools;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    private static final Map<Block, Block> STRIPPED_MAP = new HashMap<>();
    static {
        STRIPPED_MAP.put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG);
        STRIPPED_MAP.put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG);
        STRIPPED_MAP.put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG);
        STRIPPED_MAP.put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG);
        STRIPPED_MAP.put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG);
        STRIPPED_MAP.put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG);
        STRIPPED_MAP.put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM);
        STRIPPED_MAP.put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM);
        STRIPPED_MAP.put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD);
        STRIPPED_MAP.put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD);
        STRIPPED_MAP.put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD);
        STRIPPED_MAP.put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD);
        STRIPPED_MAP.put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD);
        STRIPPED_MAP.put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD);
        STRIPPED_MAP.put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE);
        STRIPPED_MAP.put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE);
    }

    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (JsonConfig.config.tools.enabled && JsonConfig.shouldWork(JsonConfig.config.tools.servers)) {
            if (cir.getReturnValue() == ActionResult.SUCCESS && context.getWorld() instanceof ClientWorld) {
                BlockState state = context.getWorld().getBlockState(context.getBlockPos());
                Block stripped = STRIPPED_MAP.get(state.getBlock());
                if (stripped != null) {
                    ((ClientWorld) context.getWorld()).setBlockState(context.getBlockPos(), stripped.getDefaultState());
                }
            }
        }
    }
}
