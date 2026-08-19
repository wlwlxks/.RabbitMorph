package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiLoadConfigDialog extends GuiScreen {

    private final GuiRabbitSettings parentScreen;
    private List<File> configFileSlots;
    private int selectedIndex = -1;
    private int scrollOffset = 0;
    private long lastClickTime = 0L;

    private GuiButton btnLoad;
    private GuiButton btnDelete;

    private static final int ITEM_H = 22;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public GuiLoadConfigDialog(GuiRabbitSettings parent) {
        this.parentScreen = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.configFileSlots = RabbitConfig.listSavedJsonConfigs();

        int dialogW = 280;
        int dialogH = 180;
        int dialogX = (this.width - dialogW) / 2;
        int dialogY = (this.height - dialogH) / 2;

        this.btnLoad = new GuiButton(1, dialogX + 15, dialogY + dialogH - 24, 75, 16, "Load");
        this.btnDelete = new GuiButton(2, dialogX + 100, dialogY + dialogH - 24, 75, 16, "Delete");
        GuiButton btnCancel = new GuiButton(3, dialogX + 185, dialogY + dialogH - 24, 75, 16, I18n.format("gui.rabbitmorph.cancel"));

        this.buttonList.add(btnLoad);
        this.buttonList.add(btnDelete);
        this.buttonList.add(btnCancel);

        updateButtonState();
    }

    private void updateButtonState() {
        boolean hasSelection = selectedIndex >= 0 && selectedIndex < configFileSlots.size();
        btnLoad.enabled = hasSelection;
        btnDelete.enabled = hasSelection;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int dialogW = 280;
        int dialogH = 180;
        int dialogX = (this.width - dialogW) / 2;
        int dialogY = (this.height - dialogH) / 2;

        int listH = dialogH - 58;
        int maxVisible = listH / ITEM_H;

        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            int maxScroll = Math.max(0, configFileSlots.size() - maxVisible);
            if (dWheel > 0 && scrollOffset > 0) {
                scrollOffset--;
            } else if (dWheel < 0 && scrollOffset < maxScroll) {
                scrollOffset++;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int dialogW = 280;
        int dialogH = 180;
        int dialogX = (this.width - dialogW) / 2;
        int dialogY = (this.height - dialogH) / 2;

        int listX = dialogX + 10;
        int listY = dialogY + 28;
        int listW = dialogW - 20;
        int listH = dialogH - 58;

        if (mouseButton == 0 && mouseX >= listX && mouseX <= listX + listW && mouseY >= listY && mouseY <= listY + listH) {
            int clickedIdx = scrollOffset + (mouseY - listY) / ITEM_H;
            if (clickedIdx >= 0 && clickedIdx < configFileSlots.size()) {
                long now = Minecraft.getSystemTime();
                if (clickedIdx == selectedIndex && (now - lastClickTime < 400)) { // Double click to load
                    actionPerformed(btnLoad);
                    return;
                }
                selectedIndex = clickedIdx;
                lastClickTime = now;
                updateButtonState();
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1 && selectedIndex >= 0 && selectedIndex < configFileSlots.size()) { // Load Selected
            File file = configFileSlots.get(selectedIndex);
            RabbitConfig.RabbitConfigData data = RabbitConfig.loadJsonConfigFromFile(file);
            if (data != null) {
                parentScreen.populateFromConfigData(data);
            }
            this.mc.displayGuiScreen(parentScreen);
        } else if (button.id == 2 && selectedIndex >= 0 && selectedIndex < configFileSlots.size()) { // Delete
            File file = configFileSlots.get(selectedIndex);
            RabbitConfig.deleteJsonConfig(file);
            this.configFileSlots = RabbitConfig.listSavedJsonConfigs();
            this.selectedIndex = -1;
            this.scrollOffset = 0;
            updateButtonState();
        } else if (button.id == 3) { // Cancel
            this.mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC
            this.mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int dialogW = 280;
        int dialogH = 180;
        int dialogX = (this.width - dialogW) / 2;
        int dialogY = (this.height - dialogH) / 2;

        // Dialog frame
        drawRect(dialogX, dialogY, dialogX + dialogW, dialogY + dialogH, 0xEE1A1A24);
        drawRect(dialogX, dialogY, dialogX + dialogW, dialogY + 1, 0xFF5588FF);
        drawRect(dialogX, dialogY + dialogH - 1, dialogX + dialogW, dialogY + dialogH, 0xFF5588FF);
        drawRect(dialogX, dialogY, dialogX + 1, dialogY + dialogH, 0xFF5588FF);
        drawRect(dialogX + dialogW - 1, dialogY, dialogX + dialogW, dialogY + dialogH, 0xFF5588FF);

        this.fontRendererObj.drawStringWithShadow("Select Config File to Load", dialogX + 15, dialogY + 10, 0xFFFF55);

        // Render List Box inside dialog bounds
        int listX = dialogX + 10;
        int listY = dialogY + 26;
        int listW = dialogW - 20;
        int listH = dialogH - 56;

        drawRect(listX, listY, listX + listW, listY + listH, 0xFF0D0D12);
        drawRect(listX, listY, listX + listW, listY + 1, 0xFF334455);
        drawRect(listX, listY + listH - 1, listX + listW, listY + listH, 0xFF334455);
        drawRect(listX, listY, listX + 1, listY + listH, 0xFF334455);
        drawRect(listX + listW - 1, listY, listX + listW, listY + listH, 0xFF334455);

        int maxVisible = listH / ITEM_H;

        if (configFileSlots.isEmpty()) {
            this.fontRendererObj.drawString("No saved config files found.", listX + 10, listY + 20, 0x888888);
        } else {
            for (int i = 0; i < maxVisible; i++) {
                int index = scrollOffset + i;
                if (index >= configFileSlots.size()) break;

                File file = configFileSlots.get(index);
                int itemY = listY + 2 + (i * ITEM_H);

                boolean isSelected = (index == selectedIndex);
                if (isSelected) {
                    drawRect(listX + 2, itemY, listX + listW - 8, itemY + ITEM_H - 2, 0xFF2A3B55);
                    drawRect(listX + 2, itemY, listX + listW - 8, itemY + 1, 0xFF5588FF);
                    drawRect(listX + 2, itemY + ITEM_H - 3, listX + listW - 8, itemY + ITEM_H - 2, 0xFF5588FF);
                }

                String name = file.getName();
                String dateStr = dateFormat.format(new Date(file.lastModified()));

                this.fontRendererObj.drawString(name, listX + 8, itemY + 2, isSelected ? 0xFFFF55 : 0xEEEEEE);
                this.fontRendererObj.drawString(dateStr, listX + listW - 110, itemY + 2, 0x888888);
            }

            // Scrollbar
            if (configFileSlots.size() > maxVisible) {
                int sbX = listX + listW - 6;
                int sbH = listH - 4;
                int handleH = Math.max(10, sbH * maxVisible / configFileSlots.size());
                int maxScroll = configFileSlots.size() - maxVisible;
                int handleY = listY + 2 + ((sbH - handleH) * scrollOffset / maxScroll);

                drawRect(sbX, listY + 2, sbX + 4, listY + 2 + sbH, 0xFF1A1A24);
                drawRect(sbX, handleY, sbX + 4, handleY + handleH, 0xFF5588FF);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
