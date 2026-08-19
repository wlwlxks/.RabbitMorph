package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiLoadConfigDialog extends GuiScreen {

    private final GuiRabbitSettings parentScreen;
    private List<File> configFileSlots;
    private ConfigListSlot listSlot;
    private int selectedIndex = -1;

    private GuiButton btnLoad;
    private GuiButton btnDelete;

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

        this.listSlot = new ConfigListSlot(dialogX + 10, dialogY + 28, dialogW - 20, dialogH - 60);

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
        if (this.listSlot != null) {
            this.listSlot.handleMouseInput();
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

        if (this.listSlot != null) {
            this.listSlot.drawScreen(mouseX, mouseY, partialTicks);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // Scrollable File List Slot
    class ConfigListSlot extends GuiSlot {

        private final int slotX, slotW;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        public ConfigListSlot(int x, int y, int width, int height) {
            super(GuiLoadConfigDialog.this.mc, width, height, y, y + height, 22);
            this.slotX = x;
            this.slotW = width;
        }

        @Override
        protected int getSize() {
            return configFileSlots.size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            selectedIndex = slotIndex;
            updateButtonState();
            if (isDoubleClick) {
                try {
                    actionPerformed(btnLoad);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return slotIndex == selectedIndex;
        }

        @Override
        protected void drawBackground() {}

        @Override
        protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseX, int mouseY) {
            if (entryID >= 0 && entryID < configFileSlots.size()) {
                File file = configFileSlots.get(entryID);
                String name = file.getName();
                String dateStr = dateFormat.format(new Date(file.lastModified()));

                int color = entryID == selectedIndex ? 0xFFFF55 : 0xFFFFFF;
                fontRendererObj.drawString(name, p_180791_2_ + 5, p_180791_3_ + 2, color);
                fontRendererObj.drawString(dateStr, p_180791_2_ + 5, p_180791_3_ + 12, 0x888888);
            }
        }
    }
}
