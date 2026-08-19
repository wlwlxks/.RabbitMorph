package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitConfig;
import com.jiwan.rabbitmorph.RabbitData;
import com.jiwan.rabbitmorph.network.PacketHandler;
import com.jiwan.rabbitmorph.network.PacketRabbitSettings;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiRabbitSettings extends GuiScreen {

    private int activeTab = 0; // 0=Stats, 1=Scales, 2=Colors, 3=Effects, 4=Presets

    private GuiTabStats tabStats;
    private GuiTabScales tabScales;
    private GuiTabColors tabColors;
    private GuiTabEffects tabEffects;
    private GuiTabPresets tabPresets;
    private GuiPreviewPanel previewPanel;

    private float ticksOpen = 0.0F;

    private GuiButton btnTabStats, btnTabScales, btnTabColors, btnTabEffects, btnTabPresets;

    @Override
    public void initGui() {
        this.buttonList.clear();

        int leftX = 8;
        int topY = 16;

        // Top Navigation Tabs
        int tabW = 45, tabH = 13;
        btnTabStats = new GuiButton(1, leftX, topY, tabW, tabH, "Stats");
        btnTabScales = new GuiButton(2, leftX + 47, topY, tabW, tabH, "Scales");
        btnTabColors = new GuiButton(3, leftX + 94, topY, tabW, tabH, "Colors");
        btnTabEffects = new GuiButton(4, leftX + 141, topY, tabW, tabH, "Effects");
        btnTabPresets = new GuiButton(5, leftX + 188, topY, tabW, tabH, "Presets");

        this.buttonList.add(btnTabStats);
        this.buttonList.add(btnTabScales);
        this.buttonList.add(btnTabColors);
        this.buttonList.add(btnTabEffects);
        this.buttonList.add(btnTabPresets);

        // Content Area Y starts at topY + 24
        int contentY = topY + 24;

        this.tabStats = new GuiTabStats(this.fontRendererObj, leftX, contentY);
        this.tabScales = new GuiTabScales(this.fontRendererObj, leftX, contentY);
        this.tabColors = new GuiTabColors(this.fontRendererObj, leftX, contentY);
        this.tabEffects = new GuiTabEffects(this.fontRendererObj, leftX, contentY);
        this.tabPresets = new GuiTabPresets(this.fontRendererObj, leftX, contentY + 12);

        this.tabEffects.initGui(this.buttonList, 30);
        this.tabPresets.initGui(this.buttonList, 40);

        // Right side preview panel
        int previewX = 246;
        int previewY = 16;
        int previewW = Math.max(60, this.width - previewX - 8);
        int previewH = Math.max(60, this.height - 24);
        this.previewPanel = new GuiPreviewPanel(previewX, previewY, previewW, previewH);
        this.previewPanel.initGui(this.buttonList, 20);

        // Bottom action buttons
        int actionY = Math.max(contentY + 70, this.height - 18);
        this.buttonList.add(new GuiButton(10, leftX, actionY, 42, 14, I18n.format("gui.rabbitmorph.apply")));
        this.buttonList.add(new GuiButton(11, leftX + 44, actionY, 42, 14, I18n.format("gui.rabbitmorph.cancel")));
        this.buttonList.add(new GuiButton(12, leftX + 88, actionY, 42, 14, I18n.format("gui.rabbitmorph.reset")));
        this.buttonList.add(new GuiButton(13, leftX + 132, actionY, 50, 14, I18n.format("gui.rabbitmorph.save_json")));
        this.buttonList.add(new GuiButton(14, leftX + 184, actionY, 50, 14, I18n.format("gui.rabbitmorph.load_json")));

        loadCurrentPlayerData();
        updateTabVisibility();
    }

    private void loadCurrentPlayerData() {
        EntityPlayerSP player = this.mc.thePlayer;
        if (player != null) {
            this.tabPresets.selectedType = RabbitData.getType(player);
            this.tabEffects.isGlowing = RabbitData.isGlowing(player);

            this.tabStats.fieldHealth.setText(String.valueOf(RabbitData.getHealth(player)));
            this.tabStats.fieldSpeed.setText(String.valueOf(RabbitData.getSpeed(player)));
            this.tabStats.fieldJump.setText(String.valueOf(RabbitData.getJump(player)));
            this.tabStats.fieldFall.setText(String.valueOf(RabbitData.getFallDamage(player)));
            this.tabStats.fieldStep.setText(String.valueOf(RabbitData.getStepHeight(player)));
            this.tabStats.fieldKnockback.setText(String.valueOf(RabbitData.getKnockbackRes(player)));

            this.tabScales.fieldScaleOverall.setText(String.valueOf(RabbitData.getScale(player, "overall")));
            this.tabScales.fieldScaleHead.setText(String.valueOf(RabbitData.getScale(player, "head")));
            this.tabScales.fieldScaleEar.setText(String.valueOf(RabbitData.getScale(player, "ear")));
            this.tabScales.fieldScaleBody.setText(String.valueOf(RabbitData.getScale(player, "body")));
            this.tabScales.fieldScaleLegs.setText(String.valueOf(RabbitData.getScale(player, "legs")));
            this.tabScales.fieldScaleTail.setText(String.valueOf(RabbitData.getScale(player, "tail")));
            this.tabScales.fieldScaleNose.setText(String.valueOf(RabbitData.getScale(player, "nose")));
            this.tabScales.fieldScaleEye.setText(String.valueOf(RabbitData.getScale(player, "eye")));

            this.tabColors.setPresetColors(
                    RabbitData.color(player, "body", "R"), RabbitData.color(player, "body", "G"), RabbitData.color(player, "body", "B"),
                    RabbitData.color(player, "ear", "R"), RabbitData.color(player, "ear", "G"), RabbitData.color(player, "ear", "B"),
                    RabbitData.color(player, "eye", "R"), RabbitData.color(player, "eye", "G"), RabbitData.color(player, "eye", "B"),
                    RabbitData.color(player, "tail", "R"), RabbitData.color(player, "tail", "G"), RabbitData.color(player, "tail", "B")
            );
        }
    }

    private void updateTabVisibility() {
        btnTabStats.enabled = activeTab != 0;
        btnTabScales.enabled = activeTab != 1;
        btnTabColors.enabled = activeTab != 2;
        btnTabEffects.enabled = activeTab != 3;
        btnTabPresets.enabled = activeTab != 4;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id >= 1 && button.id <= 5) {
            activeTab = button.id - 1;
            updateTabVisibility();
            return;
        }

        if (previewPanel.actionPerformed(button)) return;
        if (tabEffects.actionPerformed(button)) return;

        String presetType = tabPresets.actionPerformed(button);
        if (presetType != null) {
            selectPreset(presetType);
            return;
        }

        switch (button.id) {
            case 10: // Apply
                applySettings();
                this.mc.displayGuiScreen(null);
                break;
            case 11: // Cancel
                this.mc.displayGuiScreen(null);
                break;
            case 12: // Reset defaults
                selectPreset(RabbitConfig.TYPE_NORMAL);
                this.tabEffects.isGlowing = false;
                this.tabStats.fieldHealth.setText(String.valueOf(RabbitConfig.DEFAULT_HEALTH));
                this.tabStats.fieldSpeed.setText(String.valueOf(RabbitConfig.DEFAULT_SPEED));
                this.tabStats.fieldJump.setText(String.valueOf(RabbitConfig.DEFAULT_JUMP));
                this.tabStats.fieldFall.setText(String.valueOf(RabbitConfig.DEFAULT_FALL));
                this.tabStats.fieldStep.setText("0.6");
                this.tabStats.fieldKnockback.setText("0.0");
                break;
            case 13: // Save JSON config
                saveJsonConfig();
                break;
            case 14: // Load JSON config
                loadJsonConfig();
                break;
        }
    }

    private void selectPreset(String type) {
        if (RabbitConfig.TYPE_NORMAL.equals(type)) {
            tabColors.setPresetColors(RabbitConfig.NORMAL_BODY_R, RabbitConfig.NORMAL_BODY_G, RabbitConfig.NORMAL_BODY_B,
                    RabbitConfig.NORMAL_EAR_R, RabbitConfig.NORMAL_EAR_G, RabbitConfig.NORMAL_EAR_B,
                    RabbitConfig.NORMAL_EYE_R, RabbitConfig.NORMAL_EYE_G, RabbitConfig.NORMAL_EYE_B,
                    RabbitConfig.NORMAL_TAIL_R, RabbitConfig.NORMAL_TAIL_G, RabbitConfig.NORMAL_TAIL_B);
        } else if (RabbitConfig.TYPE_BROWN.equals(type)) {
            tabColors.setPresetColors(RabbitConfig.BROWN_BODY_R, RabbitConfig.BROWN_BODY_G, RabbitConfig.BROWN_BODY_B,
                    RabbitConfig.BROWN_EAR_R, RabbitConfig.BROWN_EAR_G, RabbitConfig.BROWN_EAR_B,
                    RabbitConfig.BROWN_EYE_R, RabbitConfig.BROWN_EYE_G, RabbitConfig.BROWN_EYE_B,
                    RabbitConfig.BROWN_TAIL_R, RabbitConfig.BROWN_TAIL_G, RabbitConfig.BROWN_TAIL_B);
        } else if (RabbitConfig.TYPE_BLACK.equals(type)) {
            tabColors.setPresetColors(RabbitConfig.BLACK_BODY_R, RabbitConfig.BLACK_BODY_G, RabbitConfig.BLACK_BODY_B,
                    RabbitConfig.BLACK_EAR_R, RabbitConfig.BLACK_EAR_G, RabbitConfig.BLACK_EAR_B,
                    RabbitConfig.BLACK_EYE_R, RabbitConfig.BLACK_EYE_G, RabbitConfig.BLACK_EYE_B,
                    RabbitConfig.BLACK_TAIL_R, RabbitConfig.BLACK_TAIL_G, RabbitConfig.BLACK_TAIL_B);
        } else if (RabbitConfig.TYPE_WHITE.equals(type)) {
            tabColors.setPresetColors(RabbitConfig.WHITE_BODY_R, RabbitConfig.WHITE_BODY_G, RabbitConfig.WHITE_BODY_B,
                    RabbitConfig.WHITE_EAR_R, RabbitConfig.WHITE_EAR_G, RabbitConfig.WHITE_EAR_B,
                    RabbitConfig.WHITE_EYE_R, RabbitConfig.WHITE_EYE_G, RabbitConfig.WHITE_EYE_B,
                    RabbitConfig.WHITE_TAIL_R, RabbitConfig.WHITE_TAIL_G, RabbitConfig.WHITE_TAIL_B);
        } else if (RabbitConfig.TYPE_GOLDEN.equals(type)) {
            tabColors.setPresetColors(RabbitConfig.GOLDEN_BODY_R, RabbitConfig.GOLDEN_BODY_G, RabbitConfig.GOLDEN_BODY_B,
                    RabbitConfig.GOLDEN_EAR_R, RabbitConfig.GOLDEN_EAR_G, RabbitConfig.GOLDEN_EAR_B,
                    RabbitConfig.GOLDEN_EYE_R, RabbitConfig.GOLDEN_EYE_G, RabbitConfig.GOLDEN_EYE_B,
                    RabbitConfig.GOLDEN_TAIL_R, RabbitConfig.GOLDEN_TAIL_G, RabbitConfig.GOLDEN_TAIL_B);
        }
    }

    private void saveJsonConfig() {
        RabbitConfig.RabbitConfigData data = new RabbitConfig.RabbitConfigData();
        data.type = this.tabPresets.selectedType;
        data.isGlowing = this.tabEffects.isGlowing;
        data.health = tabStats.getHealth();
        data.speed = tabStats.getSpeed();
        data.jump = tabStats.getJump();
        data.fall = tabStats.getFall();

        data.scaleOverall = tabScales.getScaleOverall();
        data.scaleHead = tabScales.getScaleHead();
        data.scaleEar = tabScales.getScaleEar();
        data.scaleBody = tabScales.getScaleBody();
        data.scaleLegs = tabScales.getScaleLegs();
        data.scaleTail = tabScales.getScaleTail();

        data.bodyR = tabColors.pickerBody.getR(); data.bodyG = tabColors.pickerBody.getG(); data.bodyB = tabColors.pickerBody.getB(); data.bodyA = tabColors.pickerBody.getA();
        data.earR = tabColors.pickerEar.getR(); data.earG = tabColors.pickerEar.getG(); data.earB = tabColors.pickerEar.getB(); data.earA = tabColors.pickerEar.getA();
        data.eyeR = tabColors.pickerEye.getR(); data.eyeG = tabColors.pickerEye.getG(); data.eyeB = tabColors.pickerEye.getB(); data.eyeA = tabColors.pickerEye.getA();
        data.tailR = tabColors.pickerTail.getR(); data.tailG = tabColors.pickerTail.getG(); data.tailB = tabColors.pickerTail.getB(); data.tailA = tabColors.pickerTail.getA();

        RabbitConfig.saveJsonConfig(data);
    }

    private void loadJsonConfig() {
        RabbitConfig.RabbitConfigData data = RabbitConfig.loadLatestJsonConfig();
        if (data != null) {
            this.tabPresets.selectedType = data.type != null ? data.type : RabbitConfig.TYPE_NORMAL;
            this.tabEffects.isGlowing = data.isGlowing;
            this.tabStats.fieldHealth.setText(String.valueOf(data.health));
            this.tabStats.fieldSpeed.setText(String.valueOf(data.speed));
            this.tabStats.fieldJump.setText(String.valueOf(data.jump));
            this.tabStats.fieldFall.setText(String.valueOf(data.fall));

            this.tabScales.fieldScaleOverall.setText(String.valueOf(data.scaleOverall));
            this.tabScales.fieldScaleHead.setText(String.valueOf(data.scaleHead));
            this.tabScales.fieldScaleEar.setText(String.valueOf(data.scaleEar));
            this.tabScales.fieldScaleBody.setText(String.valueOf(data.scaleBody));
            this.tabScales.fieldScaleLegs.setText(String.valueOf(data.scaleLegs));
            this.tabScales.fieldScaleTail.setText(String.valueOf(data.scaleTail));

            this.tabColors.pickerBody.setRGBA(data.bodyR, data.bodyG, data.bodyB, data.bodyA);
            this.tabColors.pickerEar.setRGBA(data.earR, data.earG, data.earB, data.earA);
            this.tabColors.pickerEye.setRGBA(data.eyeR, data.eyeG, data.eyeB, data.eyeA);
            this.tabColors.pickerTail.setRGBA(data.tailR, data.tailG, data.tailB, data.tailA);
        }
    }

    private void applySettings() {
        PacketRabbitSettings pkt = new PacketRabbitSettings(
                this.tabPresets.selectedType, this.tabEffects.isGlowing,
                tabStats.getHealth(), tabStats.getSpeed(), tabStats.getJump(), tabStats.getFall(),
                tabScales.getScaleOverall(), tabScales.getScaleHead(), tabScales.getScaleEar(), tabScales.getScaleBody(), tabScales.getScaleLegs(), tabScales.getScaleTail(),
                tabColors.pickerBody.getR(), tabColors.pickerBody.getG(), tabColors.pickerBody.getB(), tabColors.pickerBody.getA(),
                tabColors.pickerEar.getR(), tabColors.pickerEar.getG(), tabColors.pickerEar.getB(), tabColors.pickerEar.getA(),
                tabColors.pickerEye.getR(), tabColors.pickerEye.getG(), tabColors.pickerEye.getB(), tabColors.pickerEye.getA(),
                tabColors.pickerTail.getR(), tabColors.pickerTail.getG(), tabColors.pickerTail.getB(), tabColors.pickerTail.getA()
        );
        PacketHandler.INSTANCE.sendToServer(pkt);

        saveJsonConfig();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (activeTab == 0) tabStats.keyTyped(typedChar, keyCode);
        else if (activeTab == 1) tabScales.keyTyped(typedChar, keyCode);
        else if (activeTab == 2) tabColors.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (activeTab == 0) tabStats.mouseClicked(mouseX, mouseY, mouseButton);
        else if (activeTab == 1) tabScales.mouseClicked(mouseX, mouseY, mouseButton);
        else if (activeTab == 2) tabColors.mouseClicked(mouseX, mouseY, mouseButton);

        previewPanel.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (activeTab == 2) tabColors.mouseReleased(mouseX, mouseY, state);
        previewPanel.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        previewPanel.mouseClickMove(mouseX, mouseY, clickedMouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        previewPanel.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.ticksOpen += partialTicks;

        String title = I18n.format("gui.rabbitmorph.title");
        this.fontRendererObj.drawStringWithShadow(title, 8, 4, 0xFFFF55);

        if (activeTab == 0) tabStats.draw(mouseX, mouseY);
        else if (activeTab == 1) tabScales.draw(mouseX, mouseY);
        else if (activeTab == 2) tabColors.draw(this.fontRendererObj, mouseX, mouseY);
        else if (activeTab == 3) tabEffects.draw(mouseX, mouseY);
        else if (activeTab == 4) tabPresets.draw(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);

        previewPanel.draw(this.fontRendererObj, ticksOpen,
                tabScales.getScaleOverall(), tabScales.getScaleHead(), tabScales.getScaleEar(), tabScales.getScaleBody(), tabScales.getScaleLegs(), tabScales.getScaleTail(),
                tabColors.pickerBody.getR(), tabColors.pickerBody.getG(), tabColors.pickerBody.getB(), tabColors.pickerBody.getA(),
                tabColors.pickerEar.getR(), tabColors.pickerEar.getG(), tabColors.pickerEar.getB(), tabColors.pickerEar.getA(),
                tabColors.pickerEye.getR(), tabColors.pickerEye.getG(), tabColors.pickerEye.getB(), tabColors.pickerEye.getA(),
                tabColors.pickerTail.getR(), tabColors.pickerTail.getG(), tabColors.pickerTail.getB(), tabColors.pickerTail.getA());
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
