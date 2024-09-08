package com.cursee.summons.core.client.block.entity.renderer;

import com.cursee.summons.core.common.block.entity.custom.SummonTombstoneBlockEntityNeoForge;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class SummonTombstoneBlockEntityRendererNeoForge implements BlockEntityRenderer<SummonTombstoneBlockEntityNeoForge> {

    public SummonTombstoneBlockEntityRendererNeoForge(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(SummonTombstoneBlockEntityNeoForge tombstone, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        pPoseStack.pushPose();

        // NO-OP

        pPoseStack.popPose();
    }

//    @Override
//    public boolean shouldRender(SummonTombstoneBlockEntityNeoForge tombstone, Vec3 rawPosition) {
//        return BlockEntityRenderer.super.shouldRender(tombstone, rawPosition);
//    }
}
