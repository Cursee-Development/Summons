package com.cursee.summons.core.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class AbstractSummon extends TamableAnimal {

    protected AbstractSummon(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public int secondsToTicks(float seconds) {
        return (int)(seconds * 20f);
    }
}
