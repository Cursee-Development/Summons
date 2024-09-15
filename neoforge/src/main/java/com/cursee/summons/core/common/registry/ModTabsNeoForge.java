package com.cursee.summons.core.common.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModTabsNeoForge {

    public static void register() {}

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SUMMONS_TAB = RegistryNeoForge.registerCreativeModeTab("summons_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup.summons"))
            .icon(() -> new ItemStack(ModBlocksNeoForge.SUMMON_TOMBSTONE.get()))
            .displayItems((displayParameters, output) -> {

                output.accept(ModBlocksNeoForge.SUMMON_TOMBSTONE.get());
                output.accept(ModBlocksNeoForge.SUMMON_TOMBSTONE_USED.get());

                output.accept(ModItemsNeoForge.UNTAMED_FAIRY_SUMMON_SPAWN_EGG_ITEM.get());
                output.accept(ModItemsNeoForge.UNTAMED_BATTLE_SUMMON_SPAWN_EGG_ITEM.get());
                output.accept(ModItemsNeoForge.UNTAMED_BIRD_SUMMON_SPAWN_EGG_ITEM.get());

                output.accept(ModItemsNeoForge.TAMED_FAIRY_SUMMON_SPAWN_EGG_ITEM.get());
                output.accept(ModItemsNeoForge.TAMED_BATTLE_SUMMON_SPAWN_EGG_ITEM.get());
                output.accept(ModItemsNeoForge.TAMED_BIRD_SUMMON_SPAWN_EGG_ITEM.get());
            }).build());
}
