package com.cursee.summons.core.datagen.worldgen.custom.configured;

import com.cursee.summons.Constants;
import com.cursee.summons.core.common.registry.ModBlocksNeoForge;
import com.cursee.summons.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Predicate;

public class CustomMonsterRoomFeaturePlacement {

    public static final int XZ_OFFSET_WALL = 8;

    public static final int Y_OFFSET_FLOOR = -1;
    public static final int Y_OFFSET_CEILING = 4;

    public static boolean attemptWithContext(CustomMonsterRoomFeature feature, FeaturePlaceContext<NoneFeatureConfiguration> context) {

        final WorldGenLevel level = context.level();
        final ChunkGenerator chunkGenerator = context.chunkGenerator();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final FeatureConfiguration configuration = context.config();

        // unlike the original MonsterRoomFeature, we want to always create a cube instead of sometimes a cube, and sometimes a rectangular prism

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

        if (canBePlaced) return createFeature(feature, level, chunkGenerator, random, origin, configuration);

        return false;
    }

    private static boolean createFeature(CustomMonsterRoomFeature feature, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, FeatureConfiguration configuration) {

        final Predicate<BlockState> notReplaceable = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);

        final int XZ_LOWER_BOUND = -XZ_OFFSET_WALL + 1;
        final int XZ_UPPER_BOUND = XZ_OFFSET_WALL - 1;

        for (int xOffset = XZ_LOWER_BOUND; xOffset <= XZ_UPPER_BOUND; xOffset++) {
            for (int yOffset = 3; yOffset >= -1; yOffset--) {
                for (int zOffset = XZ_LOWER_BOUND; zOffset <= XZ_UPPER_BOUND; zOffset++) {

                    final BlockPos positionToCheck = origin.offset(xOffset, yOffset, zOffset);
                    final BlockState positionBlockState = level.getBlockState(positionToCheck);

                    // check floor, walls, and ceiling

                    if (xOffset == XZ_LOWER_BOUND || yOffset == Y_OFFSET_FLOOR || zOffset == XZ_LOWER_BOUND || xOffset == XZ_UPPER_BOUND || yOffset == Y_OFFSET_CEILING || zOffset == XZ_UPPER_BOUND) {

                        if (positionToCheck.getY() >= level.getMinBuildHeight() && !level.getBlockState(positionToCheck.below()).isSolid()) {
                            // level.setBlock(positionToCheck, Blocks.CAVE_AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                            safeSetBlock(level, positionToCheck, Blocks.CAVE_AIR.defaultBlockState(), notReplaceable);
                        }
                        else if (positionBlockState.isSolid() && !positionBlockState.is(Blocks.CHEST)) {

                            if (yOffset == Y_OFFSET_FLOOR && random.nextInt(4) != 0) {
                                if (random.nextBoolean()) {
                                    safeSetBlock(level, positionToCheck, Blocks.QUARTZ_BRICKS.defaultBlockState(), notReplaceable);
                                } else {
                                    safeSetBlock(level, positionToCheck, Blocks.GLOWSTONE.defaultBlockState(), notReplaceable);
                                }
                            }
                            else {
                                safeSetBlock(level, positionToCheck, Blocks.QUARTZ_BLOCK.defaultBlockState(), notReplaceable);
                            }
                        }
                    }
                    else if (yOffset == Y_OFFSET_CEILING - 1 && xOffset == 0 && zOffset == 0) {
                        safeSetBlock(level, positionToCheck, Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true), notReplaceable);
                    }
                    else if (!positionBlockState.is(Blocks.CHEST) && !positionBlockState.is(Blocks.SPAWNER)) {
                        safeSetBlock(level, positionToCheck, Blocks.CAVE_AIR.defaultBlockState(), notReplaceable);
                    }
                }
            }
        }

        // 1 in 1000 chance to place "used" tombstone
        if (random.nextInt(1, 1000) == 1) {
            safeSetBlock(level, origin, ModBlocksNeoForge.SUMMON_TOMBSTONE_USED.get().defaultBlockState(), notReplaceable);
        } else {
            safeSetBlock(level, origin, ModBlocksNeoForge.SUMMON_TOMBSTONE.get().defaultBlockState(), notReplaceable);
        }

        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            Constants.LOG.info("*Should* have created CustomMonsterRoomFeature at ({}, {}, {})", origin.getX(), origin.getY(), origin.getZ());
        }

        return false;
    }

    public static void safeSetBlock(WorldGenLevel level, BlockPos pos, BlockState state, Predicate<BlockState> predicate) {
        if (predicate.test(level.getBlockState(pos))) {
            level.setBlock(pos, state, Block.UPDATE_CLIENTS);
        }
    }
}
