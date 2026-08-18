package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRabbitColorPicker {

    private final String label;
    private final GuiTextField fieldR, fieldG, fieldB, fieldA;
    private int r, g, b, a;

    private final int startX;
    private final int startY;
    private static final int SLIDER_W = 32;
    private static final int SLIDER_H = 8;
    private static final int CHANNEL_SPACING = 55;

    private int activeSlider = -1; // 0=R, 1=G, 2=B, 3=A, -1=None

    public GuiRabbitColorPicker(FontRenderer font, int x, int y, String label, int initR, int initG, int initB, int initA) {
        this.label = label;
        this.startX = x;
        this.startY = y;
        this.r = RabbitUtils.clampColor(initR);
        this.g = RabbitUtils.clampColor(initG);
        this.b = RabbitUtils.clampColor(initB);
        this.a = RabbitUtils.clampColor(initA);

        int fieldWidth = 18;
        int fieldHeight = 12;

        this.fieldR = new GuiTextField(10, font, x + 50, y, fieldWidth, fieldHeight);
        this.fieldG = new GuiTextField(11, font, x + 50 + CHANNEL_SPACING, y, fieldWidth, fieldHeight);
        this.fieldB = new GuiTextField(12, font, x + 50 + CHANNEL_SPACING * 2, y, fieldWidth, fieldHeight);
        this.fieldA = new GuiTextField(13, font, x + 50 + CHANNEL_SPACING * 3, y, fieldWidth, fieldHeight);

        this.fieldR.setMaxStringLength(3);
        this.fieldG.setMaxStringLength(3);
        this.fieldB.setMaxStringLength(3);
        this.fieldA.setMaxStringLength(3);

        this.fieldR.setText(String.valueOf(this.r));
        this.fieldG.setText(String.valueOf(this.g));
        this.fieldB.setText(String.valueOf(this.b));
        this.fieldA.setText(String.valueOf(this.a));
    }

    public void draw(FontRenderer font, int mouseX, int mouseY) {
        font.drawString(label, startX, startY + 2, 0xFFFFFF);

        this.fieldR.drawTextBox();
        this.fieldG.drawTextBox();
        this.fieldB.drawTextBox();
        this.fieldA.drawTextBox();

        // Draw Sliders spaced cleanly
        drawChannelSlider(startX + 70, startY + 2, this.r, 0xFF4444);
        drawChannelSlider(startX + 70 + CHANNEL_SPACING, startY + 2, this.g, 0x44FF44);
        drawChannelSlider(startX + 70 + CHANNEL_SPACING * 2, startY + 2, this.b, 0x4444FF);
        drawChannelSlider(startX + 70 + CHANNEL_SPACING * 3, startY + 2, this.a, 0xAAAAAA);

        if (this.activeSlider != -1) {
            updateSliderDrag(mouseX);
        }
    }

    private void drawChannelSlider(int x, int y, int value, int color) {
        Gui.drawRect(x, y, x + SLIDER_W, y + SLIDER_H, 0xFF222222);
        int fillW = (int) (((float) value / 255.0F) * SLIDER_W);
        Gui.drawRect(x, y, x + fillW, y + SLIDER_H, 0xFF000000 | color);

        int handleX = x + fillW - 2;
        if (handleX < x) handleX = x;
        if (handleX > x + SLIDER_W - 3) handleX = x + SLIDER_W - 3;
        Gui.drawRect(handleX, y - 1, handleX + 3, y + SLIDER_H + 1, 0xFFFFFFFF);
    }

    private void updateSliderDrag(int mouseX) {
        int sliderX = startX + 70 + (this.activeSlider * CHANNEL_SPACING);
        float pct = (float) (mouseX - sliderX) / (float) SLIDER_W;
        if (pct < 0.0F) pct = 0.0F;
        if (pct > 1.0F) pct = 1.0F;
        int val = Math.round(pct * 255.0F);

        if (this.activeSlider == 0) { this.r = val; this.fieldR.setText(String.valueOf(val)); }
        else if (this.activeSlider == 1) { this.g = val; this.fieldG.setText(String.valueOf(val)); }
        else if (this.activeSlider == 2) { this.b = val; this.fieldB.setText(String.valueOf(val)); }
        else if (this.activeSlider == 3) { this.a = val; this.fieldA.setText(String.valueOf(val)); }
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.fieldR.textboxKeyTyped(typedChar, keyCode);
        this.fieldG.textboxKeyTyped(typedChar, keyCode);
        this.fieldB.textboxKeyTyped(typedChar, keyCode);
        this.fieldA.textboxKeyTyped(typedChar, keyCode);

        this.r = RabbitUtils.clampColor(RabbitUtils.parseIntSafe(this.fieldR.getText(), this.r));
        this.g = RabbitUtils.clampColor(RabbitUtils.parseIntSafe(this.fieldG.getText(), this.g));
        this.b = RabbitUtils.clampColor(RabbitUtils.parseIntSafe(this.fieldB.getText(), this.b));
        this.a = RabbitUtils.clampColor(RabbitUtils.parseIntSafe(this.fieldA.getText(), this.a));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.fieldR.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldG.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldB.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldA.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            for (int i = 0; i < 4; i++) {
                int sliderX = startX + 70 + (i * CHANNEL_SPACING);
                if (isInsideSlider(mouseX, mouseY, sliderX, startY + 2)) {
                    this.activeSlider = i;
                    updateSliderDrag(mouseX);
                    break;
                }
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.activeSlider = -1;
        }
    }

    private boolean isInsideSlider(int mouseX, int mouseY, int x, int y) {
        return mouseX >= x - 2 && mouseX <= x + SLIDER_W + 2 && mouseY >= y - 2 && mouseY <= y + SLIDER_H + 2;
    }

    public void setRGBA(int r, int g, int b, int a) {
        this.r = RabbitUtils.clampColor(r);
        this.g = RabbitUtils.clampColor(g);
        this.b = RabbitUtils.clampColor(b);
        this.a = RabbitUtils.clampColor(a);
        this.fieldR.setText(String.valueOf(this.r));
        this.fieldG.setText(String.valueOf(this.g));
        this.fieldB.setText(String.valueOf(this.b));
        this.fieldA.setText(String.valueOf(this.a));
    }

    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }
    public int getA() { return a; }
}
