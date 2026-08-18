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

    private int bodyR = RabbitConfig.NORMAL_BODY_R;
    private int bodyG = RabbitConfig.NORMAL_BODY_G;
    private int bodyB = RabbitConfig.NORMAL_BODY_B;

    private int earR = RabbitConfig.NORMAL_EAR_R;
    private int earG = RabbitConfig.NORMAL_EAR_G;
    private int earB = RabbitConfig.NORMAL_EAR_B;

    private int eyeR = RabbitConfig.NORMAL_EYE_R;
    private int eyeG = RabbitConfig.NORMAL_EYE_G;
    private int eyeB = RabbitConfig.NORMAL_EYE_B;

    private int tailR = RabbitConfig.NORMAL_TAIL_R;
    private int tailG = RabbitConfig.NORMAL_TAIL_G;
    private int tailB = RabbitConfig.NORMAL_TAIL_B;

    // Fallback static map for client side cache by UUID when player object is recreated
    private static final Map<UUID, RabbitDataHolder> CLIENT_CACHE = new HashMap<UUID, RabbitDataHolder>();

    public static class RabbitDataHolder {
        public boolean rabbit;
        public String type = RabbitConfig.TYPE_NORMAL;
        public double health = RabbitConfig.DEFAULT_HEALTH;
        public double speed = RabbitConfig.DEFAULT_SPEED;
        public double jump = RabbitConfig.DEFAULT_JUMP;
        public double fallDamage = RabbitConfig.DEFAULT_FALL;

        public int bodyR = RabbitConfig.NORMAL_BODY_R, bodyG = RabbitConfig.NORMAL_BODY_G, bodyB = RabbitConfig.NORMAL_BODY_B;
        public int earR = RabbitConfig.NORMAL_EAR_R, earG = RabbitConfig.NORMAL_EAR_G, earB = RabbitConfig.NORMAL_EAR_B;
        public int eyeR = RabbitConfig.NORMAL_EYE_R, eyeG = RabbitConfig.NORMAL_EYE_G, eyeB = RabbitConfig.NORMAL_EYE_B;
        public int tailR = RabbitConfig.NORMAL_TAIL_R, tailG = RabbitConfig.NORMAL_TAIL_G, tailB = RabbitConfig.NORMAL_TAIL_B;
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

        tag.setInteger("BodyR", this.bodyR);
        tag.setInteger("BodyG", this.bodyG);
        tag.setInteger("BodyB", this.bodyB);

        tag.setInteger("EarR", this.earR);
        tag.setInteger("EarG", this.earG);
        tag.setInteger("EarB", this.earB);

        tag.setInteger("EyeR", this.eyeR);
        tag.setInteger("EyeG", this.eyeG);
        tag.setInteger("EyeB", this.eyeB);

        tag.setInteger("TailR", this.tailR);
        tag.setInteger("TailG", this.tailG);
        tag.setInteger("TailB", this.tailB);

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

            this.bodyR = tag.getInteger("BodyR");
            this.bodyG = tag.getInteger("BodyG");
            this.bodyB = tag.getInteger("BodyB");

            this.earR = tag.getInteger("EarR");
            this.earG = tag.getInteger("EarG");
            this.earB = tag.getInteger("EarB");

            this.eyeR = tag.getInteger("EyeR");
            this.eyeG = tag.getInteger("EyeG");
            this.eyeB = tag.getInteger("EyeB");

            this.tailR = tag.getInteger("TailR");
            this.tailG = tag.getInteger("TailG");
            this.tailB = tag.getInteger("TailB");
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }

    // Required Centralized API Methods
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

    public static int color(EntityPlayer player, String part, String channel) {
        RabbitData data = get(player);
        int defaultVal = 255;

        int bR = data != null ? data.bodyR : RabbitConfig.NORMAL_BODY_R;
        int bG = data != null ? data.bodyG : RabbitConfig.NORMAL_BODY_G;
        int bB = data != null ? data.bodyB : RabbitConfig.NORMAL_BODY_B;

        int eR = data != null ? data.earR : RabbitConfig.NORMAL_EAR_R;
        int eG = data != null ? data.earG : RabbitConfig.NORMAL_EAR_G;
        int eB = data != null ? data.earB : RabbitConfig.NORMAL_EAR_B;

        int eyR = data != null ? data.eyeR : RabbitConfig.NORMAL_EYE_R;
        int eyG = data != null ? data.eyeG : RabbitConfig.NORMAL_EYE_G;
        int eyB = data != null ? data.eyeB : RabbitConfig.NORMAL_EYE_B;

        int tR = data != null ? data.tailR : RabbitConfig.NORMAL_TAIL_R;
        int tG = data != null ? data.tailG : RabbitConfig.NORMAL_TAIL_G;
        int tB = data != null ? data.tailB : RabbitConfig.NORMAL_TAIL_B;

        if (player != null && data == null) {
            RabbitDataHolder holder = CLIENT_CACHE.get(player.getUniqueID());
            if (holder != null) {
                bR = holder.bodyR; bG = holder.bodyG; bB = holder.bodyB;
                eR = holder.earR; eG = holder.earG; eB = holder.earB;
                eyR = holder.eyeR; eyG = holder.eyeG; eyB = holder.eyeB;
                tR = holder.tailR; tG = holder.tailG; tB = holder.tailB;
            }
        }

        if ("body".equalsIgnoreCase(part)) {
            if ("R".equalsIgnoreCase(channel)) return bR;
            if ("G".equalsIgnoreCase(channel)) return bG;
            if ("B".equalsIgnoreCase(channel)) return bB;
        } else if ("ear".equalsIgnoreCase(part)) {
            if ("R".equalsIgnoreCase(channel)) return eR;
            if ("G".equalsIgnoreCase(channel)) return eG;
            if ("B".equalsIgnoreCase(channel)) return eB;
        } else if ("eye".equalsIgnoreCase(part)) {
            if ("R".equalsIgnoreCase(channel)) return eyR;
            if ("G".equalsIgnoreCase(channel)) return eyG;
            if ("B".equalsIgnoreCase(channel)) return eyB;
        } else if ("tail".equalsIgnoreCase(part)) {
            if ("R".equalsIgnoreCase(channel)) return tR;
            if ("G".equalsIgnoreCase(channel)) return tG;
            if ("B".equalsIgnoreCase(channel)) return tB;
        }
        return defaultVal;
    }

    public static void setColor(EntityPlayer player, String part, int r, int g, int b) {
        RabbitData data = get(player);
        r = RabbitUtils.clampColor(r);
        g = RabbitUtils.clampColor(g);
        b = RabbitUtils.clampColor(b);

        if (data != null) {
            if ("body".equalsIgnoreCase(part)) {
                data.bodyR = r; data.bodyG = g; data.bodyB = b;
            } else if ("ear".equalsIgnoreCase(part)) {
                data.earR = r; data.earG = g; data.earB = b;
            } else if ("eye".equalsIgnoreCase(part)) {
                data.eyeR = r; data.eyeG = g; data.eyeB = b;
            } else if ("tail".equalsIgnoreCase(part)) {
                data.tailR = r; data.tailG = g; data.tailB = b;
            }
        }

        if (player != null) {
            RabbitDataHolder holder = getOrCreateCache(player.getUniqueID());
            if ("body".equalsIgnoreCase(part)) {
                holder.bodyR = r; holder.bodyG = g; holder.bodyB = b;
            } else if ("ear".equalsIgnoreCase(part)) {
                holder.earR = r; holder.earG = g; holder.earB = b;
            } else if ("eye".equalsIgnoreCase(part)) {
                holder.eyeR = r; holder.eyeG = g; holder.eyeB = b;
            } else if ("tail".equalsIgnoreCase(part)) {
                holder.tailR = r; holder.tailG = g; holder.tailB = b;
            }
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
                                       int bodyR, int bodyG, int bodyB,
                                       int earR, int earG, int earB,
                                       int eyeR, int eyeG, int eyeB,
                                       int tailR, int tailG, int tailB) {
        RabbitDataHolder holder = getOrCreateCache(uuid);
        holder.rabbit = isRabbit;
        holder.type = type;
        holder.health = health;
        holder.speed = speed;
        holder.jump = jump;
        holder.fallDamage = fall;
        holder.bodyR = bodyR; holder.bodyG = bodyG; holder.bodyB = bodyB;
        holder.earR = earR; holder.earG = earG; holder.earB = earB;
        holder.eyeR = eyeR; holder.eyeG = eyeG; holder.eyeB = eyeB;
        holder.tailR = tailR; holder.tailG = tailG; holder.tailB = tailB;
    }
}
