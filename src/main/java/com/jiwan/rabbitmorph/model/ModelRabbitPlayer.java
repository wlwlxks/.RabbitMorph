package com.jiwan.rabbitmorph.model;

import com.jiwan.rabbitmorph.RabbitAnimation;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelRabbitPlayer extends ModelBase {

    public ModelRenderer rabbitLeftFoot;
    public ModelRenderer rabbitRightFoot;
    public ModelRenderer rabbitLeftThigh;
    public ModelRenderer rabbitRightThigh;
    public ModelRenderer rabbitBody;
    public ModelRenderer rabbitLeftArm;
    public ModelRenderer rabbitRightArm;
    public ModelRenderer rabbitHead;
    public ModelRenderer rabbitRightEar;
    public ModelRenderer rabbitLeftEar;
    public ModelRenderer rabbitTail;
    public ModelRenderer rabbitNose;
    public ModelRenderer rabbitLeftEye;
    public ModelRenderer rabbitRightEye;

    public ModelRabbitPlayer() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
        this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
        this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.rabbitLeftFoot.mirror = true;

        this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
        this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
        this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);

        this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
        this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
        this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.rabbitLeftThigh.mirror = true;

        this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
        this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
        this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);

        this.rabbitBody = new ModelRenderer(this, 0, 0);
        this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
        this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);

        this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
        this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
        this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
        this.rabbitLeftArm.mirror = true;

        this.rabbitRightArm = new ModelRenderer(this, 0, 15);
        this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
        this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);

        this.rabbitHead = new ModelRenderer(this, 32, 0);
        this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
        this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);

        this.rabbitRightEar = new ModelRenderer(this, 52, 0);
        this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);

        this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
        this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);

        this.rabbitTail = new ModelRenderer(this, 52, 6);
        this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
        this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);

        this.rabbitNose = new ModelRenderer(this, 32, 9);
        this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
        this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);

        this.rabbitLeftEye = new ModelRenderer(this, 32, 11);
        this.rabbitLeftEye.addBox(1.6F, -3.2F, -3.8F, 1, 1, 1);
        this.rabbitLeftEye.setRotationPoint(0.0F, 16.0F, -1.0F);

        this.rabbitRightEye = new ModelRenderer(this, 32, 13);
        this.rabbitRightEye.addBox(-2.6F, -3.2F, -3.8F, 1, 1, 1);
        this.rabbitRightEye.setRotationPoint(0.0F, 16.0F, -1.0F);
    }

    public void renderColoredDetailed(float scale,
                                      float scaleHead, float scaleEar, float scaleBody, float scaleLegs, float scaleTail,
                                      int bodyR, int bodyG, int bodyB, int bodyA,
                                      int earR, int earG, int earB, int earA,
                                      int eyeR, int eyeG, int eyeB, int eyeA,
                                      int tailR, int tailG, int tailB, int tailA) {

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        // Body
        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleBody, scaleBody, scaleBody);
        GlStateManager.color(bodyR / 255.0F, bodyG / 255.0F, bodyB / 255.0F, bodyA / 255.0F);
        this.rabbitBody.render(scale);
        GlStateManager.popMatrix();

        // Head
        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleHead, scaleHead, scaleHead);
        GlStateManager.color(bodyR / 255.0F, bodyG / 255.0F, bodyB / 255.0F, bodyA / 255.0F);
        this.rabbitHead.render(scale);
        this.rabbitNose.render(scale);

        // Eyes
        GlStateManager.color(eyeR / 255.0F, eyeG / 255.0F, eyeB / 255.0F, eyeA / 255.0F);
        this.rabbitLeftEye.render(scale);
        this.rabbitRightEye.render(scale);
        GlStateManager.popMatrix();

        // Ears
        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleEar, scaleEar, scaleEar);
        GlStateManager.color(earR / 255.0F, earG / 255.0F, earB / 255.0F, earA / 255.0F);
        this.rabbitRightEar.render(scale);
        this.rabbitLeftEar.render(scale);
        GlStateManager.popMatrix();

        // Legs
        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleLegs, scaleLegs, scaleLegs);
        GlStateManager.color(bodyR / 255.0F, bodyG / 255.0F, bodyB / 255.0F, bodyA / 255.0F);
        this.rabbitLeftFoot.render(scale);
        this.rabbitRightFoot.render(scale);
        this.rabbitLeftThigh.render(scale);
        this.rabbitRightThigh.render(scale);
        this.rabbitLeftArm.render(scale);
        this.rabbitRightArm.render(scale);
        GlStateManager.popMatrix();

        // Tail
        GlStateManager.pushMatrix();
        GlStateManager.scale(scaleTail, scaleTail, scaleTail);
        GlStateManager.color(tailR / 255.0F, tailG / 255.0F, tailB / 255.0F, tailA / 255.0F);
        this.rabbitTail.render(scale);
        GlStateManager.popMatrix();

        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scaleFactor, Entity entityIn) {
        boolean isJumping = entityIn != null && entityIn.motionY > 0.1D;
        boolean isSneaking = entityIn != null && entityIn.isSneaking();

        RabbitAnimation.RabbitPose pose = RabbitAnimation.calculatePose(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, isJumping, isSneaking);

        this.rabbitHead.rotateAngleX = pose.headPitch;
        this.rabbitHead.rotateAngleY = pose.headYaw;
        this.rabbitNose.rotateAngleX = pose.headPitch;
        this.rabbitNose.rotateAngleY = pose.headYaw;
        this.rabbitLeftEye.rotateAngleX = pose.headPitch;
        this.rabbitLeftEye.rotateAngleY = pose.headYaw;
        this.rabbitRightEye.rotateAngleX = pose.headPitch;
        this.rabbitRightEye.rotateAngleY = pose.headYaw;

        this.rabbitRightEar.rotateAngleX = pose.headPitch + pose.earRightPitch;
        this.rabbitRightEar.rotateAngleY = pose.headYaw;
        this.rabbitLeftEar.rotateAngleX = pose.headPitch + pose.earLeftPitch;
        this.rabbitLeftEar.rotateAngleY = pose.headYaw;

        this.rabbitBody.rotateAngleX = pose.bodyPitch;
        this.rabbitLeftArm.rotateAngleX = pose.frontLegLeftPitch;
        this.rabbitRightArm.rotateAngleX = pose.frontLegRightPitch;
        this.rabbitLeftThigh.rotateAngleX = pose.rearLegLeftPitch;
        this.rabbitRightThigh.rotateAngleX = pose.rearLegRightPitch;
        this.rabbitLeftFoot.rotateAngleX = pose.rearLegLeftPitch;
        this.rabbitRightFoot.rotateAngleX = pose.rearLegRightPitch;

        this.rabbitTail.rotateAngleX = pose.tailPitch;
    }
}
