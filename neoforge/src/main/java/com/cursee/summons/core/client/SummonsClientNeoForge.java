package com.cursee.summons.core.client;

import com.cursee.summons.Constants;
import com.cursee.summons.core.client.block.entity.renderer.SummonTombstoneBlockEntityRendererNeoForge;
import com.cursee.summons.core.client.entity.model.BattleSummonModel;
import com.cursee.summons.core.client.entity.model.BirdSummonModel;
import com.cursee.summons.core.client.entity.model.FairySummonModel;
import com.cursee.summons.core.client.entity.renderer.BattleSummonRenderer;
import com.cursee.summons.core.client.entity.renderer.BirdSummonRenderer;
import com.cursee.summons.core.client.entity.renderer.FairySummonRenderer;
import com.cursee.summons.core.client.entity.renderer.QuieterLightningBoltEntityRendererNeoForge;
import com.cursee.summons.core.client.registry.ModModelLayersNeoForge;
import com.cursee.summons.core.common.registry.ModBlockEntityTypesNeoForge;
import com.cursee.summons.core.common.registry.ModEntityTypesNeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SummonsClientNeoForge {

    @SubscribeEvent
    private static void onRegisterLayerDefinitionsEvent(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayersNeoForge.FAIRY_SUMMON_LAYER, FairySummonModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayersNeoForge.BATTLE_SUMMON_LAYER, BattleSummonModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayersNeoForge.BIRD_SUMMON_LAYER, BirdSummonModel::createBodyLayer);
    }

    @SubscribeEvent
    private static void onRegisterEntityRenderersEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypesNeoForge.QUIETER_LIGHTNING_BOLT.get(), QuieterLightningBoltEntityRendererNeoForge::new);
        event.registerEntityRenderer(ModEntityTypesNeoForge.FAIRY_SUMMON.get(), FairySummonRenderer::new);
        event.registerEntityRenderer(ModEntityTypesNeoForge.BATTLE_SUMMON.get(), BattleSummonRenderer::new);
        event.registerEntityRenderer(ModEntityTypesNeoForge.BIRD_SUMMON.get(), BirdSummonRenderer::new);

        event.registerBlockEntityRenderer(ModBlockEntityTypesNeoForge.SUMMON_TOMBSTONE.get(), SummonTombstoneBlockEntityRendererNeoForge::new);
    }
}
