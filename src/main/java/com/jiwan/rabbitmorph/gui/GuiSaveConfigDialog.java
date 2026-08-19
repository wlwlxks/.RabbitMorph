package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiSaveConfigDialog extends GuiScreen {

    private final GuiRabbitSettings parentScreen;
    private final RabbitConfig.RabbitConfigData configData;
    private GuiTextField fieldName;

    public GuiSaveConfigDialog(GuiRabbitSettings parent, RabbitConfig.RabbitConfigData data) {
        this.parentScreen = parent;
        this.configData = data;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        int dialogW = 200;
        int dialogH = 100;
        int dialogX = (this.width - dialogW) / 2;
        int dialogY = (this.height - dialogH) / 2;

        this.fieldName = new GuiTextField(100, this.fontRendererObj, dialogX + 15, dialogY + 38, 130, 14);
        this.fieldName.setFocused(true);
        this.fieldName.setText("my_rabbit_config");

        this.buttonList.add(new GuiButton(1, dialogX + 20, dialogY + 68, 70, 16, I18n.format("gui.rabbitmorph.apply"))); // Save
        this.buttonList.add(new GuiButton(2, dialogX + 110, dialogY + 68, 70, 16, I18n.format("gui.rabbitmorph.cancel"))); // Cancel
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) { // Save
            String name = fieldName.getText().trim();
            RabbitConfig.saveJsonConfigWithName(name, configData);
            this.mc.displayGuiScreen(parentScreen);
        } else if (button.id == 2) { // Cancel
            this.mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC key
            this.mc.displayGuiScreen(parentScreen);
            return;
        }
        if (keyCode == 28) { // ENTER key
            actionPerformed((GuiButton) this.buttonList.get(0));
            return;
        }
        this.fieldName.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldName.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int dialogW = 200;
        int dialogH = 100;
        int dialogX = (this.width - dialogW) / 2;
        int dialogY = (this.height - dialogH) / 2;

        // Dialog background box & frame
        drawRect(dialogX, dialogY, dialogX + dialogW, dialogY + dialogH, 0xEE1A1A24);
        drawRect(dialogX, dialogY, dialogX + dialogW, dialogY + 1, 0xFF5588FF);
        drawRect(dialogX, dialogY + dialogH - 1, dialogX + dialogW, dialogY + dialogH, 0xFF5588FF);
        drawRect(dialogX, dialogY, dialogX + 1, dialogY + dialogH, 0xFF5588FF);
        drawRect(dialogX + dialogW - 1, dialogY, dialogX + dialogW, dialogY + dialogH, 0xFF5588FF);

        this.fontRendererObj.drawStringWithShadow("Save Config File", dialogX + 15, dialogY + 10, 0xFFFF55);
        this.fontRendererObj.drawString("Enter file name:", dialogX + 15, dialogY + 26, 0xCCCCCC);

        this.fieldName.drawTextBox();
        // Fixed read-only .json extension label
        this.fontRendererObj.drawString(".json", dialogX + 150, dialogY + 41, 0x88AAFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
