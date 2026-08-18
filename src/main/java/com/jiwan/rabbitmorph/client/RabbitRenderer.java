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

        // Cancel default human player rendering
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

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - renderYaw, 0.0F, 1.0F, 0.0F);

        // Adjust scale for rabbit size & transition animation
        float rabbitScale = 0.65F * scaleFactor;
        GlStateManager.scale(rabbitScale, rabbitScale, rabbitScale);
        GlStateManager.translate(0.0F, 1.35F, 0.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(RABBIT_TEXTURE);

        int bodyR = RabbitData.color(player, "body", "R");
        int bodyG = RabbitData.color(player, "body", "G");
        int bodyB = RabbitData.color(player, "body", "B");

        int earR = RabbitData.color(player, "ear", "R");
        int earG = RabbitData.color(player, "ear", "G");
        int earB = RabbitData.color(player, "ear", "B");

        int eyeR = RabbitData.color(player, "eye", "R");
        int eyeG = RabbitData.color(player, "eye", "G");
        int eyeB = RabbitData.color(player, "eye", "B");

        int tailR = RabbitData.color(player, "tail", "R");
        int tailG = RabbitData.color(player, "tail", "G");
        int tailB = RabbitData.color(player, "tail", "B");

        this.rabbitModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, rotationPitch, 0.0625F, player);
        this.rabbitModel.renderColored(0.0625F, bodyR, bodyG, bodyB, earR, earG, earB, eyeR, eyeG, eyeB, tailR, tailG, tailB);

        GlStateManager.popMatrix();
    }
}
