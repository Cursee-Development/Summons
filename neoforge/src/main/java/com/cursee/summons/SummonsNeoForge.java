package com.cursee.summons;

import com.cursee.summons.core.client.block.entity.renderer.SummonTombstoneBlockEntityRendererNeoForge;
import com.cursee.summons.core.client.entity.renderer.QuieterLightningBoltEntityRendererNeoForge;
import com.cursee.summons.core.common.registry.ModBlockEntityTypesNeoForge;
import com.cursee.summons.core.common.registry.ModEntityTypesNeoForge;
import com.cursee.summons.core.common.registry.RegistryNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(Constants.MOD_ID)
public class SummonsNeoForge {

    public static int createdCustomMonsterRooms, attemptedCustomMonsterRooms, attemptedFoundBeaconCustomMonsterRooms = 0;

    public SummonsNeoForge(IEventBus modEventBus, ModContainer container) {

        Summons.init();

        RegistryNeoForge.register(modEventBus);

        modEventBus.addListener(SummonsNeoForge::onRegisterEntityRenderersEvent);
    }

    private static void onRegisterEntityRenderersEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypesNeoForge.QUIETER_LIGHTNING_BOLT.get(), QuieterLightningBoltEntityRendererNeoForge::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypesNeoForge.SUMMON_TOMBSTONE.get(), SummonTombstoneBlockEntityRendererNeoForge::new);
    }
}
