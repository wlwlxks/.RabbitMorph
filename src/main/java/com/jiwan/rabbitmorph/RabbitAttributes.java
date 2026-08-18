package com.jiwan.rabbitmorph;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class RabbitAttributes {

    public static final UUID SPEED_MODIFIER_UUID = UUID.fromString("d8f34180-2a91-4c07-b31a-6d601a4e10b1");
    public static final String SPEED_MODIFIER_NAME = "RabbitSpeedModifier";

    public static void applyAttributes(EntityPlayer player) {
        if (player == null || player.worldObj == null || player.worldObj.isRemote) return;

        double targetSpeed = RabbitData.getSpeed(player);
        IAttributeInstance speedAttr = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (speedAttr != null) {
            AttributeModifier existing = speedAttr.getModifier(SPEED_MODIFIER_UUID);
            if (existing != null) {
                speedAttr.removeModifier(existing);
            }
            // Base speed for player is 0.1D, so modifier amount = (targetSpeed - 0.1)
            double modifierAmount = targetSpeed - 0.1D;
            AttributeModifier speedMod = new AttributeModifier(SPEED_MODIFIER_UUID, SPEED_MODIFIER_NAME, modifierAmount, 0);
            speedAttr.applyModifier(speedMod);
        }

        double targetHealth = RabbitData.getHealth(player);
        IAttributeInstance healthAttr = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        if (healthAttr != null) {
            healthAttr.setBaseValue(targetHealth);
            if (player.getHealth() > targetHealth) {
                player.setHealth((float) targetHealth);
            }
        }
    }

    public static void removeAttributes(EntityPlayer player) {
        if (player == null || player.worldObj == null || player.worldObj.isRemote) return;

        IAttributeInstance speedAttr = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (speedAttr != null) {
            AttributeModifier existing = speedAttr.getModifier(SPEED_MODIFIER_UUID);
            if (existing != null) {
                speedAttr.removeModifier(existing);
            }
        }

        IAttributeInstance healthAttr = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        if (healthAttr != null) {
            healthAttr.setBaseValue(20.0D);
            if (player.getHealth() > 20.0F) {
                player.setHealth(20.0F);
            }
        }
    }
}
