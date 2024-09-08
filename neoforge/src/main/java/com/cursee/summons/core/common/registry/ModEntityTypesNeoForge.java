package com.cursee.summons.core.common.registry;

import com.cursee.summons.Constants;
import com.cursee.summons.core.common.entity.custom.QuieterLightningBoltEntityNeoForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEntityTypesNeoForge {

    public static void register() {}

    public static final DeferredHolder<EntityType<?>, EntityType<QuieterLightningBoltEntityNeoForge>> QUIETER_LIGHTNING_BOLT = RegistryNeoForge.registerEntityType("quieter_lightning_bolt", () -> EntityType.Builder.of(QuieterLightningBoltEntityNeoForge::new, MobCategory.MISC).build(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "quieter_lightning_bolt").toString()));
}
