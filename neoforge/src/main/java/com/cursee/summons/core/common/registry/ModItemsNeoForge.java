package com.cursee.summons.core.common.registry;

import com.cursee.summons.core.common.item.custom.DeferredTamedSpawnEggItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModItemsNeoForge {

    public static void register() {}

    public static final DeferredHolder<Item, Item> UNTAMED_FAIRY_SUMMON_SPAWN_EGG_ITEM = RegistryNeoForge.registerItem("untamed_fairy_summon_spawn_egg_item", () -> new DeferredSpawnEggItem(ModEntityTypesNeoForge.FAIRY_SUMMON, 0xFFAAAABB, 0xFF980098, new Item.Properties().fireResistant()));
    public static final DeferredHolder<Item, Item> UNTAMED_BATTLE_SUMMON_SPAWN_EGG_ITEM = RegistryNeoForge.registerItem("untamed_battle_summon_spawn_egg_item", () -> new DeferredSpawnEggItem(ModEntityTypesNeoForge.BATTLE_SUMMON, 0xFFAAAABB, 0xFF980098, new Item.Properties().fireResistant()));
    public static final DeferredHolder<Item, Item> UNTAMED_BIRD_SUMMON_SPAWN_EGG_ITEM = RegistryNeoForge.registerItem("untamed_bird_summon_spawn_egg_item", () -> new DeferredSpawnEggItem(ModEntityTypesNeoForge.BIRD_SUMMON, 0xFFAAAABB, 0xFF980098, new Item.Properties().fireResistant()));

    public static final DeferredHolder<Item, Item> TAMED_FAIRY_SUMMON_SPAWN_EGG_ITEM = RegistryNeoForge.registerItem("tamed_fairy_summon_spawn_egg_item", () -> new DeferredTamedSpawnEggItem(ModEntityTypesNeoForge.FAIRY_SUMMON, 0xFFAAAABB, 0xFFF800F8, new Item.Properties().fireResistant()));
    public static final DeferredHolder<Item, Item> TAMED_BATTLE_SUMMON_SPAWN_EGG_ITEM = RegistryNeoForge.registerItem("tamed_battle_summon_spawn_egg_item", () -> new DeferredTamedSpawnEggItem(ModEntityTypesNeoForge.BATTLE_SUMMON, 0xFFAAAABB, 0xFFF800F8, new Item.Properties().fireResistant()));
    public static final DeferredHolder<Item, Item> TAMED_BIRD_SUMMON_SPAWN_EGG_ITEM = RegistryNeoForge.registerItem("tamed_bird_summon_spawn_egg_item", () -> new DeferredTamedSpawnEggItem(ModEntityTypesNeoForge.BIRD_SUMMON, 0xFFAAAABB, 0xFFF800F8, new Item.Properties().fireResistant()));
}
