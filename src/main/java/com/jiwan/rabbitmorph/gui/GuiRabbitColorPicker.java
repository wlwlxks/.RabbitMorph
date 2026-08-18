package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRabbitColorPicker {

    private final String label;
    private final GuiTextField fieldR;
    private final GuiTextField fieldG;
    private final GuiTextField fieldB;
    private int r, g, b;

    public GuiRabbitColorPicker(FontRenderer font, int x, int y, String label, int initR, int initG, int initB) {
        this.label = label;
        this.r = initR;
        this.g = initG;
        this.b = initB;

        int fieldWidth = 30;
        int fieldHeight = 14;

        this.fieldR = new GuiTextField(10, font, x + 65, y, fieldWidth, fieldHeight);
        this.fieldG = new GuiTextField(11, font, x + 100, y, fieldWidth, fieldHeight);
        this.fieldB = new GuiTextField(12, font, x + 135, y, fieldWidth, fieldHeight);

        this.fieldR.setMaxStringLength(3);
        this.fieldG.setMaxStringLength(3);
        this.fieldB.setMaxStringLength(3);

        this.fieldR.setText(String.valueOf(initR));
        this.fieldG.setText(String.valueOf(initG));
        this.fieldB.setText(String.valueOf(initB));
    }

    public void draw(FontRenderer font, int x, int y) {
        font.drawString(label, x, y + 3, 0xFFFFFF);
        this.fieldR.drawTextBox();
        this.fieldG.drawTextBox();
        this.fieldB.drawTextBox();
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.fieldR.textboxKeyTyped(typedChar, keyCode);
        this.fieldG.textboxKeyTyped(typedChar, keyCode);
        this.fieldB.textboxKeyTyped(typedChar, keyCode);

        this.r = RabbitUtils.clampColor(RabbitUtils.parseIntSafe(this.fieldR.getText(), this.r));
        this.g = RabbitUtils.clampColor(RabbitUtils.parseIntSafe(this.fieldG.getText(), this.g));
        this.b = RabbitUtils.clampColor(RabbitUtils.parseIntSafe(this.fieldB.getText(), this.b));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.fieldR.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldG.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldB.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void setRGB(int r, int g, int b) {
        this.r = RabbitUtils.clampColor(r);
        this.g = RabbitUtils.clampColor(g);
        this.b = RabbitUtils.clampColor(b);
        this.fieldR.setText(String.valueOf(this.r));
        this.fieldG.setText(String.valueOf(this.g));
        this.fieldB.setText(String.valueOf(this.b));
    }

    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }
}
