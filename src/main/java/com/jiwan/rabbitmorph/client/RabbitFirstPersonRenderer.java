package com.jiwan.rabbitmorph.client;

import com.jiwan.rabbitmorph.RabbitData;
import com.jiwan.rabbitmorph.model.ModelRabbitHands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RabbitFirstPersonRenderer {

    private static final ResourceLocation RABBIT_TEXTURE = new ResourceLocation("textures/entity/rabbit/white.png");
    private final ModelRabbitHands handsModel = new ModelRabbitHands();

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        if (!RabbitData.isRabbit(player)) return;

        // Cancel default human arm rendering
        event.setCanceled(true);

        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        RenderHelper.enableStandardItemLighting();

        Minecraft.getMinecraft().getTextureManager().bindTexture(RABBIT_TEXTURE);

        int bodyR = RabbitData.color(player, "body", "R");
        int bodyG = RabbitData.color(player, "body", "G");
        int bodyB = RabbitData.color(player, "body", "B");

        float swingProgress = player.getSwingProgress(event.partialTicks);
        float ticksExisted = player.ticksExisted + event.partialTicks;

        // Position rabbit paws in front of camera
        GlStateManager.translate(0.0F, -0.35F, -0.5F);
        GlStateManager.scale(0.85F, 0.85F, 0.85F);

        this.handsModel.renderHands(swingProgress, 0.0F, ticksExisted, bodyR, bodyG, bodyB);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
