package com.jiwan.rabbitmorph;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RabbitTransform {

    public static final int TRANSITION_MAX_TICKS = 15;
    private static final Map<UUID, Integer> TRANSITION_TICKS = new HashMap<UUID, Integer>();
    private static final Map<UUID, Boolean> TRANSITION_TARGET_STATE = new HashMap<UUID, Boolean>();

    public static void startTransformation(EntityPlayer player, boolean intoRabbit) {
        if (player == null || player.worldObj == null) return;
        UUID uuid = player.getUniqueID();
        TRANSITION_TICKS.put(uuid, TRANSITION_MAX_TICKS);
        TRANSITION_TARGET_STATE.put(uuid, intoRabbit);

        World world = player.worldObj;
        double x = player.posX;
        double y = player.posY + player.height / 2.0D;
        double z = player.posZ;

        if (!world.isRemote) {
            String soundName = intoRabbit ? "mob.rabbit.hop" : "random.pop";
            world.playSoundEffect(x, y, z, soundName, 1.0F, intoRabbit ? 1.0F : 1.2F);
        } else {
            // Spawn particle burst on client
            for (int i = 0; i < 20; i++) {
                double vx = (world.rand.nextDouble() - 0.5D) * 0.3D;
                double vy = world.rand.nextDouble() * 0.3D + 0.1D;
                double vz = (world.rand.nextDouble() - 0.5D) * 0.3D;
                world.spawnParticle(EnumParticleTypes.CLOUD, x + vx, y, z + vz, vx, vy, vz);
                if (i % 2 == 0) {
                    world.spawnParticle(EnumParticleTypes.HEART, x + vx, y + 0.2D, z + vz, vx, vy * 0.5D, vz);
                }
            }
        }
    }

    public static float getScaleFactor(EntityPlayer player) {
        if (player == null) return 1.0F;
        UUID uuid = player.getUniqueID();
        Integer remainingTicks = TRANSITION_TICKS.get(uuid);
        Boolean intoRabbit = TRANSITION_TARGET_STATE.get(uuid);

        if (remainingTicks == null || remainingTicks <= 0 || intoRabbit == null) {
            return RabbitData.isRabbit(player) ? 1.0F : 0.0F;
        }

        float progress = 1.0F - ((float) remainingTicks / (float) TRANSITION_MAX_TICKS);
        return intoRabbit ? progress : (1.0F - progress);
    }

    public static void tick() {
        if (TRANSITION_TICKS.isEmpty()) return;
        for (UUID uuid : new java.util.ArrayList<UUID>(TRANSITION_TICKS.keySet())) {
            int ticks = TRANSITION_TICKS.get(uuid);
            if (ticks > 1) {
                TRANSITION_TICKS.put(uuid, ticks - 1);
            } else {
                TRANSITION_TICKS.remove(uuid);
                TRANSITION_TARGET_STATE.remove(uuid);
            }
        }
    }
}
