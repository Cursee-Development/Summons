package com.cursee.summons.core.common.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            }).build());
}
