package com.chaosforge.structuresonly.mixin;

import com.chaosforge.structuresonly.StructuresOnly;
import com.chaosforge.structuresonly.config.ModConfig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
    @ModifyVariable(method = "setBlock", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private BlockState modifyPlacedBlockState(BlockState state) {
        if (StructuresOnly.IS_GENERATING_STRUCTURE.get() && !ModConfig.get().keepStructureBlocks) {
            if (!StructuresOnly.isChestOrLootBlock(state)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return state;
    }
}
