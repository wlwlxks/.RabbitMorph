package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiTabPresets {

    private final FontRenderer font;
    private final int startX, startY;

    public String selectedType = RabbitConfig.TYPE_NORMAL;

    private GuiButton btnNormal, btnBrown, btnBlack, btnWhite, btnGolden;

    public GuiTabPresets(FontRenderer font, int x, int y) {
        this.font = font;
        this.startX = x;
        this.startY = y;
    }

    public void initGui(List<GuiButton> buttonList, int startBtnId) {
        int w = 45, h = 14;
        this.btnNormal = new GuiButton(startBtnId, startX, startY, w, h, "Normal");
        this.btnBrown = new GuiButton(startBtnId + 1, startX + 47, startY, w, h, "Brown");
        this.btnBlack = new GuiButton(startBtnId + 2, startX + 94, startY, w, h, "Black");
        this.btnWhite = new GuiButton(startBtnId + 3, startX + 141, startY, w, h, "White");
        this.btnGolden = new GuiButton(startBtnId + 4, startX + 188, startY, w, h, "Golden");

        buttonList.add(btnNormal);
        buttonList.add(btnBrown);
        buttonList.add(btnBlack);
        buttonList.add(btnWhite);
        buttonList.add(btnGolden);
    }

    public String actionPerformed(GuiButton button) {
        if (button == btnNormal) { selectedType = RabbitConfig.TYPE_NORMAL; return selectedType; }
        if (button == btnBrown) { selectedType = RabbitConfig.TYPE_BROWN; return selectedType; }
        if (button == btnBlack) { selectedType = RabbitConfig.TYPE_BLACK; return selectedType; }
        if (button == btnWhite) { selectedType = RabbitConfig.TYPE_WHITE; return selectedType; }
        if (button == btnGolden) { selectedType = RabbitConfig.TYPE_GOLDEN; return selectedType; }
        return null;
    }

    public void draw(int mouseX, int mouseY) {
        font.drawString("Quick Color Presets:", startX, startY - 10, 0xAAAAAA);
        font.drawString("Selected: " + selectedType, startX, startY + 20, 0xFFFF55);
    }
}
