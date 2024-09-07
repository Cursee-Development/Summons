package com.cursee.summons.mixin;

import com.cursee.summons.Constants;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MonsterRoomFeature.class)
public class NeoForgeMonsterRoomFeatureMixin extends Feature<NoneFeatureConfiguration> {

    // not logging :(
    @Inject(method = "place", at = @At("RETURN"))
    private void summons$injected2(FeaturePlaceContext<NoneFeatureConfiguration> p_160066_, CallbackInfoReturnable<Boolean> cir) {

        if (cir.getReturnValue()) {
            final BlockPos pos = p_160066_.origin();
            Constants.LOG.info("Placed a vanilla MonsterRoomFeature at ({}, {}, {})!", pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Inject(method = "place", at = @At("HEAD"))
    private void summons$injected1(FeaturePlaceContext<NoneFeatureConfiguration> p_160066_, CallbackInfoReturnable<Boolean> cir) {

        final BlockPos pos = p_160066_.origin();

        Constants.LOG.info("Attempted to place a vanilla MonsterRoomFeature at ({}, {}, {})!", pos.getX(), pos.getY(), pos.getZ());
    }

    public NeoForgeMonsterRoomFeatureMixin(Codec<NoneFeatureConfiguration> p_65786_) {
        super(p_65786_);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159749_) {
        return false;
    }
}
