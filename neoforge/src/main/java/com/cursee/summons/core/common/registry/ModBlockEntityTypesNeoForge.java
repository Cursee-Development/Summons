package com.cursee.summons.core.common.registry;

import com.cursee.summons.core.common.block.entity.custom.SummonTombstoneBlockEntityNeoForge;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntityTypesNeoForge {

    public static void register() {}

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SummonTombstoneBlockEntityNeoForge>> SUMMON_TOMBSTONE = RegistryNeoForge.registerBlockEntityType("summon_tombstone", () -> BlockEntityType.Builder.of(SummonTombstoneBlockEntityNeoForge::new, ModBlocksNeoForge.SUMMON_TOMBSTONE.get()).build(null));
}
