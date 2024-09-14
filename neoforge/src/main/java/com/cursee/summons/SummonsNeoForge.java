package com.cursee.summons;

import com.cursee.summons.core.client.block.entity.renderer.SummonTombstoneBlockEntityRendererNeoForge;
import com.cursee.summons.core.client.entity.model.BattleSummonModel;
import com.cursee.summons.core.client.entity.model.BirdSummonModel;
import com.cursee.summons.core.client.entity.model.FairySummonModel;
import com.cursee.summons.core.client.entity.renderer.BattleSummonRenderer;
import com.cursee.summons.core.client.entity.renderer.BirdSummonRenderer;
import com.cursee.summons.core.client.entity.renderer.FairySummonRenderer;
import com.cursee.summons.core.client.entity.renderer.QuieterLightningBoltEntityRendererNeoForge;
import com.cursee.summons.core.common.entity.custom.BattleSummonEntity;
import com.cursee.summons.core.common.entity.custom.BirdSummonEntity;
import com.cursee.summons.core.common.entity.custom.FairySummonEntity;
import com.cursee.summons.core.common.registry.ModBlockEntityTypesNeoForge;
import com.cursee.summons.core.common.registry.ModEntityTypesNeoForge;
import com.cursee.summons.core.common.registry.ModModelLayersNeoForge;
import com.cursee.summons.core.common.registry.RegistryNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(Constants.MOD_ID)
public class SummonsNeoForge {

    public static int createdCustomMonsterRooms, attemptedCustomMonsterRooms, attemptedFoundBeaconCustomMonsterRooms = 0;

    public SummonsNeoForge(IEventBus modEventBus, ModContainer container) {

        Summons.init();

        RegistryNeoForge.register(modEventBus);

        modEventBus.addListener(SummonsNeoForge::onRegisterLayerDefinitionsEvent);
        modEventBus.addListener(SummonsNeoForge::onRegisterEntityRenderersEvent);
        modEventBus.addListener(SummonsNeoForge::onCreateEntityAttributesEvent);
    }

    private static void onRegisterLayerDefinitionsEvent(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayersNeoForge.FAIRY_SUMMON_LAYER, FairySummonModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayersNeoForge.BATTLE_SUMMON_LAYER, BattleSummonModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayersNeoForge.BIRD_SUMMON_LAYER, BirdSummonModel::createBodyLayer);
    }

    private static void onRegisterEntityRenderersEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypesNeoForge.QUIETER_LIGHTNING_BOLT.get(), QuieterLightningBoltEntityRendererNeoForge::new);
        event.registerEntityRenderer(ModEntityTypesNeoForge.FAIRY_SUMMON.get(), FairySummonRenderer::new);
        event.registerEntityRenderer(ModEntityTypesNeoForge.BATTLE_SUMMON.get(), BattleSummonRenderer::new);
        event.registerEntityRenderer(ModEntityTypesNeoForge.BIRD_SUMMON.get(), BirdSummonRenderer::new);

        event.registerBlockEntityRenderer(ModBlockEntityTypesNeoForge.SUMMON_TOMBSTONE.get(), SummonTombstoneBlockEntityRendererNeoForge::new);
    }

    private static void onCreateEntityAttributesEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypesNeoForge.FAIRY_SUMMON.get(), FairySummonEntity.createAttributes().build());
        event.put(ModEntityTypesNeoForge.BATTLE_SUMMON.get(), BattleSummonEntity.createAttributes().build());
        event.put(ModEntityTypesNeoForge.BIRD_SUMMON.get(), BirdSummonEntity.createAttributes().build());
    }
}
