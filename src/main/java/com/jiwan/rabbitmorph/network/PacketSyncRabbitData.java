package com.jiwan.rabbitmorph.network;

import com.jiwan.rabbitmorph.RabbitData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketSyncRabbitData implements IMessage {

    private UUID playerUUID;
    private boolean isRabbit;
    private String type;
    private double health;
    private double speed;
    private double jump;
    private double fall;

    private int bodyR, bodyG, bodyB;
    private int earR, earG, earB;
    private int eyeR, eyeG, eyeB;
    private int tailR, tailG, tailB;

    public PacketSyncRabbitData() {
    }

    public static PacketSyncRabbitData createForPlayer(EntityPlayer player) {
        PacketSyncRabbitData pkt = new PacketSyncRabbitData();
        pkt.playerUUID = player.getUniqueID();
        pkt.isRabbit = RabbitData.isRabbit(player);
        pkt.type = RabbitData.getType(player);
        pkt.health = RabbitData.getHealth(player);
        pkt.speed = RabbitData.getSpeed(player);
        pkt.jump = RabbitData.getJump(player);
        pkt.fall = RabbitData.getFallDamage(player);

        pkt.bodyR = RabbitData.color(player, "body", "R");
        pkt.bodyG = RabbitData.color(player, "body", "G");
        pkt.bodyB = RabbitData.color(player, "body", "B");

        pkt.earR = RabbitData.color(player, "ear", "R");
        pkt.earG = RabbitData.color(player, "ear", "G");
        pkt.earB = RabbitData.color(player, "ear", "B");

        pkt.eyeR = RabbitData.color(player, "eye", "R");
        pkt.eyeG = RabbitData.color(player, "eye", "G");
        pkt.eyeB = RabbitData.color(player, "eye", "B");

        pkt.tailR = RabbitData.color(player, "tail", "R");
        pkt.tailG = RabbitData.color(player, "tail", "G");
        pkt.tailB = RabbitData.color(player, "tail", "B");

        return pkt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        long most = buf.readLong();
        long least = buf.readLong();
        this.playerUUID = new UUID(most, least);
        this.isRabbit = buf.readBoolean();
        this.type = ByteBufUtils.readUTF8String(buf);
        this.health = buf.readDouble();
        this.speed = buf.readDouble();
        this.jump = buf.readDouble();
        this.fall = buf.readDouble();

        this.bodyR = buf.readInt(); this.bodyG = buf.readInt(); this.bodyB = buf.readInt();
        this.earR = buf.readInt(); this.earG = buf.readInt(); this.earB = buf.readInt();
        this.eyeR = buf.readInt(); this.eyeG = buf.readInt(); this.eyeB = buf.readInt();
        this.tailR = buf.readInt(); this.tailG = buf.readInt(); this.tailB = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.playerUUID.getMostSignificantBits());
        buf.writeLong(this.playerUUID.getLeastSignificantBits());
        buf.writeBoolean(this.isRabbit);
        ByteBufUtils.writeUTF8String(buf, this.type != null ? this.type : "");
        buf.writeDouble(this.health);
        buf.writeDouble(this.speed);
        buf.writeDouble(this.jump);
        buf.writeDouble(this.fall);

        buf.writeInt(this.bodyR); buf.writeInt(this.bodyG); buf.writeInt(this.bodyB);
        buf.writeInt(this.earR); buf.writeInt(this.earG); buf.writeInt(this.earB);
        buf.writeInt(this.eyeR); buf.writeInt(this.eyeG); buf.writeInt(this.eyeB);
        buf.writeInt(this.tailR); buf.writeInt(this.tailG); buf.writeInt(this.tailB);
    }

    public static class Handler implements IMessageHandler<PacketSyncRabbitData, IMessage> {
        @Override
        public IMessage onMessage(final PacketSyncRabbitData message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer clientPlayer = Minecraft.getMinecraft().theWorld != null ?
                            Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(message.playerUUID) : null;

                    RabbitData.syncClientCache(message.playerUUID, message.isRabbit, message.type,
                            message.health, message.speed, message.jump, message.fall,
                            message.bodyR, message.bodyG, message.bodyB,
                            message.earR, message.earG, message.earB,
                            message.eyeR, message.eyeG, message.eyeB,
                            message.tailR, message.tailG, message.tailB);

                    if (clientPlayer != null) {
                        RabbitData.setRabbit(clientPlayer, message.isRabbit);
                        RabbitData.setType(clientPlayer, message.type);
                        RabbitData.setHealth(clientPlayer, message.health);
                        RabbitData.setSpeed(clientPlayer, message.speed);
                        RabbitData.setJump(clientPlayer, message.jump);
                        RabbitData.setFallDamage(clientPlayer, message.fall);

                        RabbitData.setColor(clientPlayer, "body", message.bodyR, message.bodyG, message.bodyB);
                        RabbitData.setColor(clientPlayer, "ear", message.earR, message.earG, message.earB);
                        RabbitData.setColor(clientPlayer, "eye", message.eyeR, message.eyeG, message.eyeB);
                        RabbitData.setColor(clientPlayer, "tail", message.tailR, message.tailG, message.tailB);
                    }
                }
            });

            return null;
        }
    }
}
