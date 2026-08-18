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
                                     int bodyR, int bodyG, int bodyB,
                                     int earR, int earG, int earB,
                                     int eyeR, int eyeG, int eyeB,
                                     int tailR, int tailG, int tailB,
                                     float ticksExisted) {

        GlStateManager.pushMatrix();

        // Draw background box for preview
        net.minecraft.client.gui.Gui.drawRect(x, y, x + width, y + height, 0xAA000000);
        net.minecraft.client.gui.Gui.drawRect(x, y, x + width, y + 1, 0xFF555555);
        net.minecraft.client.gui.Gui.drawRect(x, y + height - 1, x + width, y + height, 0xFF555555);
        net.minecraft.client.gui.Gui.drawRect(x, y, x + 1, y + height, 0xFF555555);
        net.minecraft.client.gui.Gui.drawRect(x + width - 1, y, x + width, y + height, 0xFF555555);

        int centerX = x + width / 2;
        int centerY = y + height / 2 + 25;

        GlStateManager.translate(centerX, centerY, 150.0F);
        float baseScale = 45.0F * zoom;
        GlStateManager.scale(-baseScale, baseScale, baseScale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);

        GlStateManager.enableDepth();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

        Minecraft.getMinecraft().getTextureManager().bindTexture(RABBIT_TEXTURE);

        previewModel.setRotationAngles(ticksExisted * 0.05F, 0.1F, ticksExisted, 0.0F, 0.0F, 0.0625F, null);
        previewModel.renderColored(0.0625F, bodyR, bodyG, bodyB, earR, earG, earB, eyeR, eyeG, eyeB, tailR, tailG, tailB);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
}
