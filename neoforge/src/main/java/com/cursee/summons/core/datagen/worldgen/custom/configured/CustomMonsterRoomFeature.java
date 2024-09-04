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

        return hasPlaced;
    }

    private EntityType<?> randomEntityId(RandomSource randomSource) {
        return MonsterRoomHooks.getRandomMonsterRoomMob(randomSource);
    }
}

