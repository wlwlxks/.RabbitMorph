package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.RabbitConfig;
import com.jiwan.rabbitmorph.RabbitData;
import com.jiwan.rabbitmorph.RabbitUtils;
import com.jiwan.rabbitmorph.client.RabbitPreviewRenderer;
import com.jiwan.rabbitmorph.network.PacketHandler;
import com.jiwan.rabbitmorph.network.PacketRabbitSettings;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiRabbitSettings extends GuiScreen {

    private String selectedType = RabbitConfig.TYPE_NORMAL;
    private GuiTextField fieldHealth;
    private GuiTextField fieldSpeed;
    private GuiTextField fieldJump;
    private GuiTextField fieldFall;

    private GuiRabbitColorPicker pickerBody;
    private GuiRabbitColorPicker pickerEar;
    private GuiRabbitColorPicker pickerEye;
    private GuiRabbitColorPicker pickerTail;

    private float previewYaw = 0.0F;
    private float previewPitch = 0.0F;
    private float previewZoom = 1.0F;
    private float ticksOpen = 0.0F;

    private boolean isDragging = false;
    private int prevMouseX;
    private int prevMouseY;

    @Override
    public void initGui() {
        this.buttonList.clear();

        EntityPlayerSP player = this.mc.thePlayer;
        if (player != null) {
            this.selectedType = RabbitData.getType(player);
        }

        int leftX = 15;
        int topY = 30;

        // Preset type buttons
        int btnWidth = 42;
        int btnHeight = 16;
        this.buttonList.add(new GuiButton(1, leftX, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.normal")));
        this.buttonList.add(new GuiButton(2, leftX + 45, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.brown")));
        this.buttonList.add(new GuiButton(3, leftX + 90, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.black")));
        this.buttonList.add(new GuiButton(4, leftX + 135, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.white")));
        this.buttonList.add(new GuiButton(5, leftX + 180, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.golden")));

        // Stats fields
        int statsY = topY + 24;
        this.fieldHealth = new GuiTextField(100, this.fontRendererObj, leftX + 65, statsY, 40, 14);
        this.fieldSpeed = new GuiTextField(101, this.fontRendererObj, leftX + 65, statsY + 18, 40, 14);
        this.fieldJump = new GuiTextField(102, this.fontRendererObj, leftX + 175, statsY, 40, 14);
        this.fieldFall = new GuiTextField(103, this.fontRendererObj, leftX + 175, statsY + 18, 40, 14);

        double curHealth = player != null ? RabbitData.getHealth(player) : RabbitConfig.DEFAULT_HEALTH;
        double curSpeed = player != null ? RabbitData.getSpeed(player) : RabbitConfig.DEFAULT_SPEED;
        double curJump = player != null ? RabbitData.getJump(player) : RabbitConfig.DEFAULT_JUMP;
        double curFall = player != null ? RabbitData.getFallDamage(player) : RabbitConfig.DEFAULT_FALL;

        this.fieldHealth.setText(String.valueOf(curHealth));
        this.fieldSpeed.setText(String.valueOf(curSpeed));
        this.fieldJump.setText(String.valueOf(curJump));
        this.fieldFall.setText(String.valueOf(curFall));

        // Color pickers
        int colorY = statsY + 42;
        int curBR = player != null ? RabbitData.color(player, "body", "R") : RabbitConfig.NORMAL_BODY_R;
        int curBG = player != null ? RabbitData.color(player, "body", "G") : RabbitConfig.NORMAL_BODY_G;
        int curBB = player != null ? RabbitData.color(player, "body", "B") : RabbitConfig.NORMAL_BODY_B;

        int curER = player != null ? RabbitData.color(player, "ear", "R") : RabbitConfig.NORMAL_EAR_R;
        int curEG = player != null ? RabbitData.color(player, "ear", "G") : RabbitConfig.NORMAL_EAR_G;
        int curEB = player != null ? RabbitData.color(player, "ear", "B") : RabbitConfig.NORMAL_EAR_B;

        int curEyR = player != null ? RabbitData.color(player, "eye", "R") : RabbitConfig.NORMAL_EYE_R;
        int curEyG = player != null ? RabbitData.color(player, "eye", "G") : RabbitConfig.NORMAL_EYE_G;
        int curEyB = player != null ? RabbitData.color(player, "eye", "B") : RabbitConfig.NORMAL_EYE_B;

        int curTR = player != null ? RabbitData.color(player, "tail", "R") : RabbitConfig.NORMAL_TAIL_R;
        int curTG = player != null ? RabbitData.color(player, "tail", "G") : RabbitConfig.NORMAL_TAIL_G;
        int curTB = player != null ? RabbitData.color(player, "tail", "B") : RabbitConfig.NORMAL_TAIL_B;

        this.pickerBody = new GuiRabbitColorPicker(this.fontRendererObj, leftX, colorY, I18n.format("gui.rabbitmorph.body_color"), curBR, curBG, curBB);
        this.pickerEar = new GuiRabbitColorPicker(this.fontRendererObj, leftX, colorY + 18, I18n.format("gui.rabbitmorph.ear_color"), curER, curEG, curEB);
        this.pickerEye = new GuiRabbitColorPicker(this.fontRendererObj, leftX, colorY + 36, I18n.format("gui.rabbitmorph.eye_color"), curEyR, curEyG, curEyB);
        this.pickerTail = new GuiRabbitColorPicker(this.fontRendererObj, leftX, colorY + 54, I18n.format("gui.rabbitmorph.tail_color"), curTR, curTG, curTB);

        // Bottom action buttons
        int actionY = this.height - 28;
        this.buttonList.add(new GuiButton(10, leftX, actionY, 55, 18, I18n.format("gui.rabbitmorph.apply")));
        this.buttonList.add(new GuiButton(11, leftX + 60, actionY, 55, 18, I18n.format("gui.rabbitmorph.cancel")));
        this.buttonList.add(new GuiButton(12, leftX + 120, actionY, 65, 18, I18n.format("gui.rabbitmorph.reset")));
        this.buttonList.add(new GuiButton(13, leftX + 190, actionY, 65, 18, I18n.format("gui.rabbitmorph.reset_preview")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1: selectPreset(RabbitConfig.TYPE_NORMAL); break;
            case 2: selectPreset(RabbitConfig.TYPE_BROWN); break;
            case 3: selectPreset(RabbitConfig.TYPE_BLACK); break;
            case 4: selectPreset(RabbitConfig.TYPE_WHITE); break;
            case 5: selectPreset(RabbitConfig.TYPE_GOLDEN); break;

            case 10: // Apply
                applySettings();
                this.mc.displayGuiScreen(null);
                break;
            case 11: // Cancel
                this.mc.displayGuiScreen(null);
                break;
            case 12: // Reset defaults
                selectPreset(RabbitConfig.TYPE_NORMAL);
                this.fieldHealth.setText(String.valueOf(RabbitConfig.DEFAULT_HEALTH));
                this.fieldSpeed.setText(String.valueOf(RabbitConfig.DEFAULT_SPEED));
                this.fieldJump.setText(String.valueOf(RabbitConfig.DEFAULT_JUMP));
                this.fieldFall.setText(String.valueOf(RabbitConfig.DEFAULT_FALL));
                break;
            case 13: // Reset preview view
                this.previewYaw = 0.0F;
                this.previewPitch = 0.0F;
                this.previewZoom = 1.0F;
                break;
        }
    }

    private void selectPreset(String type) {
        this.selectedType = type;
        if (RabbitConfig.TYPE_NORMAL.equals(type)) {
            this.pickerBody.setRGB(RabbitConfig.NORMAL_BODY_R, RabbitConfig.NORMAL_BODY_G, RabbitConfig.NORMAL_BODY_B);
            this.pickerEar.setRGB(RabbitConfig.NORMAL_EAR_R, RabbitConfig.NORMAL_EAR_G, RabbitConfig.NORMAL_EAR_B);
            this.pickerEye.setRGB(RabbitConfig.NORMAL_EYE_R, RabbitConfig.NORMAL_EYE_G, RabbitConfig.NORMAL_EYE_B);
            this.pickerTail.setRGB(RabbitConfig.NORMAL_TAIL_R, RabbitConfig.NORMAL_TAIL_G, RabbitConfig.NORMAL_TAIL_B);
        } else if (RabbitConfig.TYPE_BROWN.equals(type)) {
            this.pickerBody.setRGB(RabbitConfig.BROWN_BODY_R, RabbitConfig.BROWN_BODY_G, RabbitConfig.BROWN_BODY_B);
            this.pickerEar.setRGB(RabbitConfig.BROWN_EAR_R, RabbitConfig.BROWN_EAR_G, RabbitConfig.BROWN_EAR_B);
            this.pickerEye.setRGB(RabbitConfig.BROWN_EYE_R, RabbitConfig.BROWN_EYE_G, RabbitConfig.BROWN_EYE_B);
            this.pickerTail.setRGB(RabbitConfig.BROWN_TAIL_R, RabbitConfig.BROWN_TAIL_G, RabbitConfig.BROWN_TAIL_B);
        } else if (RabbitConfig.TYPE_BLACK.equals(type)) {
            this.pickerBody.setRGB(RabbitConfig.BLACK_BODY_R, RabbitConfig.BLACK_BODY_G, RabbitConfig.BLACK_BODY_B);
            this.pickerEar.setRGB(RabbitConfig.BLACK_EAR_R, RabbitConfig.BLACK_EAR_G, RabbitConfig.BLACK_EAR_B);
            this.pickerEye.setRGB(RabbitConfig.BLACK_EYE_R, RabbitConfig.BLACK_EYE_G, RabbitConfig.BLACK_EYE_B);
            this.pickerTail.setRGB(RabbitConfig.BLACK_TAIL_R, RabbitConfig.BLACK_TAIL_G, RabbitConfig.BLACK_TAIL_B);
        } else if (RabbitConfig.TYPE_WHITE.equals(type)) {
            this.pickerBody.setRGB(RabbitConfig.WHITE_BODY_R, RabbitConfig.WHITE_BODY_G, RabbitConfig.WHITE_BODY_B);
            this.pickerEar.setRGB(RabbitConfig.WHITE_EAR_R, RabbitConfig.WHITE_EAR_G, RabbitConfig.WHITE_EAR_B);
            this.pickerEye.setRGB(RabbitConfig.WHITE_EYE_R, RabbitConfig.WHITE_EYE_G, RabbitConfig.WHITE_EYE_B);
            this.pickerTail.setRGB(RabbitConfig.WHITE_TAIL_R, RabbitConfig.WHITE_TAIL_G, RabbitConfig.WHITE_TAIL_B);
        } else if (RabbitConfig.TYPE_GOLDEN.equals(type)) {
            this.pickerBody.setRGB(RabbitConfig.GOLDEN_BODY_R, RabbitConfig.GOLDEN_BODY_G, RabbitConfig.GOLDEN_BODY_B);
            this.pickerEar.setRGB(RabbitConfig.GOLDEN_EAR_R, RabbitConfig.GOLDEN_EAR_G, RabbitConfig.GOLDEN_EAR_B);
            this.pickerEye.setRGB(RabbitConfig.GOLDEN_EYE_R, RabbitConfig.GOLDEN_EYE_G, RabbitConfig.GOLDEN_EYE_B);
            this.pickerTail.setRGB(RabbitConfig.GOLDEN_TAIL_R, RabbitConfig.GOLDEN_TAIL_G, RabbitConfig.GOLDEN_TAIL_B);
        }
    }

    private void applySettings() {
        double health = RabbitUtils.parseDoubleSafe(this.fieldHealth.getText(), RabbitConfig.DEFAULT_HEALTH);
        double speed = RabbitUtils.parseDoubleSafe(this.fieldSpeed.getText(), RabbitConfig.DEFAULT_SPEED);
        double jump = RabbitUtils.parseDoubleSafe(this.fieldJump.getText(), RabbitConfig.DEFAULT_JUMP);
        double fall = RabbitUtils.parseDoubleSafe(this.fieldFall.getText(), RabbitConfig.DEFAULT_FALL);

        PacketRabbitSettings pkt = new PacketRabbitSettings(
                this.selectedType, health, speed, jump, fall,
                this.pickerBody.getR(), this.pickerBody.getG(), this.pickerBody.getB(),
                this.pickerEar.getR(), this.pickerEar.getG(), this.pickerEar.getB(),
                this.pickerEye.getR(), this.pickerEye.getG(), this.pickerEye.getB(),
                this.pickerTail.getR(), this.pickerTail.getG(), this.pickerTail.getB()
        );
        PacketHandler.INSTANCE.sendToServer(pkt);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.fieldHealth.textboxKeyTyped(typedChar, keyCode);
        this.fieldSpeed.textboxKeyTyped(typedChar, keyCode);
        this.fieldJump.textboxKeyTyped(typedChar, keyCode);
        this.fieldFall.textboxKeyTyped(typedChar, keyCode);

        this.pickerBody.keyTyped(typedChar, keyCode);
        this.pickerEar.keyTyped(typedChar, keyCode);
        this.pickerEye.keyTyped(typedChar, keyCode);
        this.pickerTail.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldHealth.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldSpeed.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldJump.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldFall.mouseClicked(mouseX, mouseY, mouseButton);

        this.pickerBody.mouseClicked(mouseX, mouseY, mouseButton);
        this.pickerEar.mouseClicked(mouseX, mouseY, mouseButton);
        this.pickerEye.mouseClicked(mouseX, mouseY, mouseButton);
        this.pickerTail.mouseClicked(mouseX, mouseY, mouseButton);

        // Preview drag check
        int previewX = 265;
        int previewY = 30;
        int previewW = this.width - previewX - 15;
        int previewH = this.height - 65;

        if (mouseButton == 0 && mouseX >= previewX && mouseX <= previewX + previewW && mouseY >= previewY && mouseY <= previewY + previewH) {
            this.isDragging = true;
            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.isDragging = false;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (this.isDragging && clickedMouseButton == 0) {
            int deltaX = mouseX - this.prevMouseX;
            int deltaY = mouseY - this.prevMouseY;

            this.previewYaw += deltaX * 1.2F;
            this.previewPitch += deltaY * 0.8F;

            // Clamp pitch to sensible range (-60 to +60 degrees)
            if (this.previewPitch < -60.0F) this.previewPitch = -60.0F;
            if (this.previewPitch > 60.0F) this.previewPitch = 60.0F;

            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            if (dWheel > 0) {
                this.previewZoom += 0.1F;
            } else {
                this.previewZoom -= 0.1F;
            }
            if (this.previewZoom < 0.4F) this.previewZoom = 0.4F;
            if (this.previewZoom > 2.5F) this.previewZoom = 2.5F;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.ticksOpen += partialTicks;

        // Title
        String title = I18n.format("gui.rabbitmorph.title");
        int titleX = 15;
        this.fontRendererObj.drawStringWithShadow(title, titleX, 10, 0xFFFF55);

        int leftX = 15;
        int topY = 30;
        int statsY = topY + 24;

        this.fontRendererObj.drawString(I18n.format("gui.rabbitmorph.health"), leftX, statsY + 3, 0xCCCCCC);
        this.fieldHealth.drawTextBox();

        this.fontRendererObj.drawString(I18n.format("gui.rabbitmorph.speed"), leftX, statsY + 21, 0xCCCCCC);
        this.fieldSpeed.drawTextBox();

        this.fontRendererObj.drawString(I18n.format("gui.rabbitmorph.jump"), leftX + 115, statsY + 3, 0xCCCCCC);
        this.fieldJump.drawTextBox();

        this.fontRendererObj.drawString(I18n.format("gui.rabbitmorph.fall"), leftX + 115, statsY + 21, 0xCCCCCC);
        this.fieldFall.drawTextBox();

        int colorY = statsY + 42;
        this.pickerBody.draw(this.fontRendererObj, leftX, colorY);
        this.pickerEar.draw(this.fontRendererObj, leftX, colorY + 18);
        this.pickerEye.draw(this.fontRendererObj, leftX, colorY + 36);
        this.pickerTail.draw(this.fontRendererObj, leftX, colorY + 54);

        // Draw buttons
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Draw Live 360 Preview
        int previewX = 265;
        int previewY = 30;
        int previewW = this.width - previewX - 15;
        int previewH = this.height - 65;

        if (previewW > 60 && previewH > 60) {
            RabbitPreviewRenderer.renderPreview(previewX, previewY, previewW, previewH,
                    this.previewYaw, this.previewPitch, this.previewZoom,
                    this.pickerBody.getR(), this.pickerBody.getG(), this.pickerBody.getB(),
                    this.pickerEar.getR(), this.pickerEar.getG(), this.pickerEar.getB(),
                    this.pickerEye.getR(), this.pickerEye.getG(), this.pickerEye.getB(),
                    this.pickerTail.getR(), this.pickerTail.getG(), this.pickerTail.getB(),
                    this.ticksOpen);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
