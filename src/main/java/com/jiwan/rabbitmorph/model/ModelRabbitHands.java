package com.jiwan.rabbitmorph.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class ModelRabbitHands extends ModelBase {

    public ModelRenderer leftPaw;
    public ModelRenderer rightPaw;

    public ModelRabbitHands() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        // Left front leg paw
        this.leftPaw = new ModelRenderer(this, 8, 15);
        this.leftPaw.addBox(-1.0F, 0.0F, -2.0F, 2, 6, 2);
        this.leftPaw.setRotationPoint(1.8F, 0.0F, 0.0F);
        this.leftPaw.mirror = true;

        // Right front leg paw
        this.rightPaw = new ModelRenderer(this, 0, 15);
        this.rightPaw.addBox(-1.0F, 0.0F, -2.0F, 2, 6, 2);
        this.rightPaw.setRotationPoint(-1.8F, 0.0F, 0.0F);
    }

    public void renderHands(float swingProgress, float equipProgress, float ticksExisted,
                            int bodyR, int bodyG, int bodyB) {

        GlStateManager.pushMatrix();

        float swing = MathHelper.sin(swingProgress * (float) Math.PI);
        float swingAngle = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);

        // Idle floating movement
        float idleY = MathHelper.sin(ticksExisted * 0.1F) * 0.02F;

        GlStateManager.translate(0.0F, idleY, 0.0F);

        // Swing / attack rotation
        if (swingProgress > 0.0F) {
            GlStateManager.rotate(-swingAngle * 40.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(swingAngle * 20.0F, 0.0F, 1.0F, 0.0F);
        }

        GlStateManager.color(bodyR / 255.0F, bodyG / 255.0F, bodyB / 255.0F, 1.0F);
        this.rightPaw.render(0.0625F);
        this.leftPaw.render(0.0625F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.popMatrix();
    }
}
