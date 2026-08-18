package com.jiwan.rabbitmorph;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import java.util.List;

public class RabbitUtils {

    public static int clampColor(int val) {
        if (val < 0) return 0;
        if (val > 255) return 255;
        return val;
    }

    public static double clampDouble(double val, double min, double max, double fallback) {
        if (Double.isNaN(val) || Double.isInfinite(val)) return fallback;
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }

    public static int parseIntSafe(String s, int fallback) {
        if (s == null) return fallback;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    public static double parseDoubleSafe(String s, double fallback) {
        if (s == null) return fallback;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    public static EntityPlayer getTargetPlayer(EntityPlayer shooter, double maxDistance) {
        if (shooter == null || shooter.worldObj == null) return null;

        Vec3 eyePos = shooter.getPositionEyes(1.0F);
        Vec3 lookVec = shooter.getLook(1.0F);
        Vec3 reachVec = eyePos.addVector(lookVec.xCoord * maxDistance, lookVec.yCoord * maxDistance, lookVec.zCoord * maxDistance);

        AxisAlignedBB searchBox = shooter.getEntityBoundingBox().addCoord(lookVec.xCoord * maxDistance, lookVec.yCoord * maxDistance, lookVec.zCoord * maxDistance).expand(1.0D, 1.0D, 1.0D);
        List<Entity> list = shooter.worldObj.getEntitiesWithinAABBExcludingEntity(shooter, searchBox);

        EntityPlayer closestPlayer = null;
        double closestDist = maxDistance;

        for (Entity entity : list) {
            if (entity instanceof EntityPlayer && entity.canBeCollidedWith()) {
                float borderSize = entity.getCollisionBorderSize();
                AxisAlignedBB aabb = entity.getEntityBoundingBox().expand(borderSize, borderSize, borderSize);
                if (aabb.isVecInside(eyePos)) {
                    closestPlayer = (EntityPlayer) entity;
                    closestDist = 0.0D;
                } else {
                    net.minecraft.util.MovingObjectPosition intercept = aabb.calculateIntercept(eyePos, reachVec);
                    if (intercept != null && intercept.hitVec != null) {
                        double dist = eyePos.distanceTo(intercept.hitVec);
                        if (dist < closestDist) {
                            closestPlayer = (EntityPlayer) entity;
                            closestDist = dist;
                        }
                    }
                }
            }
        }
        return closestPlayer;
    }
}
