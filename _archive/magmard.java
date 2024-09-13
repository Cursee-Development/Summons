// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class magmard<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "magmard"), "main");
	private final ModelPart body;
	private final ModelPart torso;
	private final ModelPart head;
	private final ModelPart arm0;
	private final ModelPart arm1;
	private final ModelPart crystal;
	private final ModelPart leg0;
	private final ModelPart leg1;

	public magmard(ModelPart root) {
		this.body = root.getChild("body");
		this.torso = root.getChild("torso");
		this.head = root.getChild("head");
		this.arm0 = root.getChild("arm0");
		this.arm1 = root.getChild("arm1");
		this.crystal = root.getChild("crystal");
		this.leg0 = root.getChild("leg0");
		this.leg1 = root.getChild("leg1");
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

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}