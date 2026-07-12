package com.chaosforge.structuresonly.mixin;

import com.chaosforge.structuresonly.util.INaturallyGenerated;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {
    @Inject(method = "setLootTable", at = @At("HEAD"))
    private void onSetLootTable(ResourceKey<LootTable> key, CallbackInfo ci) {
        if (key != null) {
            ((INaturallyGenerated) this).structuresonly$setNaturallyGenerated(true);
        }
    }
}
