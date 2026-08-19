package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitConfig;
import com.jiwan.rabbitmorph.RabbitUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTabStats {

    private final FontRenderer font;
    private final int startX, startY;

    public GuiTextField fieldHealth;
    public GuiTextField fieldSpeed;
    public GuiTextField fieldJump;
    public GuiTextField fieldFall;
    public GuiTextField fieldStep;
    public GuiTextField fieldKnockback;

    public GuiTabStats(FontRenderer font, int x, int y) {
        this.font = font;
        this.startX = x;
        this.startY = y;

        int fieldW = 32;
        int fieldH = 12;

        this.fieldHealth = new GuiTextField(100, font, x + 55, y, fieldW, fieldH);
        this.fieldSpeed = new GuiTextField(101, font, x + 55, y + 16, fieldW, fieldH);
        this.fieldJump = new GuiTextField(102, font, x + 55, y + 32, fieldW, fieldH);

        this.fieldFall = new GuiTextField(103, font, x + 160, y, fieldW, fieldH);
        this.fieldStep = new GuiTextField(104, font, x + 160, y + 16, fieldW, fieldH);
        this.fieldKnockback = new GuiTextField(105, font, x + 160, y + 32, fieldW, fieldH);

        this.fieldHealth.setText(String.valueOf(RabbitConfig.DEFAULT_HEALTH));
        this.fieldSpeed.setText(String.valueOf(RabbitConfig.DEFAULT_SPEED));
        this.fieldJump.setText(String.valueOf(RabbitConfig.DEFAULT_JUMP));
        this.fieldFall.setText(String.valueOf(RabbitConfig.DEFAULT_FALL));
        this.fieldStep.setText("0.6");
        this.fieldKnockback.setText("0.0");
    }

    public void draw(int mouseX, int mouseY) {
        font.drawString("Health:", startX, startY + 2, 0xDDDDDD);
        fieldHealth.drawTextBox();

        font.drawString("Speed:", startX, startY + 18, 0xDDDDDD);
        fieldSpeed.drawTextBox();

        font.drawString("Jump:", startX, startY + 34, 0xDDDDDD);
        fieldJump.drawTextBox();

        font.drawString("FallDmg:", startX + 105, startY + 2, 0xDDDDDD);
        fieldFall.drawTextBox();

        font.drawString("StepHeight:", startX + 105, startY + 18, 0xDDDDDD);
        fieldStep.drawTextBox();

        font.drawString("Knockback:", startX + 105, startY + 34, 0xDDDDDD);
        fieldKnockback.drawTextBox();
    }

    public void keyTyped(char typedChar, int keyCode) {
        fieldHealth.textboxKeyTyped(typedChar, keyCode);
        fieldSpeed.textboxKeyTyped(typedChar, keyCode);
        fieldJump.textboxKeyTyped(typedChar, keyCode);
        fieldFall.textboxKeyTyped(typedChar, keyCode);
        fieldStep.textboxKeyTyped(typedChar, keyCode);
        fieldKnockback.textboxKeyTyped(typedChar, keyCode);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        fieldHealth.mouseClicked(mouseX, mouseY, mouseButton);
        fieldSpeed.mouseClicked(mouseX, mouseY, mouseButton);
        fieldJump.mouseClicked(mouseX, mouseY, mouseButton);
        fieldFall.mouseClicked(mouseX, mouseY, mouseButton);
        fieldStep.mouseClicked(mouseX, mouseY, mouseButton);
        fieldKnockback.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public double getHealth() { return RabbitUtils.parseDoubleSafe(fieldHealth.getText(), RabbitConfig.DEFAULT_HEALTH); }
    public double getSpeed() { return RabbitUtils.parseDoubleSafe(fieldSpeed.getText(), RabbitConfig.DEFAULT_SPEED); }
    public double getJump() { return RabbitUtils.parseDoubleSafe(fieldJump.getText(), RabbitConfig.DEFAULT_JUMP); }
    public double getFall() { return RabbitUtils.parseDoubleSafe(fieldFall.getText(), RabbitConfig.DEFAULT_FALL); }
    public float getStep() { return (float) RabbitUtils.parseDoubleSafe(fieldStep.getText(), 0.6D); }
    public float getKnockback() { return (float) RabbitUtils.parseDoubleSafe(fieldKnockback.getText(), 0.0D); }
}
