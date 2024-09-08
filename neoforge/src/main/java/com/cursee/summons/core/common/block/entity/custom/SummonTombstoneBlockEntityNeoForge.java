package com.cursee.summons.core.common.block.entity.custom;

import com.cursee.summons.Constants;
import com.cursee.summons.core.common.registry.ModBlockEntityTypesNeoForge;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SummonTombstoneBlockEntityNeoForge extends BlockEntity {

    public SummonTombstoneBlockEntityNeoForge(BlockPos blocKPos, BlockState blockState) {
        super(ModBlockEntityTypesNeoForge.SUMMON_TOMBSTONE.get(), blocKPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SummonTombstoneBlockEntityNeoForge tombstone) {

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel && serverLevel.getLevelData().getGameTime() % 20L == 0) {
            // Constants.LOG.info("ticking at ({}, {}, {})", blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
    }
}
