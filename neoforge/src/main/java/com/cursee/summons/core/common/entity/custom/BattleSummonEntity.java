package com.cursee.summons.core.common.entity.custom;

import com.cursee.summons.core.common.entity.AbstractSummon;
import com.cursee.summons.core.common.registry.ModEntityTypesNeoForge;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BattleSummonEntity extends AbstractSummon {

    private static final List<Supplier<Item>> CONSUMABLE;

    public BattleSummonEntity(EntityType<?> entityType, Level level) {
        super(ModEntityTypesNeoForge.BATTLE_SUMMON.get(), level);
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
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 16D);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {

        if (source.is(DamageTypes.LIGHTNING_BOLT)) return false;
        if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE)) return false;

        return super.hurt(source, damage);
    }

    static {
        CONSUMABLE = List.of(() -> Items.COAL, () -> Items.CHARCOAL, () -> Items.BLAZE_POWDER, () -> Items.GUNPOWDER, () -> Items.FIRE_CHARGE);
    }
}
