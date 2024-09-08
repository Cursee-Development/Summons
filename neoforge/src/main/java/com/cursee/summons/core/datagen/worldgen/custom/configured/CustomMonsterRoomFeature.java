package com.cursee.summons.core.datagen.worldgen.custom.configured;

import com.cursee.summons.Constants;
import com.cursee.summons.SummonsNeoForge;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.MonsterRoomHooks;
import org.slf4j.Logger;

/**
 * CustomMonsterFeature is a variant of `MonsterRoomFeature`. These features are "hard-coded" meaning the placement
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
public class CustomMonsterRoomFeature extends MonsterRoomFeature {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityType<?>[] MOBS = new EntityType[]{EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER};
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    private static final List<BlockPos> attemptedPlacements = new ArrayList<>();

    public CustomMonsterRoomFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override @SuppressWarnings("deprecation")
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        // final BlockPos pos = context.origin();

        // Constants.LOG.info("Attempted to place CustomMonsterRoomFeature instance at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());

        // return super.place(context);
        // return attemptOriginalMonsterRoom(context);

        // based on testing, the feature will not always be placed
        return CustomMonsterRoomFeaturePlacement.attemptWithContext(this, context); // attemptCustomMonsterRoom(context);
    }

    private boolean attemptCustomMonsterRoom(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        Predicate<BlockState> cannotReplacePredicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        WorldGenLevel level = context.level();

        boolean hasPlaced = false;

        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();

//        final int SPACING = 42 / 2; // the answer to life, the universe, and everything (cut in half because we are not very knowledgeable)
//
//        if (x % SPACING == 0 && (y > -SPACING &&  y < SPACING) && z % SPACING == 0) {
//
//            Constants.LOG.info("Attempted to place CustomerMonsterRoomFeature instance at ({}, {}, {})", x, y, z);
//
//            this.safeSetBlock(level, pos, Blocks.BEACON.defaultBlockState(), cannotReplacePredicate);
//            level.setBlock(pos, Blocks.BEACON.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
//
//            this.safeSetBlock(level, pos.below(), Blocks.DIAMOND_BLOCK.defaultBlockState(), cannotReplacePredicate);
//
//            for (int i=2; i<10; i++) {
//                this.safeSetBlock(level, pos.below(i), Blocks.LAVA.defaultBlockState(), cannotReplacePredicate);
//            }
//        }

        Constants.LOG.info("Attempted to place CustomerMonsterRoomFeature instance at ({}, {}, {})", x, y, z);
        SummonsNeoForge.attemptedCustomMonsterRooms += 1;

        this.safeSetBlock(level, pos, Blocks.BEACON.defaultBlockState(), cannotReplacePredicate);
        level.setBlock(pos, Blocks.BEACON.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);

        this.safeSetBlock(level, pos.below(), Blocks.DIAMOND_BLOCK.defaultBlockState(), cannotReplacePredicate);

        for (int i=2; i<10; i++) {
            this.safeSetBlock(level, pos.below(i), Blocks.LAVA.defaultBlockState(), cannotReplacePredicate);
        }

        // if only the beacon was placed, the feature was not placed completely.
        // if the beacon and diamond block are found, assume that the feature is fully placed.

        if (level.getBlockState(pos).getBlock() == Blocks.BEACON) {

            Constants.LOG.info("found BEACON block during formation check");
            SummonsNeoForge.attemptedFoundBeaconCustomMonsterRooms += 1;

            if (level.getBlockState(pos.below()).getBlock() == Blocks.DIAMOND_BLOCK) {

                Constants.LOG.info("found DIAMOND_BLOCK block during formation check");
                SummonsNeoForge.createdCustomMonsterRooms += 1;

                Constants.LOG.info("Creation Ratio: ({}, {}) -> ({})",
                        SummonsNeoForge.createdCustomMonsterRooms,
                        SummonsNeoForge.attemptedCustomMonsterRooms,
                        SummonsNeoForge.createdCustomMonsterRooms / SummonsNeoForge.attemptedCustomMonsterRooms
                );

                hasPlaced = true;
            }
            else {
                Constants.LOG.info("ONLY found BEACON block during formation check!!! {}", SummonsNeoForge.attemptedFoundBeaconCustomMonsterRooms);
            }
        }

        return hasPlaced;
    }

    /* Modified to hopefully be understandable at some point... */
    private boolean attemptOriginalMonsterRoom(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        Predicate<BlockState> cannotReplacePredicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos spawnerPosition = context.origin();
        RandomSource randomSource = context.random();
        WorldGenLevel level = context.level();

        // unused by default, due to obfuscation
        final int ROOM_HEIGHT_ABOVE_SPAWNER = 3;

        final int FLOOR_LAYER = -1;
        final int CEILING_LAYER = 4;

        int xWidth = randomSource.nextInt(2) + 2;
        // nextInt would return 1 or 2
        // which would get modified to 3 or 4 with the "+2"

        int lowerBoundOffsetX = -xWidth - 1;
        // -xWidth would result in -3 or -4, changing to -4 or -5 with the "-1"

        int upperBoundOffsetX = xWidth + 1;
        // xWidth would result in 3 or 4, changing to 4 or 5 with the "+1"

        int zLength = randomSource.nextInt(2) + 2;
        // nextInt would return 1 or 2
        // which would get modified to 3 or 4 with the "+2"

        int lowerBoundOffsetZ = -zLength - 1;
        // -zLength would result in -3 or -4, changing to -4 or -5 with the "-1"

        int upperBoundOffsetZ = zLength + 1;
        // zLength would result in 3 or 4, changing to 4 or 5 with the "+1"

        boolean canBePlaced = false;

        for (int localX = lowerBoundOffsetX; localX <= upperBoundOffsetX; localX++) {
            for (int localY = FLOOR_LAYER; localY <= CEILING_LAYER; localY++) {
                for (int localZ = lowerBoundOffsetZ; localZ <= upperBoundOffsetZ; localZ++) {

                    BlockPos checkedPosition = spawnerPosition.offset(localX, localY, localZ);

                    final boolean checkingSolidBlock = level.getBlockState(checkedPosition).isSolid();

                    if ((localY == FLOOR_LAYER || localY == CEILING_LAYER) && !checkingSolidBlock) {
                        return false;
                    }

                    if ((localX == lowerBoundOffsetX || localX == upperBoundOffsetX || localZ == lowerBoundOffsetZ || localZ == upperBoundOffsetZ)
                            && localY == 0
                            && level.isEmptyBlock(checkedPosition)
                            && level.isEmptyBlock(checkedPosition.above())) {
                        canBePlaced = true;
                    }
                }
            }
        }

        if (canBePlaced) {
            
            for (int xOffset = lowerBoundOffsetX; xOffset <= upperBoundOffsetX; xOffset++) {
                for (int yOffset = ROOM_HEIGHT_ABOVE_SPAWNER; yOffset >= -1; yOffset--) {
                    for (int zOffset = lowerBoundOffsetZ; zOffset <= upperBoundOffsetZ; zOffset++) {

                        BlockPos positionToCheck = spawnerPosition.offset(xOffset, yOffset, zOffset);

                        BlockState positionBlockState = level.getBlockState(positionToCheck);

                        if (xOffset == lowerBoundOffsetX || yOffset == -1 || zOffset == lowerBoundOffsetZ || xOffset == upperBoundOffsetX || yOffset == 4 || zOffset == upperBoundOffsetZ) {
                            if (positionToCheck.getY() >= level.getMinBuildHeight() && !level.getBlockState(positionToCheck.below()).isSolid()) {
                                level.setBlock(positionToCheck, AIR, 2);
                            }
                            else if (positionBlockState.isSolid() && !positionBlockState.is(Blocks.CHEST)) {
                                if (yOffset == -1 && randomSource.nextInt(4) != 0) {
                                    this.safeSetBlock(level, positionToCheck, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), cannotReplacePredicate);
                                }
                                else {
                                    this.safeSetBlock(level, positionToCheck, Blocks.COBBLESTONE.defaultBlockState(), cannotReplacePredicate);
                                }
                            }
                        }
                        else if (!positionBlockState.is(Blocks.CHEST) && !positionBlockState.is(Blocks.SPAWNER)) {
                            this.safeSetBlock(level, positionToCheck, AIR, cannotReplacePredicate);
                        }
                    }
                }
            }

            for (int l3 = 0; l3 < 2; l3++) {
                for (int j4 = 0; j4 < 3; j4++) {
                    int l4 = spawnerPosition.getX() + randomSource.nextInt(xWidth * 2 + 1) - xWidth;
                    int i5 = spawnerPosition.getY();
                    int j5 = spawnerPosition.getZ() + randomSource.nextInt(zLength * 2 + 1) - zLength;
                    BlockPos blockpos2 = new BlockPos(l4, i5, j5);
                    if (level.isEmptyBlock(blockpos2)) {
                        int j3 = 0;

                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            if (level.getBlockState(blockpos2.relative(direction)).isSolid()) {
                                j3++;
                            }
                        }

                        if (j3 == 1) {
                            this.safeSetBlock(
                                    level, blockpos2, StructurePiece.reorient(level, blockpos2, Blocks.CHEST.defaultBlockState()), cannotReplacePredicate
                            );
                            RandomizableContainer.setBlockEntityLootTable(level, randomSource, blockpos2, BuiltInLootTables.SIMPLE_DUNGEON);
                            break;
                        }
                    }
                }
            }

            this.safeSetBlock(level, spawnerPosition, Blocks.SPAWNER.defaultBlockState(), cannotReplacePredicate);
            if (level.getBlockEntity(spawnerPosition) instanceof SpawnerBlockEntity spawnerblockentity) {
                spawnerblockentity.setEntityId(this.randomEntityId(randomSource), randomSource);
            }
            else {
                LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", spawnerPosition.getX(), spawnerPosition.getY(), spawnerPosition.getZ());
            }

            return true;
        }
        else {
            return false;
        }
    }

    private EntityType<?> randomEntityId(RandomSource randomSource) {
        return MonsterRoomHooks.getRandomMonsterRoomMob(randomSource);
    }
}

