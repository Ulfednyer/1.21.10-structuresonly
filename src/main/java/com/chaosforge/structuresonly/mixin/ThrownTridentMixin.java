package com.chaosforge.structuresonly.mixin;

import com.chaosforge.structuresonly.config.ModConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {

    @Shadow
    private static EntityDataAccessor<Byte> ID_LOYALTY;

    // Dummy constructor required by the compiler for extending AbstractArrow
    private ThrownTridentMixin() {
        super(null, null);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTickCheckVoid(CallbackInfo ci) {
        if (!ModConfig.get().loyaltyTridentVoidReturn) {
            return;
        }

        // Check if the trident Y position is below the void threshold
        if (this.getY() >= this.level().getMinY() - 64) {
            return;
        }

        byte loyalty = this.getEntityData().get(ID_LOYALTY);
        if (loyalty <= 0) {
            return;
        }

        Entity owner = this.getOwner();
        if (owner instanceof ServerPlayer player && player.isAlive()) {
            ItemStack tridentItem = this.getPickupItem();

            if (!player.getInventory().add(tridentItem)) {
                player.drop(tridentItem, false);
            }

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1.0F, 1.0F);

            this.discard();
        }
    }
}
