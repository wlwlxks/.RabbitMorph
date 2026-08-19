package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTabColors {

    public GuiRabbitColorPicker pickerBody, pickerEar, pickerEye, pickerTail;

    public GuiTabColors(FontRenderer font, int x, int y) {
        this.pickerBody = new GuiRabbitColorPicker(font, x, y, "Body:", RabbitConfig.NORMAL_BODY_R, RabbitConfig.NORMAL_BODY_G, RabbitConfig.NORMAL_BODY_B, 255);
        this.pickerEar = new GuiRabbitColorPicker(font, x, y + 14, "Ear:", RabbitConfig.NORMAL_EAR_R, RabbitConfig.NORMAL_EAR_G, RabbitConfig.NORMAL_EAR_B, 255);
        this.pickerEye = new GuiRabbitColorPicker(font, x, y + 28, "Eye:", RabbitConfig.NORMAL_EYE_R, RabbitConfig.NORMAL_EYE_G, RabbitConfig.NORMAL_EYE_B, 255);
        this.pickerTail = new GuiRabbitColorPicker(font, x, y + 42, "Tail:", RabbitConfig.NORMAL_TAIL_R, RabbitConfig.NORMAL_TAIL_G, RabbitConfig.NORMAL_TAIL_B, 255);
    }

    public void draw(FontRenderer font, int mouseX, int mouseY) {
        pickerBody.draw(font, mouseX, mouseY);
        pickerEar.draw(font, mouseX, mouseY);
        pickerEye.draw(font, mouseX, mouseY);
        pickerTail.draw(font, mouseX, mouseY);
    }

    public void keyTyped(char typedChar, int keyCode) {
        pickerBody.keyTyped(typedChar, keyCode);
        pickerEar.keyTyped(typedChar, keyCode);
        pickerEye.keyTyped(typedChar, keyCode);
        pickerTail.keyTyped(typedChar, keyCode);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        pickerBody.mouseClicked(mouseX, mouseY, mouseButton);
        pickerEar.mouseClicked(mouseX, mouseY, mouseButton);
        pickerEye.mouseClicked(mouseX, mouseY, mouseButton);
        pickerTail.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        pickerBody.mouseReleased(mouseX, mouseY, state);
        pickerEar.mouseReleased(mouseX, mouseY, state);
        pickerEye.mouseReleased(mouseX, mouseY, state);
        pickerTail.mouseReleased(mouseX, mouseY, state);
    }

    public void setPresetColors(int bR, int bG, int bB, int eR, int eG, int eB, int eyR, int eyG, int eyB, int tR, int tG, int tB) {
        pickerBody.setRGBA(bR, bG, bB, 255);
        pickerEar.setRGBA(eR, eG, eB, 255);
        pickerEye.setRGBA(eyR, eyG, eyB, 255);
        pickerTail.setRGBA(tR, tG, tB, 255);
    }
}
