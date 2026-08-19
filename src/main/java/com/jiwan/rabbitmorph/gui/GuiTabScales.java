package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiTabScales {

    private final FontRenderer font;
    private final int startX, startY;

    public GuiTextField fieldScaleOverall, fieldScaleHead, fieldScaleEar, fieldScaleBody, fieldScaleLegs, fieldScaleTail, fieldScaleNose, fieldScaleEye;

    public GuiTabScales(FontRenderer font, int x, int y) {
        this.font = font;
        this.startX = x;
        this.startY = y;

        int fieldW = 28;
        int fieldH = 11;

        // Row 1
        this.fieldScaleOverall = new GuiTextField(200, font, x + 40, y, fieldW, fieldH);
        this.fieldScaleHead = new GuiTextField(201, font, x + 105, y, fieldW, fieldH);
        this.fieldScaleEar = new GuiTextField(202, font, x + 165, y, fieldW, fieldH);

        // Row 2
        this.fieldScaleBody = new GuiTextField(203, font, x + 40, y + 16, fieldW, fieldH);
        this.fieldScaleLegs = new GuiTextField(204, font, x + 105, y + 16, fieldW, fieldH);
        this.fieldScaleTail = new GuiTextField(205, font, x + 165, y + 16, fieldW, fieldH);

        // Row 3
        this.fieldScaleNose = new GuiTextField(206, font, x + 40, y + 32, fieldW, fieldH);
        this.fieldScaleEye = new GuiTextField(207, font, x + 105, y + 32, fieldW, fieldH);

        this.fieldScaleOverall.setText("1.0");
        this.fieldScaleHead.setText("1.0");
        this.fieldScaleEar.setText("1.0");
        this.fieldScaleBody.setText("1.0");
        this.fieldScaleLegs.setText("1.0");
        this.fieldScaleTail.setText("1.0");
        this.fieldScaleNose.setText("1.0");
        this.fieldScaleEye.setText("1.0");
    }

    public void draw(int mouseX, int mouseY) {
        font.drawString("Overall:", startX, startY + 2, 0xAAAAAA);
        fieldScaleOverall.drawTextBox();

        font.drawString("Head:", startX + 72, startY + 2, 0xAAAAAA);
        fieldScaleHead.drawTextBox();

        font.drawString("Ear:", startX + 138, startY + 2, 0xAAAAAA);
        fieldScaleEar.drawTextBox();

        font.drawString("Body:", startX, startY + 18, 0xAAAAAA);
        fieldScaleBody.drawTextBox();

        font.drawString("Legs:", startX + 72, startY + 18, 0xAAAAAA);
        fieldScaleLegs.drawTextBox();

        font.drawString("Tail:", startX + 138, startY + 18, 0xAAAAAA);
        fieldScaleTail.drawTextBox();

        font.drawString("Nose:", startX, startY + 34, 0xAAAAAA);
        fieldScaleNose.drawTextBox();

        font.drawString("Eye:", startX + 72, startY + 34, 0xAAAAAA);
        fieldScaleEye.drawTextBox();
    }

    public void keyTyped(char typedChar, int keyCode) {
        fieldScaleOverall.textboxKeyTyped(typedChar, keyCode);
        fieldScaleHead.textboxKeyTyped(typedChar, keyCode);
        fieldScaleEar.textboxKeyTyped(typedChar, keyCode);
        fieldScaleBody.textboxKeyTyped(typedChar, keyCode);
        fieldScaleLegs.textboxKeyTyped(typedChar, keyCode);
        fieldScaleTail.textboxKeyTyped(typedChar, keyCode);
        fieldScaleNose.textboxKeyTyped(typedChar, keyCode);
        fieldScaleEye.textboxKeyTyped(typedChar, keyCode);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        fieldScaleOverall.mouseClicked(mouseX, mouseY, mouseButton);
        fieldScaleHead.mouseClicked(mouseX, mouseY, mouseButton);
        fieldScaleEar.mouseClicked(mouseX, mouseY, mouseButton);
        fieldScaleBody.mouseClicked(mouseX, mouseY, mouseButton);
        fieldScaleLegs.mouseClicked(mouseX, mouseY, mouseButton);
        fieldScaleTail.mouseClicked(mouseX, mouseY, mouseButton);
        fieldScaleNose.mouseClicked(mouseX, mouseY, mouseButton);
        fieldScaleEye.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public float getScaleOverall() { return (float) RabbitUtils.parseDoubleSafe(fieldScaleOverall.getText(), 1.0D); }
    public float getScaleHead() { return (float) RabbitUtils.parseDoubleSafe(fieldScaleHead.getText(), 1.0D); }
    public float getScaleEar() { return (float) RabbitUtils.parseDoubleSafe(fieldScaleEar.getText(), 1.0D); }
    public float getScaleBody() { return (float) RabbitUtils.parseDoubleSafe(fieldScaleBody.getText(), 1.0D); }
    public float getScaleLegs() { return (float) RabbitUtils.parseDoubleSafe(fieldScaleLegs.getText(), 1.0D); }
    public float getScaleTail() { return (float) RabbitUtils.parseDoubleSafe(fieldScaleTail.getText(), 1.0D); }
    public float getScaleNose() { return (float) RabbitUtils.parseDoubleSafe(fieldScaleNose.getText(), 1.0D); }
    public float getScaleEye() { return (float) RabbitUtils.parseDoubleSafe(fieldScaleEye.getText(), 1.0D); }
}
