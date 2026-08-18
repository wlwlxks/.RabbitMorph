package com.jiwan.rabbitmorph.network;

import com.jiwan.rabbitmorph.RabbitAttributes;
import com.jiwan.rabbitmorph.RabbitConfig;
import com.jiwan.rabbitmorph.RabbitData;
import com.jiwan.rabbitmorph.RabbitUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRabbitSettings implements IMessage {

    private String type;
    private double health;
    private double speed;
    private double jump;
    private double fall;

    private int bodyR, bodyG, bodyB;
    private int earR, earG, earB;
    private int eyeR, eyeG, eyeB;
    private int tailR, tailG, tailB;

    public PacketRabbitSettings() {
    }

    public PacketRabbitSettings(String type, double health, double speed, double jump, double fall,
                                int bodyR, int bodyG, int bodyB,
                                int earR, int earG, int earB,
                                int eyeR, int eyeG, int eyeB,
                                int tailR, int tailG, int tailB) {
        this.type = type;
        this.health = health;
        this.speed = speed;
        this.jump = jump;
        this.fall = fall;
        this.bodyR = bodyR; this.bodyG = bodyG; this.bodyB = bodyB;
        this.earR = earR; this.earG = earG; this.earB = earB;
        this.eyeR = eyeR; this.eyeG = eyeG; this.eyeB = eyeB;
        this.tailR = tailR; this.tailG = tailG; this.tailB = tailB;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
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
        ByteBufUtils.writeUTF8String(buf, this.type != null ? this.type : RabbitConfig.TYPE_NORMAL);
        buf.writeDouble(this.health);
        buf.writeDouble(this.speed);
        buf.writeDouble(this.jump);
        buf.writeDouble(this.fall);

        buf.writeInt(this.bodyR); buf.writeInt(this.bodyG); buf.writeInt(this.bodyB);
        buf.writeInt(this.earR); buf.writeInt(this.earG); buf.writeInt(this.earB);
        buf.writeInt(this.eyeR); buf.writeInt(this.eyeG); buf.writeInt(this.eyeB);
        buf.writeInt(this.tailR); buf.writeInt(this.tailG); buf.writeInt(this.tailB);
    }

    public static class Handler implements IMessageHandler<PacketRabbitSettings, IMessage> {
        @Override
        public IMessage onMessage(final PacketRabbitSettings message, MessageContext ctx) {
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (player == null) return null;

            WorldServer world = player.getServerForPlayer();
            world.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    // Server validation
                    String validatedType = message.type != null ? message.type : RabbitConfig.TYPE_NORMAL;
                    double validHealth = RabbitUtils.clampDouble(message.health, 1.0D, 1000.0D, RabbitConfig.DEFAULT_HEALTH);
                    double validSpeed = RabbitUtils.clampDouble(message.speed, 0.01D, 5.0D, RabbitConfig.DEFAULT_SPEED);
                    double validJump = RabbitUtils.clampDouble(message.jump, 0.1D, 5.0D, RabbitConfig.DEFAULT_JUMP);
                    double validFall = RabbitUtils.clampDouble(message.fall, 0.0D, 10.0D, RabbitConfig.DEFAULT_FALL);

                    RabbitData.setType(player, validatedType);
                    RabbitData.setHealth(player, validHealth);
                    RabbitData.setSpeed(player, validSpeed);
                    RabbitData.setJump(player, validJump);
                    RabbitData.setFallDamage(player, validFall);

                    RabbitData.setColor(player, "body", message.bodyR, message.bodyG, message.bodyB);
                    RabbitData.setColor(player, "ear", message.earR, message.earG, message.earB);
                    RabbitData.setColor(player, "eye", message.eyeR, message.eyeG, message.eyeB);
                    RabbitData.setColor(player, "tail", message.tailR, message.tailG, message.tailB);

                    if (RabbitData.isRabbit(player)) {
                        RabbitAttributes.applyAttributes(player);
                    }

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
