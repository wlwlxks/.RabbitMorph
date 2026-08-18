package com.jiwan.rabbitmorph.network;

import com.jiwan.rabbitmorph.RabbitAttributes;
import com.jiwan.rabbitmorph.RabbitData;
import com.jiwan.rabbitmorph.RabbitTransform;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggleRabbit implements IMessage {

    public PacketToggleRabbit() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<PacketToggleRabbit, IMessage> {
        @Override
        public IMessage onMessage(PacketToggleRabbit message, MessageContext ctx) {
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (player == null) return null;

            WorldServer world = player.getServerForPlayer();
            world.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    boolean currentState = RabbitData.isRabbit(player);
                    boolean newState = !currentState;
                    RabbitData.setRabbit(player, newState);

                    if (newState) {
                        RabbitAttributes.applyAttributes(player);
                    } else {
                        RabbitAttributes.removeAttributes(player);
                    }

                    RabbitTransform.startTransformation(player, newState);

                    // Sync to all clients tracking this player
                    PacketSyncRabbitData syncPacket = PacketSyncRabbitData.createForPlayer(player);
                    PacketHandler.INSTANCE.sendToAllAround(syncPacket,
                            new net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint(
                                    player.dimension, player.posX, player.posY, player.posZ, 128.0D));
                    PacketHandler.INSTANCE.sendTo(syncPacket, player);
                }
            });

            return null;
        }
    }
}
