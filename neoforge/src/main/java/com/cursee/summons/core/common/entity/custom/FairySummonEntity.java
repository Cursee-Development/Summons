package com.cursee.summons.core.common.entity.custom;

import com.cursee.summons.core.common.entity.AbstractSummon;
import com.cursee.summons.core.common.registry.ModEntityTypesNeoForge;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FairySummonEntity extends AbstractSummon {

    public FairySummonEntity(EntityType<?> entityType, Level level) {
        super(ModEntityTypesNeoForge.FAIRY_SUMMON.get(), level);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.has(DataComponents.FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return (mob instanceof FairySummonEntity) ? ModEntityTypesNeoForge.FAIRY_SUMMON.get().create(level) : null;
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 5D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 16D);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {

        if (source.is(DamageTypes.LIGHTNING_BOLT)) return false;
        if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE)) return false;

        return super.hurt(source, damage);
    }
}
