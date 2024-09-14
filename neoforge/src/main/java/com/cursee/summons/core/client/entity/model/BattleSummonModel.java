package com.cursee.summons.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class BattleSummonModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart body;

    private final ModelPart torso;

    private final ModelPart head;
    private final ModelPart arm0;
    private final ModelPart arm1;
    private final ModelPart crystal;

    private final ModelPart leg0;
    private final ModelPart leg1;

    public BattleSummonModel(ModelPart root) {

        this.body = root.getChild("body");

        this.torso = this.body.getChild("torso");

        this.head = this.torso.getChild("head");
        this.arm0 = this.torso.getChild("arm0");
        this.arm1 = this.torso.getChild("arm1");
        this.crystal = this.torso.getChild("crystal");

        this.leg0 = this.body.getChild("leg0");
        this.leg1 = this.body.getChild("leg1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 116).addBox(-7.5F, 10.0F, -4.5F, 15.0F, 4.5F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -4.5F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-1.0F, -5.0F, -6.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

        PartDefinition arm0 = torso.addOrReplaceChild("arm0", CubeListBuilder.create().texOffs(60, 21).addBox(-13.0F, 10.1F, -3.0F, 4.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 83).addBox(-14.0F, -2.0F, -5.0F, 5.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition arm1 = torso.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(60, 58).addBox(9.0F, 10.1F, -3.0F, 4.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(100, 107).addBox(9.0F, -2.0F, -5.0F, 5.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition crystal = torso.addOrReplaceChild("crystal", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = crystal.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(108, 87).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 4.5F, 6.0F, -0.3927F, -0.0873F, 0.0F));

        PartDefinition cube_r2 = crystal.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(116, 87).addBox(-1.0F, -5.5F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 4.5F, 6.0F, -0.3927F, 0.0873F, 0.0F));

        PartDefinition cube_r3 = crystal.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(104, 83).addBox(-3.0F, -9.0F, -3.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.357F, 4.8277F, -0.3927F, 0.0F, 0.0F));

        PartDefinition leg0 = body.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(37, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 18.0F, 0.0F));

        PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-3.0F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 18.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);
    }

//    @Override
//    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//    }


    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, int color) {
        this.body.render(stack, consumer, light, overlay, color);
    }

    @Override
    public ModelPart root() {
        return this.body;
    }

    private void applyHeadRotation(float netHeadYaw, float headPitch) {

        netHeadYaw = Mth.clamp(netHeadYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

        this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.head.xRot = headPitch * (float) (Math.PI / 180.0);
    }
}
