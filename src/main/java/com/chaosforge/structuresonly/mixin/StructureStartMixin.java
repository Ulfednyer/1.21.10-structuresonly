package com.chaosforge.structuresonly.mixin;

import com.chaosforge.structuresonly.StructuresOnly;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureStart.class)
public class StructureStartMixin {
    @Inject(method = "placeInChunk", at = @At("HEAD"))
    private void beforePlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, CallbackInfo ci) {
        StructuresOnly.IS_GENERATING_STRUCTURE.set(true);
    }

    @Inject(method = "placeInChunk", at = @At("TAIL"))
    private void afterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, CallbackInfo ci) {
        StructuresOnly.IS_GENERATING_STRUCTURE.set(false);
    }
}
