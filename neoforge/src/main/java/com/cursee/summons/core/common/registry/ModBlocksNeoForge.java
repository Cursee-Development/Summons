package com.cursee.summons.core.common.registry;

import com.cursee.summons.core.common.block.custom.SummonTombstoneBlockNeoForge;
import com.cursee.summons.core.common.block.custom.SummonTombstoneUsedBlockNeoForge;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlocksNeoForge {

    public static void register() {}

    public static final DeferredHolder<Block, Block> SUMMON_TOMBSTONE = RegistryNeoForge.registerBlockAndBlockItem("summon_tombstone", () -> new SummonTombstoneBlockNeoForge(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(50.0F, 1200.0F).noOcclusion()));

    public static final DeferredHolder<Block, Block> SUMMON_TOMBSTONE_USED = RegistryNeoForge.registerBlockAndBlockItem("summon_tombstone_used", () -> new SummonTombstoneUsedBlockNeoForge(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(50.0F, 1200.0F).noOcclusion()));
}
