package com.jiwan.rabbitmorph.client;

import com.jiwan.rabbitmorph.model.ModelRabbitPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RabbitPreviewRenderer {

    private static final ResourceLocation RABBIT_TEXTURE = new ResourceLocation("textures/entity/rabbit/white.png");
    private static final ModelRabbitPlayer previewModel = new ModelRabbitPlayer();

    public static void renderPreview(int x, int y, int width, int height,
                                     float yaw, float pitch, float zoom,
                                     float sOverall, float sHead, float sEar, float sBody, float sLegs, float sTail,
                                     int bR, int bG, int bB, int bA,
                                     int eR, int eG, int eB, int eA,
                                     int eyR, int eyG, int eyB, int eyA,
                                     int tR, int tG, int tB, int tA,
                                     float ticksExisted, int previewPose) {

        GlStateManager.pushMatrix();

        net.minecraft.client.gui.Gui.drawRect(x, y, x + width, y + height, 0xBB000000);
        net.minecraft.client.gui.Gui.drawRect(x, y, x + width, y + 1, 0xFF666666);
        net.minecraft.client.gui.Gui.drawRect(x, y + height - 1, x + width, y + height, 0xFF666666);
        net.minecraft.client.gui.Gui.drawRect(x, y, x + 1, y + height, 0xFF666666);
        net.minecraft.client.gui.Gui.drawRect(x + width - 1, y, x + width, y + height, 0xFF666666);

        int centerX = x + width / 2;
        int centerY = y + height / 2 + 25;

        GlStateManager.translate(centerX, centerY, 150.0F);
        float baseScale = 45.0F * zoom * sOverall;
        GlStateManager.scale(-baseScale, baseScale, baseScale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);

        GlStateManager.enableDepth();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

        Minecraft.getMinecraft().getTextureManager().bindTexture(RABBIT_TEXTURE);

        float limbSwing = 0.0F;
        float limbSwingAmount = 0.0F;

        if (previewPose == 1) { // Walk pose
            limbSwing = ticksExisted * 0.4F;
            limbSwingAmount = 0.8F;
        } else if (previewPose == 2) { // Jump pose
            limbSwing = 1.0F;
            limbSwingAmount = 1.0F;
        }

        previewModel.setRotationAngles(limbSwing, limbSwingAmount, ticksExisted, 0.0F, 0.0F, 0.0625F, null);
        previewModel.renderColoredDetailed(0.0625F, sHead, sEar, sBody, sLegs, sTail,
                bR, bG, bB, bA, eR, eG, eB, eA, eyR, eyG, eyB, eyA, tR, tG, tB, tA);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
}
