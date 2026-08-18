package com.jiwan.rabbitmorph;

import net.minecraft.util.MathHelper;

public class RabbitAnimation {

    public static class RabbitPose {
        public float headPitch;
        public float headYaw;
        public float earLeftPitch;
        public float earRightPitch;
        public float bodyPitch;
        public float frontLegLeftPitch;
        public float frontLegRightPitch;
        public float rearLegLeftPitch;
        public float rearLegRightPitch;
        public float tailPitch;
    }

    public static RabbitPose calculatePose(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, boolean isJumping, boolean isSneaking) {
        RabbitPose pose = new RabbitPose();

        pose.headPitch = headPitch * 0.017453292F;
        pose.headYaw = headYaw * 0.017453292F;

        // Base walking/hopping animation cycle
        float walkCycle = MathHelper.sin(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
        float walkCycleOpposite = MathHelper.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;

        pose.frontLegLeftPitch = walkCycle;
        pose.frontLegRightPitch = walkCycleOpposite;
        pose.rearLegLeftPitch = walkCycleOpposite * 0.8F;
        pose.rearLegRightPitch = walkCycle * 0.8F;

        // Subtle ear wiggling idle animation
        float earIdle = MathHelper.sin(ageInTicks * 0.1F) * 0.05F;
        pose.earLeftPitch = pose.headPitch * 0.2F + earIdle;
        pose.earRightPitch = pose.headPitch * 0.2F - earIdle;

        // Subtle tail idle animation
        pose.tailPitch = MathHelper.cos(ageInTicks * 0.15F) * 0.1F;

        if (isJumping) {
            pose.bodyPitch = -0.35F;
            pose.frontLegLeftPitch = -0.6F;
            pose.frontLegRightPitch = -0.6F;
            pose.rearLegLeftPitch = 0.6F;
            pose.rearLegRightPitch = 0.6F;
            pose.tailPitch += 0.2F;
        } else if (isSneaking) {
            pose.bodyPitch = 0.2F;
            pose.frontLegLeftPitch += 0.2F;
            pose.frontLegRightPitch += 0.2F;
        }

        return pose;
    }
}
