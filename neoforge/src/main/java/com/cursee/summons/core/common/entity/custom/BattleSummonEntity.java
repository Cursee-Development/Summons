package com.cursee.summons.core.common.entity.custom;

import com.cursee.summons.core.common.entity.AbstractSummon;
import com.cursee.summons.core.common.entity.goal.RhinoAttackGoal;
import com.cursee.summons.core.common.registry.ModEntityTypesNeoForge;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BattleSummonEntity extends AbstractSummon {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    private static final List<Supplier<Item>> CONSUMABLE;

    // synch data
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(BattleSummonEntity.class, EntityDataSerializers.BOOLEAN);

    public BattleSummonEntity(EntityType<?> entityType, Level level) {
        super(ModEntityTypesNeoForge.BATTLE_SUMMON.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(ATTACKING, false);
    }

    public void setAttacking(boolean shouldBeAttacking) {
        this.entityData.set(ATTACKING, shouldBeAttacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
//        this.goalSelector.addGoal(1, new TamableAnimal.TamableAnimalPanicGoal(1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
//        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
//        this.goalSelector.addGoal(3, new Wolf.WolfAvoidEntityGoal<>(this, Llama.class, 24.0F, 1.5, 1.5));
//        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new RhinoAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
//        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
//        this.goalSelector.addGoal(9, new BegGoal(this, 8.0F));
//        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
//        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
//        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
//        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Monster.class, false));
//        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return canConsume(itemStack);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return (mob instanceof BattleSummonEntity) ? ModEntityTypesNeoForge.BATTLE_SUMMON.get().create(level) : null;
    }

    private boolean canConsume(ItemStack itemStack) {

        for (Supplier<Item> item : CONSUMABLE) {
            if (itemStack.is(item.get())) return true;
        }

        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 16D)
                .add(Attributes.ATTACK_DAMAGE, 8D)
                .add(Attributes.ATTACK_KNOCKBACK, 2D);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {

        if (source.is(DamageTypes.LIGHTNING_BOLT)) return false;
        if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE)) return false;

        return super.hurt(source, damage);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {

        // idling
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = secondsToTicks(2f);
            this.idleAnimationState.start(this.tickCount);
        }
        else {
            --this.idleAnimationTimeout;
        }

        // attacking
        if (this.isAttacking() && this.attackAnimationTimeout <= 0) {
            this.attackAnimationTimeout = secondsToTicks(0.5f);
            attackAnimationState.start(this.tickCount);
        }
        else {
            --this.attackAnimationTimeout;
        }

        if (!this.isAttacking()) {
            this.attackAnimationState.stop();
        }
    }

    static {
        CONSUMABLE = List.of(() -> Items.COAL, () -> Items.CHARCOAL, () -> Items.BLAZE_POWDER, () -> Items.GUNPOWDER, () -> Items.FIRE_CHARGE);
    }
}
