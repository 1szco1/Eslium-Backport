package net.solmey.eslium.mixin.tools;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.solmey.eslium.config.ConfigManager;

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

    @Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
    private void eslium$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().tools.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().tools.servers)) {
            if (cir.getReturnValue() == InteractionResult.SUCCESS && context.getLevel() instanceof ClientLevel clientLevel) {
                BlockState state = context.getLevel().getBlockState(context.getClickedPos());
                Block stripped = STRIPPED_MAP.get(state.getBlock());
                if (stripped != null) {
                    clientLevel.setBlock(context.getClickedPos(), stripped.defaultBlockState(), 3);
                }
            }
        }
    }
}
