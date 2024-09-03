package com.cursee.summons.core.datagen;

import com.cursee.summons.Constants;
import com.cursee.summons.core.datagen.worldgen.ModBiomeModifiersNeoForge;
import com.cursee.summons.core.datagen.worldgen.ModConfiguredFeaturesNeoForge;
import com.cursee.summons.core.datagen.worldgen.ModPlacedFeaturesNeoForge;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeaturesNeoForge::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeaturesNeoForge::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiersNeoForge::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Constants.MOD_ID));
    }
}
