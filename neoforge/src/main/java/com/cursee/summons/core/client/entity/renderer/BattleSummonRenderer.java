package com.cursee.summons.core.client.entity.renderer;

import com.cursee.summons.Constants;
import com.cursee.summons.core.client.entity.model.BattleSummonModel;
import com.cursee.summons.core.common.entity.custom.BattleSummonEntity;
import com.cursee.summons.core.common.registry.ModModelLayersNeoForge;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BattleSummonRenderer extends MobRenderer<BattleSummonEntity, BattleSummonModel<BattleSummonEntity>> {

    public BattleSummonRenderer(EntityRendererProvider.Context context) {
        super(context, new BattleSummonModel<>(context.bakeLayer(ModModelLayersNeoForge.BATTLE_SUMMON_LAYER)), 1.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(BattleSummonEntity p_114482_) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/magmard.png");
    }
}
