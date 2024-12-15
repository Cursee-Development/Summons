package com.cursee.summons.core.common.registry;

import com.cursee.summons.core.datagen.worldgen.custom.configured.SummoningRoom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;


public class ModFeaturesNeoForge {

    public static void register() {}

    public static final DeferredHolder<Feature<?>, SummoningRoom> CUSTOM_MONSTER_ROOM = RegistryNeoForge.register("custom_monster_room", new SummoningRoom(NoneFeatureConfiguration.CODEC));
}
