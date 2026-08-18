package com.jiwan.rabbitmorph.network;

import com.jiwan.rabbitmorph.RabbitData;
import com.jiwan.rabbitmorph.RabbitUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenInventory implements IMessage {

    public PacketOpenInventory() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<PacketOpenInventory, IMessage> {
        @Override
        public IMessage onMessage(PacketOpenInventory message, MessageContext ctx) {
            final EntityPlayerMP requester = ctx.getServerHandler().playerEntity;
            if (requester == null) return null;

            WorldServer world = requester.getServerForPlayer();
            world.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    // Server-side validation 1: Requester MUST be a rabbit
                    if (!RabbitData.isRabbit(requester)) {
                        requester.addChatMessage(new ChatComponentTranslation("msg.rabbitmorph.must_be_rabbit"));
                        return;
                    }

                    // Server-side validation 2: Raytrace target player within 6.0 blocks
                    EntityPlayer target = RabbitUtils.getTargetPlayer(requester, 6.0D);
                    if (target == null || target == requester) {
                        requester.addChatMessage(new ChatComponentTranslation("msg.rabbitmorph.no_target"));
                        return;
                    }

                    // Server-side distance check
                    double distSq = requester.getDistanceSqToEntity(target);
                    if (distSq > 36.0D) { // 6 blocks max
                        return;
                    }

                    // Server-side validation 3: Target MUST NOT be a rabbit (Rabbit to Rabbit restriction)
                    if (RabbitData.isRabbit(target)) {
                        requester.addChatMessage(new ChatComponentTranslation("msg.rabbitmorph.cannot_interact_rabbit"));
                        return;
                    }

                    // Valid: Open target player's inventory safely without duplicating/deleting items
                    requester.displayGUIChest(target.inventory);
                }
            });

            return null;
        }
    }
}
