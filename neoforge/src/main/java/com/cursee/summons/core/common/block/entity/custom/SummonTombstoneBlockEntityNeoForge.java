package com.cursee.summons.core.common.block.entity.custom;

import com.cursee.summons.core.common.registry.ModBlockEntityTypesNeoForge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SummonTombstoneBlockEntityNeoForge extends BlockEntity {

    public SummonTombstoneBlockEntityNeoForge(BlockPos blocKPos, BlockState blockState) {
        super(ModBlockEntityTypesNeoForge.SUMMON_TOMBSTONE.get(), blocKPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SummonTombstoneBlockEntityNeoForge tombstone) {
    }
}
