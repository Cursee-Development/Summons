package com.cursee.summons.core.datagen.worldgen;

import com.cursee.summons.Constants;
import com.cursee.summons.core.common.registry.ModFeaturesNeoForge;
import com.cursee.summons.core.datagen.worldgen.custom.configured.CustomMonsterRoomFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModConfiguredFeaturesNeoForge {

    public static final ResourceKey<ConfiguredFeature<?, ?>> CUSTOM_MONSTER_ROOM_KEY = registerKey("custom_monster_room");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
//        register(context, CUSTOM_MONSTER_ROOM_KEY, Feature.MONSTER_ROOM, new MonsterRoomFeature(NoneFeatureConfiguration.CODEC));

        // FeatureUtils.register(context, CUSTOM_MONSTER_ROOM_KEY, new CustomMonsterRoomFeature(NoneFeatureConfiguration.CODEC));
//        register(context, CUSTOM_MONSTER_ROOM_KEY, Feature.MONSTER_ROOM, new CustomMonsterRoomFeature(NoneFeatureConfiguration.CODEC));

//        register(context, CUSTOM_MONSTER_ROOM_KEY, Feature.FLOWER, new RandomPatchConfiguration(32, 6, 2,
//                PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.STONE)))));

        register(context, CUSTOM_MONSTER_ROOM_KEY, ModFeaturesNeoForge.CUSTOM_MONSTER_ROOM.get(), null);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
