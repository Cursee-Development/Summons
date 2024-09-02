package com.cursee.summons.core.common.registry;

import com.cursee.summons.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class RegistryNeoForge {

    protected static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MOD_ID);
    protected static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MOD_ID);
    protected static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Constants.MOD_ID);
    protected static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    protected static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Constants.MOD_ID);
    protected static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

    public static void register(IEventBus modEventBus) {

        ModBlocksNeoForge.register();
        ModItemsNeoForge.register();
        ModTabsNeoForge.register();

        ModBlockEntityTypesNeoForge.register();
        ModEntityTypesNeoForge.register();
        ModDataComponentsNeoForge.register();

        RegistryNeoForge.BLOCKS.register(modEventBus);
        RegistryNeoForge.ITEMS.register(modEventBus);
        RegistryNeoForge.TABS.register(modEventBus);

        RegistryNeoForge.BLOCK_ENTITY_TYPES.register(modEventBus);
        RegistryNeoForge.ENTITY_TYPES.register(modEventBus);
        RegistryNeoForge.DATA_COMPONENTS.register(modEventBus);
    }

    protected static <I extends Block, T extends I> DeferredHolder<Block, T> registerBlock(String blockID, Supplier<T> blockSupplier) {
        return BLOCKS.register(blockID, blockSupplier);
    }

    protected static <I extends Item, T extends I> DeferredHolder<Item, T> registerItem(String itemID, Supplier<T> item) {
        return ITEMS.register(itemID, item);
    }

    protected static <I extends Block, T extends I> DeferredHolder<Block, T> registerBlockAndBlockItem(String blockItemID, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = registerBlock(blockItemID, block);
        registerItem(blockItemID, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    protected static <I extends CreativeModeTab, T extends I> DeferredHolder<CreativeModeTab, T> registerCreativeModeTab(String tabID, Supplier<T> tab) {
        return TABS.register(tabID, tab);
    }

    protected static <I extends BlockEntityType<?>, T extends I> DeferredHolder<BlockEntityType<?>, T> registerBlockEntityType(String blockEntityTypeID, Supplier<T> blockEntityType) {
        return BLOCK_ENTITY_TYPES.register(blockEntityTypeID, blockEntityType);
    }

    protected static <I extends EntityType<?>, T extends I> DeferredHolder<EntityType<?>, T> registerEntityType(String entityTypeID, Supplier<T> entityType) {
        return ENTITY_TYPES.register(entityTypeID, entityType);
    }

    protected static <I extends DataComponentType<?>, T extends I> DeferredHolder<DataComponentType<?>, T> registerDataComponent(String dataComponentID, Supplier<T> dataComponent) {
        return DATA_COMPONENTS.register(dataComponentID, dataComponent);
    }

}
