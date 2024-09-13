package com.cursee.summons.core.common.registry;

import com.cursee.summons.Constants;
import com.cursee.summons.core.common.entity.custom.BattleSummonEntity;
import com.cursee.summons.core.common.entity.custom.FairySummonEntity;
import com.cursee.summons.core.common.entity.custom.QuieterLightningBoltEntityNeoForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEntityTypesNeoForge {

    public static void register() {}

    public static final DeferredHolder<EntityType<?>, EntityType<QuieterLightningBoltEntityNeoForge>> QUIETER_LIGHTNING_BOLT = RegistryNeoForge.registerEntityType("quieter_lightning_bolt", () -> EntityType.Builder.of(QuieterLightningBoltEntityNeoForge::new, MobCategory.MISC).build(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "quieter_lightning_bolt").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<FairySummonEntity>> FAIRY_SUMMON = RegistryNeoForge.registerEntityType("fairy_summon", () -> EntityType.Builder.of(FairySummonEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "fairy_summon").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<BattleSummonEntity>> BATTLE_SUMMON = RegistryNeoForge.registerEntityType("battle_summon", () -> EntityType.Builder.of(BattleSummonEntity::new, MobCategory.MISC).sized(1.5f, 2.0f).build(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "battle_summon").toString()));
}
