package com.cursee.summons.core.common.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class AbstractSummon extends TamableAnimal {

    protected AbstractSummon(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public int secondsToTicks(float seconds) {
        return (int)(seconds * 20f);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {

        if (this.getHealth() < this.getMaxHealth() && isFood(player.getItemInHand(hand))) {

            this.heal(2f);
            player.getItemInHand(hand).shrink(1);

            return InteractionResult.CONSUME;
        }

        return super.mobInteract(player, hand);
    }
}
