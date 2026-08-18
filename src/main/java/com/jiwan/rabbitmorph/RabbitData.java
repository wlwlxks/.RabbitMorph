package com.jiwan.rabbitmorph;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RabbitData implements IExtendedEntityProperties {

    public static final String PROP_NAME = "RabbitMorphData";

    private boolean rabbit = false;
    private String type = RabbitConfig.TYPE_NORMAL;
    private double health = RabbitConfig.DEFAULT_HEALTH;
    private double speed = RabbitConfig.DEFAULT_SPEED;
    private double jump = RabbitConfig.DEFAULT_JUMP;
    private double fallDamage = RabbitConfig.DEFAULT_FALL;

    // Scales
    private float scaleOverall = 1.0F;
    private float scaleHead = 1.0F;
    private float scaleEar = 1.0F;
    private float scaleBody = 1.0F;
    private float scaleLegs = 1.0F;
    private float scaleTail = 1.0F;

    // RGBA (Including Alpha)
    private int bodyR = RabbitConfig.NORMAL_BODY_R, bodyG = RabbitConfig.NORMAL_BODY_G, bodyB = RabbitConfig.NORMAL_BODY_B, bodyA = 255;
    private int earR = RabbitConfig.NORMAL_EAR_R, earG = RabbitConfig.NORMAL_EAR_G, earB = RabbitConfig.NORMAL_EAR_B, earA = 255;
    private int eyeR = RabbitConfig.NORMAL_EYE_R, eyeG = RabbitConfig.NORMAL_EYE_G, eyeB = RabbitConfig.NORMAL_EYE_B, eyeA = 255;
    private int tailR = RabbitConfig.NORMAL_TAIL_R, tailG = RabbitConfig.NORMAL_TAIL_G, tailB = RabbitConfig.NORMAL_TAIL_B, tailA = 255;

    private static final Map<UUID, RabbitDataHolder> CLIENT_CACHE = new HashMap<UUID, RabbitDataHolder>();

    public static class RabbitDataHolder {
        public boolean rabbit;
        public String type = RabbitConfig.TYPE_NORMAL;
        public double health = RabbitConfig.DEFAULT_HEALTH;
        public double speed = RabbitConfig.DEFAULT_SPEED;
        public double jump = RabbitConfig.DEFAULT_JUMP;
        public double fallDamage = RabbitConfig.DEFAULT_FALL;

        public float scaleOverall = 1.0F, scaleHead = 1.0F, scaleEar = 1.0F, scaleBody = 1.0F, scaleLegs = 1.0F, scaleTail = 1.0F;

        public int bodyR = RabbitConfig.NORMAL_BODY_R, bodyG = RabbitConfig.NORMAL_BODY_G, bodyB = RabbitConfig.NORMAL_BODY_B, bodyA = 255;
        public int earR = RabbitConfig.NORMAL_EAR_R, earG = RabbitConfig.NORMAL_EAR_G, earB = RabbitConfig.NORMAL_EAR_B, earA = 255;
        public int eyeR = RabbitConfig.NORMAL_EYE_R, eyeG = RabbitConfig.NORMAL_EYE_G, eyeB = RabbitConfig.NORMAL_EYE_B, eyeA = 255;
        public int tailR = RabbitConfig.NORMAL_TAIL_R, tailG = RabbitConfig.NORMAL_TAIL_G, tailB = RabbitConfig.NORMAL_TAIL_B, tailA = 255;
    }

    public static void register(EntityPlayer player) {
        if (player.getExtendedProperties(PROP_NAME) == null) {
            player.registerExtendedProperties(PROP_NAME, new RabbitData());
        }
    }

    public static RabbitData get(EntityPlayer player) {
        if (player == null) return null;
        RabbitData data = (RabbitData) player.getExtendedProperties(PROP_NAME);
        if (data == null) {
            register(player);
            data = (RabbitData) player.getExtendedProperties(PROP_NAME);
        }
        return data;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("IsRabbit", this.rabbit);
        tag.setString("Type", this.type != null ? this.type : RabbitConfig.TYPE_NORMAL);
        tag.setDouble("Health", this.health);
        tag.setDouble("Speed", this.speed);
        tag.setDouble("Jump", this.jump);
        tag.setDouble("FallDamage", this.fallDamage);

        tag.setFloat("ScaleOverall", this.scaleOverall);
        tag.setFloat("ScaleHead", this.scaleHead);
        tag.setFloat("ScaleEar", this.scaleEar);
        tag.setFloat("ScaleBody", this.scaleBody);
        tag.setFloat("ScaleLegs", this.scaleLegs);
        tag.setFloat("ScaleTail", this.scaleTail);

        tag.setInteger("BodyR", this.bodyR); tag.setInteger("BodyG", this.bodyG); tag.setInteger("BodyB", this.bodyB); tag.setInteger("BodyA", this.bodyA);
        tag.setInteger("EarR", this.earR); tag.setInteger("EarG", this.earG); tag.setInteger("EarB", this.earB); tag.setInteger("EarA", this.earA);
        tag.setInteger("EyeR", this.eyeR); tag.setInteger("EyeG", this.eyeG); tag.setInteger("EyeB", this.eyeB); tag.setInteger("EyeA", this.eyeA);
        tag.setInteger("TailR", this.tailR); tag.setInteger("TailG", this.tailG); tag.setInteger("TailB", this.tailB); tag.setInteger("TailA", this.tailA);

        compound.setTag(PROP_NAME, tag);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey(PROP_NAME)) {
            NBTTagCompound tag = compound.getCompoundTag(PROP_NAME);
            this.rabbit = tag.getBoolean("IsRabbit");
            this.type = tag.getString("Type");
            this.health = tag.getDouble("Health");
            this.speed = tag.getDouble("Speed");
            this.jump = tag.getDouble("Jump");
            this.fallDamage = tag.getDouble("FallDamage");

            this.scaleOverall = tag.hasKey("ScaleOverall") ? tag.getFloat("ScaleOverall") : 1.0F;
            this.scaleHead = tag.hasKey("ScaleHead") ? tag.getFloat("ScaleHead") : 1.0F;
            this.scaleEar = tag.hasKey("ScaleEar") ? tag.getFloat("ScaleEar") : 1.0F;
            this.scaleBody = tag.hasKey("ScaleBody") ? tag.getFloat("ScaleBody") : 1.0F;
            this.scaleLegs = tag.hasKey("ScaleLegs") ? tag.getFloat("ScaleLegs") : 1.0F;
            this.scaleTail = tag.hasKey("ScaleTail") ? tag.getFloat("ScaleTail") : 1.0F;

            this.bodyR = tag.getInteger("BodyR"); this.bodyG = tag.getInteger("BodyG"); this.bodyB = tag.getInteger("BodyB");
            this.bodyA = tag.hasKey("BodyA") ? tag.getInteger("BodyA") : 255;

            this.earR = tag.getInteger("EarR"); this.earG = tag.getInteger("EarG"); this.earB = tag.getInteger("EarB");
            this.earA = tag.hasKey("EarA") ? tag.getInteger("EarA") : 255;

            this.eyeR = tag.getInteger("EyeR"); this.eyeG = tag.getInteger("EyeG"); this.eyeB = tag.getInteger("EyeB");
            this.eyeA = tag.hasKey("EyeA") ? tag.getInteger("EyeA") : 255;

            this.tailR = tag.getInteger("TailR"); this.tailG = tag.getInteger("TailG"); this.tailB = tag.getInteger("TailB");
            this.tailA = tag.hasKey("TailA") ? tag.getInteger("TailA") : 255;
        }
    }

    @Override
    public void init(Entity entity, World world) {}

    public static boolean isRabbit(EntityPlayer player) {
        RabbitData data = get(player);
        if (data != null) return data.rabbit;
        if (player != null) {
            RabbitDataHolder holder = CLIENT_CACHE.get(player.getUniqueID());
            if (holder != null) return holder.rabbit;
        }
        return false;
    }

    public static void setRabbit(EntityPlayer player, boolean isRabbit) {
        RabbitData data = get(player);
        if (data != null) data.rabbit = isRabbit;
        if (player != null) {
            RabbitDataHolder holder = getOrCreateCache(player.getUniqueID());
            holder.rabbit = isRabbit;
        }
    }

    public static String getType(EntityPlayer player) {
        RabbitData data = get(player);
        if (data != null) return data.type;
        if (player != null) {
            RabbitDataHolder holder = CLIENT_CACHE.get(player.getUniqueID());
            if (holder != null) return holder.type;
        }
        return RabbitConfig.TYPE_NORMAL;
    }

    public static void setType(EntityPlayer player, String type) {
        RabbitData data = get(player);
        if (data != null) data.type = type;
        if (player != null) {
            RabbitDataHolder holder = getOrCreateCache(player.getUniqueID());
            holder.type = type;
        }
    }

    public static double getHealth(EntityPlayer player) {
        RabbitData data = get(player);
        if (data != null) return data.health;
        return RabbitConfig.DEFAULT_HEALTH;
    }

    public static void setHealth(EntityPlayer player, double health) {
        RabbitData data = get(player);
        if (data != null) data.health = RabbitUtils.clampDouble(health, 1.0D, 1000.0D, RabbitConfig.DEFAULT_HEALTH);
    }

    public static double getSpeed(EntityPlayer player) {
        RabbitData data = get(player);
        if (data != null) return data.speed;
        return RabbitConfig.DEFAULT_SPEED;
    }

    public static void setSpeed(EntityPlayer player, double speed) {
        RabbitData data = get(player);
        if (data != null) data.speed = RabbitUtils.clampDouble(speed, 0.01D, 5.0D, RabbitConfig.DEFAULT_SPEED);
    }

    public static double getJump(EntityPlayer player) {
        RabbitData data = get(player);
        if (data != null) return data.jump;
        return RabbitConfig.DEFAULT_JUMP;
    }

    public static void setJump(EntityPlayer player, double jump) {
        RabbitData data = get(player);
        if (data != null) data.jump = RabbitUtils.clampDouble(jump, 0.1D, 5.0D, RabbitConfig.DEFAULT_JUMP);
    }

    public static double getFallDamage(EntityPlayer player) {
        RabbitData data = get(player);
        if (data != null) return data.fallDamage;
        return RabbitConfig.DEFAULT_FALL;
    }

    public static void setFallDamage(EntityPlayer player, double fall) {
        RabbitData data = get(player);
        if (data != null) data.fallDamage = RabbitUtils.clampDouble(fall, 0.0D, 10.0D, RabbitConfig.DEFAULT_FALL);
    }

    // Scales API
    public static float getScale(EntityPlayer player, String part) {
        RabbitData data = get(player);
        if (data != null) {
            if ("overall".equalsIgnoreCase(part)) return data.scaleOverall;
            if ("head".equalsIgnoreCase(part)) return data.scaleHead;
            if ("ear".equalsIgnoreCase(part)) return data.scaleEar;
            if ("body".equalsIgnoreCase(part)) return data.scaleBody;
            if ("legs".equalsIgnoreCase(part)) return data.scaleLegs;
            if ("tail".equalsIgnoreCase(part)) return data.scaleTail;
        }
        if (player != null) {
            RabbitDataHolder holder = CLIENT_CACHE.get(player.getUniqueID());
            if (holder != null) {
                if ("overall".equalsIgnoreCase(part)) return holder.scaleOverall;
                if ("head".equalsIgnoreCase(part)) return holder.scaleHead;
                if ("ear".equalsIgnoreCase(part)) return holder.scaleEar;
                if ("body".equalsIgnoreCase(part)) return holder.scaleBody;
                if ("legs".equalsIgnoreCase(part)) return holder.scaleLegs;
                if ("tail".equalsIgnoreCase(part)) return holder.scaleTail;
            }
        }
        return 1.0F;
    }

    public static void setScale(EntityPlayer player, String part, float scale) {
        scale = (float) RabbitUtils.clampDouble(scale, 0.2D, 3.0D, 1.0D);
        RabbitData data = get(player);
        if (data != null) {
            if ("overall".equalsIgnoreCase(part)) data.scaleOverall = scale;
            else if ("head".equalsIgnoreCase(part)) data.scaleHead = scale;
            else if ("ear".equalsIgnoreCase(part)) data.scaleEar = scale;
            else if ("body".equalsIgnoreCase(part)) data.scaleBody = scale;
            else if ("legs".equalsIgnoreCase(part)) data.scaleLegs = scale;
            else if ("tail".equalsIgnoreCase(part)) data.scaleTail = scale;
        }
        if (player != null) {
            RabbitDataHolder holder = getOrCreateCache(player.getUniqueID());
            if ("overall".equalsIgnoreCase(part)) holder.scaleOverall = scale;
            else if ("head".equalsIgnoreCase(part)) holder.scaleHead = scale;
            else if ("ear".equalsIgnoreCase(part)) holder.scaleEar = scale;
            else if ("body".equalsIgnoreCase(part)) holder.scaleBody = scale;
            else if ("legs".equalsIgnoreCase(part)) holder.scaleLegs = scale;
            else if ("tail".equalsIgnoreCase(part)) holder.scaleTail = scale;
        }
    }

    // RGBA Color API
    public static int color(EntityPlayer player, String part, String channel) {
        RabbitData data = get(player);
        int defaultR = 255, defaultG = 255, defaultB = 255, defaultA = 255;

        if (data != null) {
            if ("body".equalsIgnoreCase(part)) {
                if ("R".equalsIgnoreCase(channel)) return data.bodyR;
                if ("G".equalsIgnoreCase(channel)) return data.bodyG;
                if ("B".equalsIgnoreCase(channel)) return data.bodyB;
                if ("A".equalsIgnoreCase(channel)) return data.bodyA;
            } else if ("ear".equalsIgnoreCase(part)) {
                if ("R".equalsIgnoreCase(channel)) return data.earR;
                if ("G".equalsIgnoreCase(channel)) return data.earG;
                if ("B".equalsIgnoreCase(channel)) return data.earB;
                if ("A".equalsIgnoreCase(channel)) return data.earA;
            } else if ("eye".equalsIgnoreCase(part)) {
                if ("R".equalsIgnoreCase(channel)) return data.eyeR;
                if ("G".equalsIgnoreCase(channel)) return data.eyeG;
                if ("B".equalsIgnoreCase(channel)) return data.eyeB;
                if ("A".equalsIgnoreCase(channel)) return data.eyeA;
            } else if ("tail".equalsIgnoreCase(part)) {
                if ("R".equalsIgnoreCase(channel)) return data.tailR;
                if ("G".equalsIgnoreCase(channel)) return data.tailG;
                if ("B".equalsIgnoreCase(channel)) return data.tailB;
                if ("A".equalsIgnoreCase(channel)) return data.tailA;
            }
        }
        if (player != null) {
            RabbitDataHolder holder = CLIENT_CACHE.get(player.getUniqueID());
            if (holder != null) {
                if ("body".equalsIgnoreCase(part)) {
                    if ("R".equalsIgnoreCase(channel)) return holder.bodyR;
                    if ("G".equalsIgnoreCase(channel)) return holder.bodyG;
                    if ("B".equalsIgnoreCase(channel)) return holder.bodyB;
                    if ("A".equalsIgnoreCase(channel)) return holder.bodyA;
                } else if ("ear".equalsIgnoreCase(part)) {
                    if ("R".equalsIgnoreCase(channel)) return holder.earR;
                    if ("G".equalsIgnoreCase(channel)) return holder.earG;
                    if ("B".equalsIgnoreCase(channel)) return holder.earB;
                    if ("A".equalsIgnoreCase(channel)) return holder.earA;
                } else if ("eye".equalsIgnoreCase(part)) {
                    if ("R".equalsIgnoreCase(channel)) return holder.eyeR;
                    if ("G".equalsIgnoreCase(channel)) return holder.eyeG;
                    if ("B".equalsIgnoreCase(channel)) return holder.eyeB;
                    if ("A".equalsIgnoreCase(channel)) return holder.eyeA;
                } else if ("tail".equalsIgnoreCase(part)) {
                    if ("R".equalsIgnoreCase(channel)) return holder.tailR;
                    if ("G".equalsIgnoreCase(channel)) return holder.tailG;
                    if ("B".equalsIgnoreCase(channel)) return holder.tailB;
                    if ("A".equalsIgnoreCase(channel)) return holder.tailA;
                }
            }
        }
        return 255;
    }

    public static void setColorRGBA(EntityPlayer player, String part, int r, int g, int b, int a) {
        RabbitData data = get(player);
        r = RabbitUtils.clampColor(r); g = RabbitUtils.clampColor(g); b = RabbitUtils.clampColor(b); a = RabbitUtils.clampColor(a);

        if (data != null) {
            if ("body".equalsIgnoreCase(part)) { data.bodyR = r; data.bodyG = g; data.bodyB = b; data.bodyA = a; }
            else if ("ear".equalsIgnoreCase(part)) { data.earR = r; data.earG = g; data.earB = b; data.earA = a; }
            else if ("eye".equalsIgnoreCase(part)) { data.eyeR = r; data.eyeG = g; data.eyeB = b; data.eyeA = a; }
            else if ("tail".equalsIgnoreCase(part)) { data.tailR = r; data.tailG = g; data.tailB = b; data.tailA = a; }
        }

        if (player != null) {
            RabbitDataHolder holder = getOrCreateCache(player.getUniqueID());
            if ("body".equalsIgnoreCase(part)) { holder.bodyR = r; holder.bodyG = g; holder.bodyB = b; holder.bodyA = a; }
            else if ("ear".equalsIgnoreCase(part)) { holder.earR = r; holder.earG = g; holder.earB = b; holder.earA = a; }
            else if ("eye".equalsIgnoreCase(part)) { holder.eyeR = r; holder.eyeG = g; holder.eyeB = b; holder.eyeA = a; }
            else if ("tail".equalsIgnoreCase(part)) { holder.tailR = r; holder.tailG = g; holder.tailB = b; holder.tailA = a; }
        }
    }

    public static RabbitDataHolder getOrCreateCache(UUID uuid) {
        RabbitDataHolder holder = CLIENT_CACHE.get(uuid);
        if (holder == null) {
            holder = new RabbitDataHolder();
            CLIENT_CACHE.put(uuid, holder);
        }
        return holder;
    }

    public static void syncClientCache(UUID uuid, boolean isRabbit, String type, double health, double speed, double jump, double fall,
                                       float scaleOverall, float scaleHead, float scaleEar, float scaleBody, float scaleLegs, float scaleTail,
                                       int bodyR, int bodyG, int bodyB, int bodyA,
                                       int earR, int earG, int earB, int earA,
                                       int eyeR, int eyeG, int eyeB, int eyeA,
                                       int tailR, int tailG, int tailB, int tailA) {
        RabbitDataHolder holder = getOrCreateCache(uuid);
        holder.rabbit = isRabbit;
        holder.type = type;
        holder.health = health;
        holder.speed = speed;
        holder.jump = jump;
        holder.fallDamage = fall;

        holder.scaleOverall = scaleOverall; holder.scaleHead = scaleHead; holder.scaleEar = scaleEar;
        holder.scaleBody = scaleBody; holder.scaleLegs = scaleLegs; holder.scaleTail = scaleTail;

        holder.bodyR = bodyR; holder.bodyG = bodyG; holder.bodyB = bodyB; holder.bodyA = bodyA;
        holder.earR = earR; holder.earG = earG; holder.earB = earB; holder.earA = earA;
        holder.eyeR = eyeR; holder.eyeG = eyeG; holder.eyeB = eyeB; holder.eyeA = eyeA;
        holder.tailR = tailR; holder.tailG = tailG; holder.tailB = tailB; holder.tailA = tailA;
    }
}
