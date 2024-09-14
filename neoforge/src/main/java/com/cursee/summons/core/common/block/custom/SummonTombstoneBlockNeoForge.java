package com.cursee.summons.core.common.block.custom;

import com.cursee.summons.core.common.block.entity.custom.SummonTombstoneBlockEntityNeoForge;
import com.cursee.summons.core.common.entity.AbstractSummon;
import com.cursee.summons.core.common.entity.custom.QuieterLightningBoltEntityNeoForge;
import com.cursee.summons.core.common.registry.ModBlockEntityTypesNeoForge;
import com.cursee.summons.core.common.registry.ModBlocksNeoForge;
import com.cursee.summons.core.common.registry.ModEntityTypesNeoForge;
import com.cursee.summons.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SummonTombstoneBlockNeoForge extends Block implements EntityBlock {

    public static final int MAX_AGE = 11;
    public static final int DELAY_IN_TICKS = 10;

    public static final IntegerProperty SUMMONING_AGE = IntegerProperty.create("summoning_age", 0, MAX_AGE);

    public final BlockState DEFAULT_BLOCK_STATE = defaultBlockState().setValue(SUMMONING_AGE, 0);

    public static final VoxelShape SHAPE = Block.box(-1, 0, -1, 17, 18, 17);

    public SummonTombstoneBlockNeoForge(Properties properties) {
        super(properties);
        this.registerDefaultState(DEFAULT_BLOCK_STATE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return DEFAULT_BLOCK_STATE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUMMONING_AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypesNeoForge.SUMMON_TOMBSTONE.get().create(pos, state);
    }

    public static @Nullable <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntityTypesNeoForge.SUMMON_TOMBSTONE.get(), SummonTombstoneBlockEntityNeoForge::tick);
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

            if (serverLevel.getBlockEntity(blockPos) instanceof SummonTombstoneBlockEntityNeoForge tombstone && tombstone.temporaryPlayerReference == null) {
                tombstone.temporaryPlayerReference = player;
            }

            serverLevel.scheduleTick(blockPos, this, DELAY_IN_TICKS);
        }
        else if (player.isShiftKeyDown() && Services.PLATFORM.isDevelopmentEnvironment()) {
            if (blockState.hasProperty(SUMMONING_AGE) && blockState.getValue(SUMMONING_AGE) == 0) {
                serverLevel.setBlock(blockPos, blockState.setValue(SUMMONING_AGE, MAX_AGE - 1), Block.UPDATE_ALL_IMMEDIATE);
                serverLevel.scheduleTick(blockPos, this, DELAY_IN_TICKS);
            }
        }

        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @Override
    protected void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {

        if (blockState.hasProperty(SUMMONING_AGE) && blockState.getValue(SUMMONING_AGE) > 0 && blockState.getValue(SUMMONING_AGE) < MAX_AGE) {

            serverLevel.playSound(null, blockPos, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, (float) blockState.getValue(SUMMONING_AGE) / MAX_AGE + 0.3f, Math.min((float) blockState.getValue(SUMMONING_AGE) / MAX_AGE + 0.2f, 1.0f));
            serverLevel.playSound(null, blockPos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS, (float) blockState.getValue(SUMMONING_AGE) / MAX_AGE + 0.3f, Math.min((float) blockState.getValue(SUMMONING_AGE) / MAX_AGE + 0.2f, 1.0f));

            if (blockState.getValue(SUMMONING_AGE) % 4 == 0) {

                final int LIGHTNING_ATTEMPT_RADIUS = 4;

                for (int xOffset = -LIGHTNING_ATTEMPT_RADIUS; xOffset <= LIGHTNING_ATTEMPT_RADIUS; xOffset++) {
                    for (int yOffset = 0; yOffset <= 1; yOffset++) {
                        for (int zOffset = -LIGHTNING_ATTEMPT_RADIUS; zOffset <= LIGHTNING_ATTEMPT_RADIUS; zOffset++) {

                            final boolean doAttempt = randomSource.nextInt(1, 64) == 1;

                            if (doAttempt) {

                                final BlockPos attemptPos = blockPos.offset(xOffset, yOffset, zOffset);

                                if (blockPos.distToCenterSqr(attemptPos.getX(), attemptPos.getY(), attemptPos.getZ()) > 1.0D) {
                                    QuieterLightningBoltEntityNeoForge lightningBolt = ModEntityTypesNeoForge.QUIETER_LIGHTNING_BOLT.get().create(serverLevel);
                                    lightningBolt.moveTo(attemptPos.getX(), attemptPos.getY(), attemptPos.getZ());
                                    serverLevel.addFreshEntity(lightningBolt);
                                }
                            }

                        } // zOffset
                    } // yOffset
                } // xOffset
            }

            serverLevel.setBlock(blockPos, blockState.setValue(SUMMONING_AGE, blockState.getValue(SUMMONING_AGE) + 1), Block.UPDATE_ALL_IMMEDIATE);

            serverLevel.scheduleTick(blockPos, this, DELAY_IN_TICKS);
        }

        if (blockState.hasProperty(SUMMONING_AGE) && blockState.getValue(SUMMONING_AGE) >= MAX_AGE) {

            serverLevel.playSound(null, blockPos, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 0.1f, 0.1f);

            QuieterLightningBoltEntityNeoForge lightningBolt = ModEntityTypesNeoForge.QUIETER_LIGHTNING_BOLT.get().create(serverLevel);
            lightningBolt.moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            serverLevel.addFreshEntity(lightningBolt);

            final int FIRE_ATTEMPT_RADIUS = 2;

            for (int xOffset = -FIRE_ATTEMPT_RADIUS; xOffset <= FIRE_ATTEMPT_RADIUS; xOffset++) {
                for (int yOffset = -FIRE_ATTEMPT_RADIUS; yOffset <= FIRE_ATTEMPT_RADIUS; yOffset++) {
                    for (int zOffset = -FIRE_ATTEMPT_RADIUS; zOffset <= FIRE_ATTEMPT_RADIUS; zOffset++) {

                        final boolean doAttempt = randomSource.nextInt(1, 4) == 1;

                        if (doAttempt && xOffset != 0 && yOffset != 0 && zOffset != 0) {

                            final BlockPos attemptPos = blockPos.offset(xOffset, yOffset, zOffset);

                            if (serverLevel.getBlockState(attemptPos).isEmpty()) serverLevel.setBlock(attemptPos, Blocks.FIRE.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                        }

                    } // zOffset
                } // yOffset
            } // xOffset

            List<EntityType<? extends AbstractSummon>> possibleSummonTypes = List.of(ModEntityTypesNeoForge.FAIRY_SUMMON.get(), ModEntityTypesNeoForge.BATTLE_SUMMON.get(), ModEntityTypesNeoForge.BIRD_SUMMON.get());
            final int toSpawnIndex = randomSource.nextInt(1, possibleSummonTypes.size() + 1) - 1; // range is non-inclusive of upper bound

            AbstractSummon summon = possibleSummonTypes.get(toSpawnIndex).create(serverLevel);

            if (summon == null) return; // functions as assertion but better

            if (serverLevel.getBlockEntity(blockPos) instanceof SummonTombstoneBlockEntityNeoForge tombstone && tombstone.temporaryPlayerReference != null) {

                serverLevel.getEntitiesOfClass(AbstractSummon.class, new AABB(blockPos).inflate(15.0D)).forEach(entity -> {
                    if (entity.isOwnedBy(tombstone.temporaryPlayerReference)) {
                        entity.discard();
                    }
                });

                summon.tame(tombstone.temporaryPlayerReference);
            }

            summon.moveTo(blockPos.getX(), blockPos.above().getY(), blockPos.getZ());
            serverLevel.addFreshEntity(summon);
            double d0 = serverLevel.random.nextDouble() * (float) (Math.PI * 2);
            summon.setDeltaMovement(-Math.sin(d0) * 0.02, 0.4F, -Math.cos(d0) * 0.02);
            summon.setRemainingFireTicks(4 * 20);

            // finish by replacing the original tombstone
            serverLevel.setBlock(blockPos, ModBlocksNeoForge.SUMMON_TOMBSTONE_USED.get().defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    } // end tick(BlockState, ServerLevel, BlockPos, RandomSource)

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {

        for (int i = 0; i < 3; i++) {

            int xMod = randomSource.nextInt(2) * 2 - 1;
            int zMod = randomSource.nextInt(2) * 2 - 1;

            double xPos = (double)blockPos.getX() + 0.5 + 0.25 * (double)xMod;
            double yPos = (double)((float)blockPos.getY() + randomSource.nextFloat());
            double zPos = (double)blockPos.getZ() + 0.5 + 0.25 * (double)zMod;

            double xSpeed = (double)(randomSource.nextFloat() * (float)xMod);
            double ySpeed = ((double)randomSource.nextFloat() - 0.5) * 0.125;
            double zSpeed = (double)(randomSource.nextFloat() * (float)zMod);

            level.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        }

        // ages to MAX_AGE, but we stop particles at MAX_AGE - 2 to allow them to dissipate / disperse
        if (blockState.hasProperty(SUMMONING_AGE) && blockState.getValue(SUMMONING_AGE) >= 1 && blockState.getValue(SUMMONING_AGE) <= MAX_AGE - 2) {

            // enchant particles should stop in the center of the egg
            for (BlockPos position : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                if (randomSource.nextInt(16) > 7)
                    level.addParticle(ParticleTypes.ENCHANT, (double) blockPos.getX() + 0.5, (double) blockPos.getY() + 2.0 + 0.5, (double) blockPos.getZ() + 0.5, (double) ((float) position.getX() + randomSource.nextFloat()) - 0.5, (double) ((float) position.getY() - randomSource.nextFloat() - 1.0F), (double) ((float) position.getZ() + randomSource.nextFloat()) - 0.5);
            }

            for (int particleCount = 0; particleCount < 16; ++particleCount) {

                int xMod = randomSource.nextInt(2) * 2 - 1;
                int zMod = randomSource.nextInt(2) * 2 - 1;

                double xPos = (double) blockPos.getX() + 0.5D + 0.25D * (double) xMod;
                double yPos = (double) ((float) blockPos.getY() + randomSource.nextFloat());
                double zPos = (double) blockPos.getZ() + 0.5D + 0.25D * (double) zMod;

                double xSpeed = (double) (randomSource.nextFloat() * (float) xMod);
                double ySpeed = ((double) randomSource.nextFloat() - 0.5D) * 0.125D;
                double zSpeed = (double) (randomSource.nextFloat() * (float) zMod);

                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, xPos, yPos, zPos, xSpeed * 0.2D, ySpeed * 0.1D, zSpeed * 0.2D);
                level.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed * 0.2D, ySpeed * 0.1D, zSpeed * 0.2D);
            }
        }
    }

    public static int summoningLightEmission(BlockState blockState) {
        return blockState.getValue(SUMMONING_AGE);
    }
}
