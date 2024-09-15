package com.cursee.summons.core.common.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.Objects;
import java.util.function.Supplier;

public class DeferredTamedSpawnEggItem extends DeferredSpawnEggItem {

    public DeferredTamedSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Properties props) {
        super(type, backgroundColor, highlightColor, props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            if (level.getBlockEntity(blockpos) instanceof Spawner spawner) {
                EntityType<?> entitytype1 = this.getType(itemstack);
                spawner.setEntityId(entitytype1, level.getRandom());
                level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockpos);
                itemstack.shrink(1);
                return InteractionResult.CONSUME;
            } else {
                BlockPos blockpos1;
                if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                    blockpos1 = blockpos;
                } else {
                    blockpos1 = blockpos.relative(direction);
                }

                EntityType<?> entitytype = this.getType(itemstack);

                Entity entity = entitytype.spawn(
                        (ServerLevel)level,
                        itemstack,
                        context.getPlayer(),
                        blockpos1,
                        MobSpawnType.SPAWN_EGG,
                        true,
                        !Objects.equals(blockpos, blockpos1) && direction == Direction.UP
                );

                if (entity != null && context.getPlayer() != null) {

                    if (entity instanceof TamableAnimal tamableAnimal) {
                        tamableAnimal.tame(context.getPlayer());
                    }

                    itemstack.shrink(1);
                    level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
                }

                return InteractionResult.CONSUME;
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemstack);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            if (!(level.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemstack);
            } else if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos, blockhitresult.getDirection(), itemstack)) {
                EntityType<?> entitytype = this.getType(itemstack);
                Entity entity = entitytype.spawn((ServerLevel)level, itemstack, player, blockpos, MobSpawnType.SPAWN_EGG, false, false);
                if (entity == null) {
                    return InteractionResultHolder.pass(itemstack);
                } else {

                    if (entity instanceof TamableAnimal tamableAnimal) {
                        tamableAnimal.tame(player);
                    }

                    itemstack.consume(1, player);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
                    return InteractionResultHolder.consume(itemstack);
                }
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}
