package com.cursee.summons.core.common.block.custom;

import com.cursee.summons.core.common.registry.ModBlocksNeoForge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SummonTombstoneBlockNeoForge extends Block implements EntityBlock {

    public static final int MAX_AGE = 11;
    public static final int DELAY_IN_TICKS = 10;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty SUMMONING_AGE = IntegerProperty.create("summoning_age", 0, MAX_AGE);

    public final BlockState DEFAULT_BLOCK_STATE_NO_FACING = defaultBlockState().setValue(SUMMONING_AGE, 0);

    public static final VoxelShape SHAPE = Block.box(-1, 0, -1, 17, 18, 17);

    public SummonTombstoneBlockNeoForge(Properties properties) {
        super(properties);
        this.registerDefaultState(DEFAULT_BLOCK_STATE_NO_FACING.setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return DEFAULT_BLOCK_STATE_NO_FACING.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SUMMONING_AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        // ask the player to empty their hands via a client-side message
        if (level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND && itemStack != ItemStack.EMPTY) {

            player.displayClientMessage(Component.translatable("message.summons.requires_empty_hands"), true);

            return ItemInteractionResult.FAIL;
        }

        if (!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND && itemStack == ItemStack.EMPTY) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }



        return ItemInteractionResult.FAIL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {

        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        if (blockState.hasProperty(SUMMONING_AGE) && blockState.getValue(SUMMONING_AGE) == 0) {
            serverLevel.setBlock(blockPos, blockState.setValue(SUMMONING_AGE, 1), Block.UPDATE_ALL_IMMEDIATE);
            serverLevel.scheduleTick(blockPos, this, DELAY_IN_TICKS);
        }

        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @Override
    protected void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {

        if (blockState.hasProperty(SUMMONING_AGE) && blockState.getValue(SUMMONING_AGE) > 0 && blockState.getValue(SUMMONING_AGE) < MAX_AGE) {
            serverLevel.setBlock(blockPos, blockState.setValue(SUMMONING_AGE, blockState.getValue(SUMMONING_AGE) + 1), Block.UPDATE_ALL_IMMEDIATE);

            serverLevel.playSound(null, blockPos, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, (float) blockState.getValue(SUMMONING_AGE) / MAX_AGE, (float) blockState.getValue(SUMMONING_AGE) / MAX_AGE);
            serverLevel.playSound(null, blockPos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS, (float) blockState.getValue(SUMMONING_AGE) / MAX_AGE, (float) blockState.getValue(SUMMONING_AGE) / MAX_AGE);

            serverLevel.scheduleTick(blockPos, this, DELAY_IN_TICKS);
        }

        if (blockState.hasProperty(SUMMONING_AGE) && blockState.getValue(SUMMONING_AGE) >= MAX_AGE) {

            serverLevel.playSound(null, blockPos, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 1f, 1f);

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
            lightningBolt.moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            serverLevel.addFreshEntity(lightningBolt);

            serverLevel.setBlock(blockPos, ModBlocksNeoForge.SUMMON_TOMBSTONE_USED.get().defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);

            final int FIRE_SET_RADIUS = 2;

            for (int xOffset = -FIRE_SET_RADIUS; xOffset <= FIRE_SET_RADIUS; xOffset++) {
                for (int yOffset = -FIRE_SET_RADIUS; yOffset <= FIRE_SET_RADIUS; yOffset++) {
                    for (int zOffset = -FIRE_SET_RADIUS; zOffset <= FIRE_SET_RADIUS; zOffset++) {

                        final boolean doAttempt = randomSource.nextInt(1, 4) == 1;

                        if (doAttempt && xOffset != 0 && yOffset != 0 && zOffset != 0) {

                            final BlockPos attemptPos = blockPos.offset(xOffset, yOffset, zOffset);

                            serverLevel.explode(null, attemptPos.getX(), attemptPos.getY(), attemptPos.getZ(), 0.2f, true, Level.ExplosionInteraction.TNT);
                        }

                    } // zOffset
                } // yOffset
            } // xOffset

            for (int xOffset = -FIRE_SET_RADIUS; xOffset <= FIRE_SET_RADIUS; xOffset++) {
                for (int yOffset = -FIRE_SET_RADIUS; yOffset <= FIRE_SET_RADIUS; yOffset++) {
                    for (int zOffset = -FIRE_SET_RADIUS; zOffset <= FIRE_SET_RADIUS; zOffset++) {

                        final boolean doAttempt = randomSource.nextInt(1, 4) == 1;

                        if (doAttempt && xOffset != 0 && yOffset != 0 && zOffset != 0) {

                            final BlockPos attemptPos = blockPos.offset(xOffset, yOffset, zOffset);

                            serverLevel.setBlock(attemptPos, Blocks.FIRE.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                        }

                    } // zOffset
                } // yOffset
            } // xOffset
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {

        // ages to 11, but we stop particles at 10 to allow them to dissipate / disperse
        if (blockState.hasProperty(SUMMONING_AGE) && blockState.getValue(SUMMONING_AGE) >= 1 && blockState.getValue(SUMMONING_AGE) <= 10) {

            // enchant particles should stop in the center of the egg
            for (BlockPos position : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                if (randomSource.nextInt(16) > 7)
                    level.addParticle(ParticleTypes.ENCHANT, (double) blockPos.getX() + 0.5, (double) blockPos.getY() + 2.0 + 0.5, (double) blockPos.getZ() + 0.5, (double) ((float) position.getX() + randomSource.nextFloat()) - 0.5, (double) ((float) position.getY() - randomSource.nextFloat() - 1.0F), (double) ((float) position.getZ() + randomSource.nextFloat()) - 0.5);
            }

            for (int particleCount = 0; particleCount < 3; ++particleCount) {

                int xMod = randomSource.nextInt(2) * 2 - 1;
                int zMod = randomSource.nextInt(2) * 2 - 1;

                double xPos = (double) blockPos.getX() + 0.5D + 0.25D * (double) xMod;
                double yPos = (double) ((float) blockPos.getY() + randomSource.nextFloat());
                double zPos = (double) blockPos.getZ() + 0.5D + 0.25D * (double) zMod;

                double xSpeed = (double) (randomSource.nextFloat() * (float) xMod);
                double ySpeed = ((double) randomSource.nextFloat() - 0.5D) * 0.125D;
                double zSpeed = (double) (randomSource.nextFloat() * (float) zMod);

                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, xPos, yPos, zPos, xSpeed * 0.1D, ySpeed * 0.1D, zSpeed * 0.1D);
            }
        }
    }
}
