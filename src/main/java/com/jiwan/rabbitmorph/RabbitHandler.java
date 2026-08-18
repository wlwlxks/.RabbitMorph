package com.jiwan.rabbitmorph;

import com.jiwan.rabbitmorph.gui.GuiRabbitSettings;
import com.jiwan.rabbitmorph.network.PacketHandler;
import com.jiwan.rabbitmorph.network.PacketOpenInventory;
import com.jiwan.rabbitmorph.network.PacketSyncRabbitData;
import com.jiwan.rabbitmorph.network.PacketToggleRabbit;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RabbitHandler {

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            RabbitData.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.entityPlayer != null && event.original != null) {
            RabbitData oldData = RabbitData.get(event.original);
            RabbitData newData = RabbitData.get(event.entityPlayer);
            if (oldData != null && newData != null) {
                NBTTagCompound nbt = new NBTTagCompound();
                oldData.saveNBTData(nbt);
                newData.loadNBTData(nbt);
            }
            if (RabbitData.isRabbit(event.entityPlayer)) {
                RabbitAttributes.applyAttributes(event.entityPlayer);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            if (RabbitData.isRabbit(player)) {
                RabbitAttributes.applyAttributes(player);
            }
            PacketSyncRabbitData syncPacket = PacketSyncRabbitData.createForPlayer(player);
            PacketHandler.INSTANCE.sendTo(syncPacket, player);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            PacketSyncRabbitData syncPacket = PacketSyncRabbitData.createForPlayer(player);
            PacketHandler.INSTANCE.sendToAllAround(syncPacket,
                    new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(
                            player.dimension, player.posX, player.posY, player.posZ, 128.0D));
        }
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (RabbitData.isRabbit(player)) {
                float fallMultiplier = (float) RabbitData.getFallDamage(player);
                event.distance *= fallMultiplier;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            RabbitTransform.tick();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (RabbitKeys.keyToggleRabbit.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new PacketToggleRabbit());
        }

        if (RabbitKeys.keyOpenSettings.isPressed()) {
            mc.displayGuiScreen(new GuiRabbitSettings());
        }

        if (RabbitKeys.keyInteract.isPressed()) {
            if (mc.thePlayer.isSneaking()) {
                if (!RabbitData.isRabbit(mc.thePlayer)) {
                    mc.thePlayer.addChatMessage(new ChatComponentTranslation("msg.rabbitmorph.must_be_rabbit"));
                    return;
                }
                EntityPlayer target = RabbitUtils.getTargetPlayer(mc.thePlayer, 6.0D);
                if (target != null && RabbitData.isRabbit(target)) {
                    mc.thePlayer.addChatMessage(new ChatComponentTranslation("msg.rabbitmorph.cannot_interact_rabbit"));
                    return;
                }
                PacketHandler.INSTANCE.sendToServer(new PacketOpenInventory());
            }
        }
    }
}
