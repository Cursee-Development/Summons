package com.cursee.summons.core.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class AbstractSummon extends Animal {

    protected AbstractSummon(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
}
