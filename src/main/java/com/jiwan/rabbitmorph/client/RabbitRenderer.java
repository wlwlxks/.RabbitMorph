package com.jiwan.rabbitmorph.client;

import com.jiwan.rabbitmorph.RabbitData;
import com.jiwan.rabbitmorph.RabbitTransform;
import com.jiwan.rabbitmorph.model.ModelRabbitPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RabbitRenderer {

    private static final ResourceLocation RABBIT_TEXTURE = new ResourceLocation("textures/entity/rabbit/white.png");
    private final ModelRabbitPlayer rabbitModel = new ModelRabbitPlayer();

    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;
        if (player == null) return;

        if (!RabbitData.isRabbit(player)) return;

        event.setCanceled(true);

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        double x = event.x;
        double y = event.y;
        double z = event.z;

        float partialTicks = event.partialRenderTick;
        float renderYaw = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        float rotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        float rotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        float headYaw = rotationYaw - renderYaw;

        float limbSwing = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);
        float limbSwingAmount = player.prevLimbSwingAmount + (player.limbSwingAmount - player.prevLimbSwingAmount) * partialTicks;
        float ageInTicks = player.ticksExisted + partialTicks;

        float scaleFactor = RabbitTransform.getScaleFactor(player);
        if (scaleFactor <= 0.0F) scaleFactor = 1.0F;

        float overallScale = RabbitData.getScale(player, "overall");

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - renderYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);

        float rabbitScale = 0.65F * scaleFactor * overallScale;
        GlStateManager.scale(rabbitScale, rabbitScale, rabbitScale);
        GlStateManager.translate(0.0F, -1.5F, 0.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(RABBIT_TEXTURE);

        float sHead = RabbitData.getScale(player, "head");
        float sEar = RabbitData.getScale(player, "ear");
        float sBody = RabbitData.getScale(player, "body");
        float sLegs = RabbitData.getScale(player, "legs");
        float sTail = RabbitData.getScale(player, "tail");

        int bR = RabbitData.color(player, "body", "R"), bG = RabbitData.color(player, "body", "G"), bB = RabbitData.color(player, "body", "B"), bA = RabbitData.color(player, "body", "A");
        int eR = RabbitData.color(player, "ear", "R"), eG = RabbitData.color(player, "ear", "G"), eB = RabbitData.color(player, "ear", "B"), eA = RabbitData.color(player, "ear", "A");
        int eyR = RabbitData.color(player, "eye", "R"), eyG = RabbitData.color(player, "eye", "G"), eyB = RabbitData.color(player, "eye", "B"), eyA = RabbitData.color(player, "eye", "A");
        int tR = RabbitData.color(player, "tail", "R"), tG = RabbitData.color(player, "tail", "G"), tB = RabbitData.color(player, "tail", "B"), tA = RabbitData.color(player, "tail", "A");

        this.rabbitModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, rotationPitch, 0.0625F, player);
        this.rabbitModel.renderColoredDetailed(0.0625F, sHead, sEar, sBody, sLegs, sTail,
                bR, bG, bB, bA, eR, eG, eB, eA, eyR, eyG, eyB, eyA, tR, tG, tB, tA);

        GlStateManager.popMatrix();
    }
}
