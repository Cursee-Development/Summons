package com.cursee.summons.core.client.registry;

import com.cursee.summons.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayersNeoForge {

    public static void register() {}

    public static final ModelLayerLocation FAIRY_SUMMON_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "fairy_summon"), "fairy_summon");
    public static final ModelLayerLocation BATTLE_SUMMON_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "battle_summon"), "battle_summon");
    public static final ModelLayerLocation BIRD_SUMMON_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "bird_summon"), "bird_summon");
}
