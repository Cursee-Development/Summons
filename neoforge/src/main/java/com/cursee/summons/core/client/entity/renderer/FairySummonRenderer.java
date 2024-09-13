package com.cursee.summons.core.client.entity.renderer;

import com.cursee.summons.Constants;
import com.cursee.summons.core.client.entity.model.FairySummonModel;
import com.cursee.summons.core.common.entity.custom.FairySummonEntity;
import com.cursee.summons.core.common.registry.ModModelLayersNeoForge;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FairySummonRenderer extends MobRenderer<FairySummonEntity, FairySummonModel<FairySummonEntity>> {

    public FairySummonRenderer(EntityRendererProvider.Context context) {
        super(context, new FairySummonModel<>(context.bakeLayer(ModModelLayersNeoForge.FAIRY_SUMMON_LAYER)), 1.2f);
        this.shadowRadius = 0.5f;
        this.shadowStrength = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(FairySummonEntity p_114482_) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/infant_fairy.png");
    }
}
