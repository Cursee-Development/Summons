package com.cursee.summons.core.datagen.worldgen.custom.configured;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * SummoningRoomFeature is a variant of `MonsterRoomFeature`. These features are "hard-coded" meaning the placement
 * of every block is written out manually. We can use algorithms to place these in unique fashions, which may allow for
 * structures beyond the limits of Minecraft's vanilla system. This could also be a complete waste of my and your time.
 * <br />
 * Placement attempts of the feature are data-driven, using the `InSquarePlacement` codec and `VerticalAnchor`
 * references to determine valid Y values.
 * @see net.minecraft.world.level.levelgen.feature.MonsterRoomFeature
 * @see net.minecraft.world.level.levelgen.placement.InSquarePlacement
 * @see net.minecraft.world.level.levelgen.VerticalAnchor
 */
/* Minecraft's Overworld has a logical height of 384 blocks, with the non-inclusive build limits at -64 and 320 blocks.
 * -64 Bedrock
 *   0 Stone and Deepslate Transition
 * +63 Water
 */
public class SummoningRoom extends Feature<NoneFeatureConfiguration> {

    public SummoningRoom(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return SummoningRoomFeaturePlacement.attemptWithContext(this, context);
    }
}

