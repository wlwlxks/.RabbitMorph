package com.jiwan.rabbitmorph.client;

import com.jiwan.rabbitmorph.model.ModelRabbitPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RabbitPreviewRenderer {

    private static final ResourceLocation RABBIT_TEXTURE = new ResourceLocation("textures/entity/rabbit/white.png");
    private static final ModelRabbitPlayer previewModel = new ModelRabbitPlayer();

    public static final int[] BG_COLORS = new int[] {
            0xFF121216, // Dark Charcoal
            0xFF1A1A24, // Midnight Blue
            0xFF000000, // Pitch Black
            0xFF1E2820, // Forest Dark
            0xAA000000  // Semi-transparent
    };

    public static final String[] BG_NAMES = new String[] {
            "BG: Dark",
            "BG: Midnight",
            "BG: Black",
            "BG: Forest",
            "BG: Trans"
    };

    public static void renderPreview(int x, int y, int width, int height,
                                     float yaw, float pitch, float zoom,
                                     float sOverall, float sHead, float sEar, float sBody, float sLegs, float sTail,
                                     int bR, int bG, int bB, int bA,
                                     int eR, int eG, int eB, int eA,
                                     int eyR, int eyG, int eyB, int eyA,
                                     int tR, int tG, int tB, int tA,
                                     float ticksExisted, int previewPose, int bgIndex) {

        GlStateManager.pushMatrix();

        int bgColor = BG_COLORS[Math.abs(bgIndex) % BG_COLORS.length];

        // Background frame & shadow box
        net.minecraft.client.gui.Gui.drawRect(x, y, x + width, y + height, bgColor);
        net.minecraft.client.gui.Gui.drawRect(x, y, x + width, y + 1, 0xFF445566);
        net.minecraft.client.gui.Gui.drawRect(x, y + height - 1, x + width, y + height, 0xFF445566);
        net.minecraft.client.gui.Gui.drawRect(x, y, x + 1, y + height, 0xFF445566);
        net.minecraft.client.gui.Gui.drawRect(x + width - 1, y, x + width, y + height, 0xFF445566);

        int centerX = x + width / 2;
        int centerY = y + height / 2 + (int)(15 * zoom);

        GlStateManager.translate((float)centerX, (float)centerY, 200.0F);

        float baseScale = 45.0F * zoom * sOverall;

        // In GUI screen space (Y positive = downwards):
        // Model Y=0 is head (top), Model Y=24 is feet (bottom).
        // Scaling Y by +baseScale places head at top, feet at bottom (100% upright!).
        GlStateManager.scale(-baseScale, baseScale, baseScale);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F); // Face camera (head towards screen)

        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);

        GlStateManager.translate(0.0F, -1.1F, 0.0F); // Offset Y to center rabbit vertically in box

        GlStateManager.enableDepth();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableLighting();
        RenderHelper.enableStandardItemLighting();

        // High brightness lightmap for vivid, sharp, bright rabbit preview
        float lastLightX = OpenGlHelper.lastBrightnessX;
        float lastLightY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(RABBIT_TEXTURE);

        float limbSwing = 0.0F;
        float limbSwingAmount = 0.0F;

        if (previewPose == 1) { // Walk pose
            limbSwing = ticksExisted * 0.4F;
            limbSwingAmount = 0.8F;
        } else if (previewPose == 2) { // Jump pose
            limbSwing = 1.0F;
            limbSwingAmount = 1.0F;
        } else if (previewPose == 3) { // Sneak pose
            limbSwing = 0.0F;
            limbSwingAmount = 0.0F;
        }

        previewModel.setRotationAngles(limbSwing, limbSwingAmount, ticksExisted, 0.0F, 0.0F, 0.0625F, null);
        previewModel.renderColoredDetailed(0.0625F, sHead, sEar, sBody, sLegs, sTail,
                bR, bG, bB, bA, eR, eG, eB, eA, eyR, eyG, eyB, eyA, tR, tG, tB, tA);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastLightX, lastLightY);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableDepth();

        GlStateManager.popMatrix();
    }
}
