package com.chaosforge.structuresonly.mixin;

import com.chaosforge.structuresonly.util.INaturallyGenerated;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements INaturallyGenerated {
    private boolean structuresonly_naturallyGenerated = false;

    @Override
    public boolean structuresonly$isNaturallyGenerated() {
        return this.structuresonly_naturallyGenerated;
    }

    @Override
    public void structuresonly$setNaturallyGenerated(boolean value) {
        this.structuresonly_naturallyGenerated = value;
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void onSaveAdditional(ValueOutput valueOutput, CallbackInfo ci) {
        if (this.structuresonly_naturallyGenerated) {
            valueOutput.putBoolean("StructuresOnlyNaturallyGenerated", true);
        }
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void onLoadAdditional(ValueInput valueInput, CallbackInfo ci) {
        this.structuresonly_naturallyGenerated = valueInput.getBooleanOr("StructuresOnlyNaturallyGenerated", false);
    }
}
