package com.cursee.summons.core.datagen.worldgen.custom.configured;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class CustomMonsterRoomFeaturePlacement {

    public static boolean attemptWithContext(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        final WorldGenLevel level = context.level();
        final ChunkGenerator chunkGenerator = context.chunkGenerator();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final FeatureConfiguration configuration = context.config();

        final Predicate<BlockState> notReplaceable = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);

        // unlike the original MonsterRoomFeature, we want to always create a cube instead of sometimes a cube, and sometimes a rectangular prism

        final int XZ_OFFSET_WALL = 4;

        final int Y_OFFSET_FLOOR = -1;
        final int Y_OFFSET_CEILING = 4;

        boolean canBePlaced = false;

        for (int xOffset = -XZ_OFFSET_WALL; xOffset <= XZ_OFFSET_WALL; xOffset++) {
            for (int yOffset = Y_OFFSET_FLOOR; yOffset <= Y_OFFSET_CEILING; yOffset++) {
                for (int zOffset = -XZ_OFFSET_WALL; zOffset <= XZ_OFFSET_WALL; zOffset++) {

                    // create an offset position to verify
                    final BlockPos positionToCheck = origin.offset(xOffset, yOffset, zOffset);

                    // verify if the position is a solid block
                    final boolean positionIsSolid = level.getBlockState(positionToCheck).isSolid();

                    // if the floor or ceiling are not solid, return early
                    if ((yOffset == Y_OFFSET_FLOOR || yOffset == Y_OFFSET_CEILING) && !positionIsSolid) {
                        return false;
                    }

                    // checks if there is an entry into the room or exposed corner, already carved by world generation
                    if ((xOffset == XZ_OFFSET_WALL || xOffset == -XZ_OFFSET_WALL || zOffset == XZ_OFFSET_WALL || zOffset == -XZ_OFFSET_WALL) && yOffset == 0 && level.isEmptyBlock(positionToCheck) && level.isEmptyBlock(positionToCheck.above())) {
                        canBePlaced = true;
                    }
                } // z
            } // y
        } // x

        if (canBePlaced) return createFeature(level, chunkGenerator, random, origin, configuration);

        return false;
    }

    private static boolean createFeature(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, FeatureConfiguration configuration) {



        return false;
    }
}
