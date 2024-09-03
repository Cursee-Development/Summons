package com.cursee.summons.core.datagen.worldgen.custom.configured;

import com.cursee.summons.Constants;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.MonsterRoomHooks;
import org.slf4j.Logger;

import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;

public class CustomMonsterRoomFeature extends Feature<NoneFeatureConfiguration> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityType<?>[] MOBS = new EntityType[]{EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER};
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    private static final List<BlockPos> attemptedPlacements = new ArrayList<>();

    public CustomMonsterRoomFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override @SuppressWarnings("deprecation")
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos pos = context.origin();
        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();

        boolean hasPlaced = false;

        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();

        final int SPACING = 42 / 2; // the answer to life, the universe, and everything (cut in half because we are not very knowledgeable)

        if (x % SPACING == 0 && (y > -SPACING &&  y < SPACING) && z % SPACING == 0) {
            Constants.LOG.info("Attempted to place CustomerMonsterRoomFeature instance at ({}, {}, {})", x, y, z);

            this.safeSetBlock(worldgenlevel, pos, Blocks.BEACON.defaultBlockState(), predicate);

            this.safeSetBlock(worldgenlevel, pos.below(), Blocks.DIAMOND_BLOCK.defaultBlockState(), predicate);

            for (int i=2; i<10; i++) {
                this.safeSetBlock(worldgenlevel, pos.below(i), Blocks.LAVA.defaultBlockState(), predicate);
            }

            hasPlaced = true;
        }

        // do nothing, returning false by default after this
        if (false) {
            int origin = 3;
            int randomLimit1 = randomsource.nextInt(2) + 2;
            int negativeOffsetFromRandom1 = -randomLimit1 - 1;
            int positiveOffsetFromRandom1 = randomLimit1 + 1;

            int lowerBound = -1;
            int upperBound = 4;
            int randomLimit2 = randomsource.nextInt(2) + 2;
            int negativeOffsetFromRandom2 = -randomLimit2 - 1;
            int positiveOffsetFromRandom2 = randomLimit2 + 1;

            int roomBoundingPositionY = 0;

            for (int xOffset = negativeOffsetFromRandom1; xOffset <= positiveOffsetFromRandom1; xOffset++) {
                for (int yOffset = lowerBound; yOffset <= upperBound; yOffset++) {
                    for (int zOffset = negativeOffsetFromRandom2; zOffset <= positiveOffsetFromRandom2; zOffset++) {

                        BlockPos testPosition = pos.offset(xOffset, yOffset, zOffset);
                        boolean solidPosition = worldgenlevel.getBlockState(testPosition).isSolid();

                        if (yOffset == lowerBound && !solidPosition) {
                            return false;
                        }

                        if (yOffset == upperBound && !solidPosition) {
                            return false;
                        }

                        if ((xOffset == negativeOffsetFromRandom1 || xOffset == positiveOffsetFromRandom1 || zOffset == negativeOffsetFromRandom2 || zOffset == positiveOffsetFromRandom2) && yOffset == 0 && worldgenlevel.isEmptyBlock(testPosition) && worldgenlevel.isEmptyBlock(testPosition.above())) {
                            roomBoundingPositionY++;
                        }
                    }
                }
            }

            if (roomBoundingPositionY >= 1 && roomBoundingPositionY <= 5) {

                for (int placeOffsetX = negativeOffsetFromRandom1; placeOffsetX <= positiveOffsetFromRandom1; placeOffsetX++) {
                    for (int placeOffsetY = 3; placeOffsetY >= -1; placeOffsetY--) {
                        for (int placeOffsetZ = negativeOffsetFromRandom2; placeOffsetZ <= positiveOffsetFromRandom2; placeOffsetZ++) {

                            BlockPos roomShellPlacementPosition = pos.offset(placeOffsetX, placeOffsetY, placeOffsetZ);

                            BlockState blockstate = worldgenlevel.getBlockState(roomShellPlacementPosition);

                            if (placeOffsetX == negativeOffsetFromRandom1 || placeOffsetY == lowerBound || placeOffsetZ == negativeOffsetFromRandom2 || placeOffsetX == positiveOffsetFromRandom1 || placeOffsetY == upperBound || placeOffsetZ == positiveOffsetFromRandom2) {
                                if (roomShellPlacementPosition.getY() >= worldgenlevel.getMinBuildHeight() && !worldgenlevel.getBlockState(roomShellPlacementPosition.below()).isSolid()) {
                                    worldgenlevel.setBlock(roomShellPlacementPosition, AIR, 2);
                                } else if (blockstate.isSolid() && !blockstate.is(Blocks.CHEST)) {

                                    // 4 is not relevant to upperBound here, it's simply a 3/4 or 75% chance to be true
                                    if (placeOffsetY == lowerBound && randomsource.nextInt(4) != 0) {
                                        this.safeSetBlock(worldgenlevel, roomShellPlacementPosition, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), predicate);
                                    } else {
                                        this.safeSetBlock(worldgenlevel, roomShellPlacementPosition, Blocks.COBBLESTONE.defaultBlockState(), predicate);
                                    }
                                }
                            } else if (!blockstate.is(Blocks.CHEST) && !blockstate.is(Blocks.SPAWNER)) {
                                this.safeSetBlock(worldgenlevel, roomShellPlacementPosition, AIR, predicate);
                            }
                        }
                    }

                }

                for (int chestXTest = 0; chestXTest < 2; chestXTest++) {
                    for (int chestZTest = 0; chestZTest < 3; chestZTest++) {

                        int possibleChestPosX = pos.getX() + randomsource.nextInt(randomLimit1 * 2 + 1) - randomLimit1;
                        int possibleChestPosY = pos.getY();
                        int possibleChestPosZ = pos.getZ() + randomsource.nextInt(randomLimit2 * 2 + 1) - randomLimit2;

                        final BlockPos finalChestPosition = new BlockPos(possibleChestPosX, possibleChestPosY, possibleChestPosZ);

                        if (worldgenlevel.isEmptyBlock(finalChestPosition)) {

                            boolean hasSolidPlaceableSurface = false;

                            for (Direction direction : Direction.Plane.HORIZONTAL) {
                                if (worldgenlevel.getBlockState(finalChestPosition.relative(direction)).isSolid()) {
                                    hasSolidPlaceableSurface = true;
                                }
                            }

                            if (hasSolidPlaceableSurface) {

                                this.safeSetBlock(worldgenlevel, finalChestPosition, StructurePiece.reorient(worldgenlevel, finalChestPosition, Blocks.CHEST.defaultBlockState()), predicate);

                                Constants.LOG.info("Placed a a Chest for a Custom Monster Room feature at at ({}, {}, {})", finalChestPosition.getX(), finalChestPosition.getY(), finalChestPosition.getZ());

                                RandomizableContainer.setBlockEntityLootTable(worldgenlevel, randomsource, finalChestPosition, BuiltInLootTables.SIMPLE_DUNGEON);
                                break;
                            }
                        }
                    }
                }

                this.safeSetBlock(worldgenlevel, pos, Blocks.SPAWNER.defaultBlockState(), predicate);

                if (worldgenlevel.getBlockEntity(pos) instanceof SpawnerBlockEntity spawnerblockentity) {
                    spawnerblockentity.setEntityId(this.randomEntityId(randomsource), randomsource);
                } else {
                    LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
                }

                return true;
            } else {
                return false;
            }
        }

        return hasPlaced;
    }

    private EntityType<?> randomEntityId(RandomSource randomSource) {
        return MonsterRoomHooks.getRandomMonsterRoomMob(randomSource);
    }
}

