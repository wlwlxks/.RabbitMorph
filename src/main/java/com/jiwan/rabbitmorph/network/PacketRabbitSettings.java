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
    private boolean isGlowing;
    private double health, speed, jump, fall;
    private float scaleOverall, scaleHead, scaleEar, scaleBody, scaleLegs, scaleTail;
    private int bodyR, bodyG, bodyB, bodyA;
    private int earR, earG, earB, earA;
    private int eyeR, eyeG, eyeB, eyeA;
    private int tailR, tailG, tailB, tailA;

    public PacketRabbitSettings() {}

    public PacketRabbitSettings(String type, boolean isGlowing, double health, double speed, double jump, double fall,
                                float scaleOverall, float scaleHead, float scaleEar, float scaleBody, float scaleLegs, float scaleTail,
                                int bodyR, int bodyG, int bodyB, int bodyA,
                                int earR, int earG, int earB, int earA,
                                int eyeR, int eyeG, int eyeB, int eyeA,
                                int tailR, int tailG, int tailB, int tailA) {
        this.type = type;
        this.isGlowing = isGlowing;
        this.health = health; this.speed = speed; this.jump = jump; this.fall = fall;
        this.scaleOverall = scaleOverall; this.scaleHead = scaleHead; this.scaleEar = scaleEar;
        this.scaleBody = scaleBody; this.scaleLegs = scaleLegs; this.scaleTail = scaleTail;
        this.bodyR = bodyR; this.bodyG = bodyG; this.bodyB = bodyB; this.bodyA = bodyA;
        this.earR = earR; this.earG = earG; this.earB = earB; this.earA = earA;
        this.eyeR = eyeR; this.eyeG = eyeG; this.eyeB = eyeB; this.eyeA = eyeA;
        this.tailR = tailR; this.tailG = tailG; this.tailB = tailB; this.tailA = tailA;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = ByteBufUtils.readUTF8String(buf);
        this.isGlowing = buf.readBoolean();
        this.health = buf.readDouble(); this.speed = buf.readDouble(); this.jump = buf.readDouble(); this.fall = buf.readDouble();
        this.scaleOverall = buf.readFloat(); this.scaleHead = buf.readFloat(); this.scaleEar = buf.readFloat();
        this.scaleBody = buf.readFloat(); this.scaleLegs = buf.readFloat(); this.scaleTail = buf.readFloat();
        this.bodyR = buf.readInt(); this.bodyG = buf.readInt(); this.bodyB = buf.readInt(); this.bodyA = buf.readInt();
        this.earR = buf.readInt(); this.earG = buf.readInt(); this.earB = buf.readInt(); this.earA = buf.readInt();
        this.eyeR = buf.readInt(); this.eyeG = buf.readInt(); this.eyeB = buf.readInt(); this.eyeA = buf.readInt();
        this.tailR = buf.readInt(); this.tailG = buf.readInt(); this.tailB = buf.readInt(); this.tailA = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.type != null ? this.type : RabbitConfig.TYPE_NORMAL);
        buf.writeBoolean(this.isGlowing);
        buf.writeDouble(this.health); buf.writeDouble(this.speed); buf.writeDouble(this.jump); buf.writeDouble(this.fall);
        buf.writeFloat(this.scaleOverall); buf.writeFloat(this.scaleHead); buf.writeFloat(this.scaleEar);
        buf.writeFloat(this.scaleBody); buf.writeFloat(this.scaleLegs); buf.writeFloat(this.scaleTail);
        buf.writeInt(this.bodyR); buf.writeInt(this.bodyG); buf.writeInt(this.bodyB); buf.writeInt(this.bodyA);
        buf.writeInt(this.earR); buf.writeInt(this.earG); buf.writeInt(this.earB); buf.writeInt(this.earA);
        buf.writeInt(this.eyeR); buf.writeInt(this.eyeG); buf.writeInt(this.eyeB); buf.writeInt(this.eyeA);
        buf.writeInt(this.tailR); buf.writeInt(this.tailG); buf.writeInt(this.tailB); buf.writeInt(this.tailA);
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
                    String validatedType = message.type != null ? message.type : RabbitConfig.TYPE_NORMAL;
                    double validHealth = RabbitUtils.clampDouble(message.health, 1.0D, 1000.0D, RabbitConfig.DEFAULT_HEALTH);
                    double validSpeed = RabbitUtils.clampDouble(message.speed, 0.01D, 5.0D, RabbitConfig.DEFAULT_SPEED);
                    double validJump = RabbitUtils.clampDouble(message.jump, 0.1D, 5.0D, RabbitConfig.DEFAULT_JUMP);
                    double validFall = RabbitUtils.clampDouble(message.fall, 0.0D, 10.0D, RabbitConfig.DEFAULT_FALL);

                    RabbitData.setType(player, validatedType);
                    RabbitData.setGlowing(player, message.isGlowing);
                    RabbitData.setHealth(player, validHealth);
                    RabbitData.setSpeed(player, validSpeed);
                    RabbitData.setJump(player, validJump);
                    RabbitData.setFallDamage(player, validFall);

                    RabbitData.setScale(player, "overall", message.scaleOverall);
                    RabbitData.setScale(player, "head", message.scaleHead);
                    RabbitData.setScale(player, "ear", message.scaleEar);
                    RabbitData.setScale(player, "body", message.scaleBody);
                    RabbitData.setScale(player, "legs", message.scaleLegs);
                    RabbitData.setScale(player, "tail", message.scaleTail);

                    RabbitData.setColorRGBA(player, "body", message.bodyR, message.bodyG, message.bodyB, message.bodyA);
                    RabbitData.setColorRGBA(player, "ear", message.earR, message.earG, message.earB, message.earA);
                    RabbitData.setColorRGBA(player, "eye", message.eyeR, message.eyeG, message.eyeB, message.eyeA);
                    RabbitData.setColorRGBA(player, "tail", message.tailR, message.tailG, message.tailB, message.tailA);

                    if (RabbitData.isRabbit(player)) {
                        RabbitAttributes.applyAttributes(player);
                    }

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
