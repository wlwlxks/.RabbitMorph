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
    private GuiTextField fieldHealth, fieldSpeed, fieldJump, fieldFall;
    private GuiTextField fieldScaleOverall, fieldScaleHead, fieldScaleEar, fieldScaleBody, fieldScaleLegs, fieldScaleTail;

    private GuiRabbitColorPicker pickerBody, pickerEar, pickerEye, pickerTail;

    private float previewYaw = 0.0F;
    private float previewPitch = 0.0F;
    private float previewZoom = 1.0F;
    private float ticksOpen = 0.0F;
    private int previewPose = 0; // 0=Idle, 1=Walk, 2=Jump

    private boolean isDraggingPreview = false;
    private int prevMouseX, prevMouseY;

    @Override
    public void initGui() {
        this.buttonList.clear();

        EntityPlayerSP player = this.mc.thePlayer;
        if (player != null) {
            this.selectedType = RabbitData.getType(player);
        }

        int leftX = 10;
        int topY = 22;

        // Preset type buttons
        int btnWidth = 46;
        int btnHeight = 14;
        this.buttonList.add(new GuiButton(1, leftX, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.normal")));
        this.buttonList.add(new GuiButton(2, leftX + 48, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.brown")));
        this.buttonList.add(new GuiButton(3, leftX + 96, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.black")));
        this.buttonList.add(new GuiButton(4, leftX + 144, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.white")));
        this.buttonList.add(new GuiButton(5, leftX + 192, topY, btnWidth, btnHeight, I18n.format("gui.rabbitmorph.preset.golden")));

        // Stats fields
        int statsY = topY + 18;
        this.fieldHealth = new GuiTextField(100, this.fontRendererObj, leftX + 45, statsY, 32, 12);
        this.fieldSpeed = new GuiTextField(101, this.fontRendererObj, leftX + 45, statsY + 15, 32, 12);
        this.fieldJump = new GuiTextField(102, this.fontRendererObj, leftX + 145, statsY, 32, 12);
        this.fieldFall = new GuiTextField(103, this.fontRendererObj, leftX + 145, statsY + 15, 32, 12);

        double curHealth = player != null ? RabbitData.getHealth(player) : RabbitConfig.DEFAULT_HEALTH;
        double curSpeed = player != null ? RabbitData.getSpeed(player) : RabbitConfig.DEFAULT_SPEED;
        double curJump = player != null ? RabbitData.getJump(player) : RabbitConfig.DEFAULT_JUMP;
        double curFall = player != null ? RabbitData.getFallDamage(player) : RabbitConfig.DEFAULT_FALL;

        this.fieldHealth.setText(String.valueOf(curHealth));
        this.fieldSpeed.setText(String.valueOf(curSpeed));
        this.fieldJump.setText(String.valueOf(curJump));
        this.fieldFall.setText(String.valueOf(curFall));

        // Detailed Part Scales fields
        int scalesY = statsY + 30;
        this.fieldScaleOverall = new GuiTextField(200, this.fontRendererObj, leftX + 45, scalesY, 28, 12);
        this.fieldScaleHead = new GuiTextField(201, this.fontRendererObj, leftX + 115, scalesY, 28, 12);
        this.fieldScaleEar = new GuiTextField(202, this.fontRendererObj, leftX + 185, scalesY, 28, 12);
        this.fieldScaleBody = new GuiTextField(203, this.fontRendererObj, leftX + 45, scalesY + 14, 28, 12);
        this.fieldScaleLegs = new GuiTextField(204, this.fontRendererObj, leftX + 115, scalesY + 14, 28, 12);
        this.fieldScaleTail = new GuiTextField(205, this.fontRendererObj, leftX + 185, scalesY + 14, 28, 12);

        this.fieldScaleOverall.setText(String.valueOf(player != null ? RabbitData.getScale(player, "overall") : 1.0F));
        this.fieldScaleHead.setText(String.valueOf(player != null ? RabbitData.getScale(player, "head") : 1.0F));
        this.fieldScaleEar.setText(String.valueOf(player != null ? RabbitData.getScale(player, "ear") : 1.0F));
        this.fieldScaleBody.setText(String.valueOf(player != null ? RabbitData.getScale(player, "body") : 1.0F));
        this.fieldScaleLegs.setText(String.valueOf(player != null ? RabbitData.getScale(player, "legs") : 1.0F));
        this.fieldScaleTail.setText(String.valueOf(player != null ? RabbitData.getScale(player, "tail") : 1.0F));

        // Color pickers with RGBA sliders
        int colorY = scalesY + 30;
        int curBR = player != null ? RabbitData.color(player, "body", "R") : RabbitConfig.NORMAL_BODY_R;
        int curBG = player != null ? RabbitData.color(player, "body", "G") : RabbitConfig.NORMAL_BODY_G;
        int curBB = player != null ? RabbitData.color(player, "body", "B") : RabbitConfig.NORMAL_BODY_B;
        int curBA = player != null ? RabbitData.color(player, "body", "A") : 255;

        int curER = player != null ? RabbitData.color(player, "ear", "R") : RabbitConfig.NORMAL_EAR_R;
        int curEG = player != null ? RabbitData.color(player, "ear", "G") : RabbitConfig.NORMAL_EAR_G;
        int curEB = player != null ? RabbitData.color(player, "ear", "B") : RabbitConfig.NORMAL_EAR_B;
        int curEA = player != null ? RabbitData.color(player, "ear", "A") : 255;

        int curEyR = player != null ? RabbitData.color(player, "eye", "R") : RabbitConfig.NORMAL_EYE_R;
        int curEyG = player != null ? RabbitData.color(player, "eye", "G") : RabbitConfig.NORMAL_EYE_G;
        int curEyB = player != null ? RabbitData.color(player, "eye", "B") : RabbitConfig.NORMAL_EYE_B;
        int curEyA = player != null ? RabbitData.color(player, "eye", "A") : 255;

        int curTR = player != null ? RabbitData.color(player, "tail", "R") : RabbitConfig.NORMAL_TAIL_R;
        int curTG = player != null ? RabbitData.color(player, "tail", "G") : RabbitConfig.NORMAL_TAIL_G;
        int curTB = player != null ? RabbitData.color(player, "tail", "B") : RabbitConfig.NORMAL_TAIL_B;
        int curTA = player != null ? RabbitData.color(player, "tail", "A") : 255;

        this.pickerBody = new GuiRabbitColorPicker(this.fontRendererObj, leftX, colorY, I18n.format("gui.rabbitmorph.body_color"), curBR, curBG, curBB, curBA);
        this.pickerEar = new GuiRabbitColorPicker(this.fontRendererObj, leftX, colorY + 16, I18n.format("gui.rabbitmorph.ear_color"), curER, curEG, curEB, curEA);
        this.pickerEye = new GuiRabbitColorPicker(this.fontRendererObj, leftX, colorY + 32, I18n.format("gui.rabbitmorph.eye_color"), curEyR, curEyG, curEyB, curEyA);
        this.pickerTail = new GuiRabbitColorPicker(this.fontRendererObj, leftX, colorY + 48, I18n.format("gui.rabbitmorph.tail_color"), curTR, curTG, curTB, curTA);

        // Preview pose button
        this.buttonList.add(new GuiButton(20, 265, 8, 60, 14, "Pose: Idle"));

        // Bottom action buttons
        int actionY = this.height - 20;
        this.buttonList.add(new GuiButton(10, leftX, actionY, 45, 16, I18n.format("gui.rabbitmorph.apply")));
        this.buttonList.add(new GuiButton(11, leftX + 47, actionY, 45, 16, I18n.format("gui.rabbitmorph.cancel")));
        this.buttonList.add(new GuiButton(12, leftX + 94, actionY, 45, 16, I18n.format("gui.rabbitmorph.reset")));
        this.buttonList.add(new GuiButton(13, leftX + 141, actionY, 52, 16, I18n.format("gui.rabbitmorph.save_json")));
        this.buttonList.add(new GuiButton(14, leftX + 195, actionY, 52, 16, I18n.format("gui.rabbitmorph.load_json")));
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
                this.fieldScaleOverall.setText("1.0"); this.fieldScaleHead.setText("1.0"); this.fieldScaleEar.setText("1.0");
                this.fieldScaleBody.setText("1.0"); this.fieldScaleLegs.setText("1.0"); this.fieldScaleTail.setText("1.0");
                break;
            case 13: // Save JSON config
                saveJsonConfig();
                break;
            case 14: // Load JSON config
                loadJsonConfig();
                break;
            case 20: // Pose toggle
                this.previewPose = (this.previewPose + 1) % 3;
                String poseName = this.previewPose == 0 ? "Pose: Idle" : (this.previewPose == 1 ? "Pose: Walk" : "Pose: Jump");
                button.displayString = poseName;
                break;
        }
    }

    private void saveJsonConfig() {
        RabbitConfig.RabbitConfigData data = new RabbitConfig.RabbitConfigData();
        data.type = this.selectedType;
        data.health = RabbitUtils.parseDoubleSafe(this.fieldHealth.getText(), RabbitConfig.DEFAULT_HEALTH);
        data.speed = RabbitUtils.parseDoubleSafe(this.fieldSpeed.getText(), RabbitConfig.DEFAULT_SPEED);
        data.jump = RabbitUtils.parseDoubleSafe(this.fieldJump.getText(), RabbitConfig.DEFAULT_JUMP);
        data.fall = RabbitUtils.parseDoubleSafe(this.fieldFall.getText(), RabbitConfig.DEFAULT_FALL);

        data.scaleOverall = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleOverall.getText(), 1.0D);
        data.scaleHead = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleHead.getText(), 1.0D);
        data.scaleEar = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleEar.getText(), 1.0D);
        data.scaleBody = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleBody.getText(), 1.0D);
        data.scaleLegs = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleLegs.getText(), 1.0D);
        data.scaleTail = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleTail.getText(), 1.0D);

        data.bodyR = this.pickerBody.getR(); data.bodyG = this.pickerBody.getG(); data.bodyB = this.pickerBody.getB(); data.bodyA = this.pickerBody.getA();
        data.earR = this.pickerEar.getR(); data.earG = this.pickerEar.getG(); data.earB = this.pickerEar.getB(); data.earA = this.pickerEar.getA();
        data.eyeR = this.pickerEye.getR(); data.eyeG = this.pickerEye.getG(); data.eyeB = this.pickerEye.getB(); data.eyeA = this.pickerEye.getA();
        data.tailR = this.pickerTail.getR(); data.tailG = this.pickerTail.getG(); data.tailB = this.pickerTail.getB(); data.tailA = this.pickerTail.getA();

        RabbitConfig.saveJsonConfig(data);
    }

    private void loadJsonConfig() {
        RabbitConfig.RabbitConfigData data = RabbitConfig.loadLatestJsonConfig();
        if (data != null) {
            this.selectedType = data.type != null ? data.type : RabbitConfig.TYPE_NORMAL;
            this.fieldHealth.setText(String.valueOf(data.health));
            this.fieldSpeed.setText(String.valueOf(data.speed));
            this.fieldJump.setText(String.valueOf(data.jump));
            this.fieldFall.setText(String.valueOf(data.fall));

            this.fieldScaleOverall.setText(String.valueOf(data.scaleOverall));
            this.fieldScaleHead.setText(String.valueOf(data.scaleHead));
            this.fieldScaleEar.setText(String.valueOf(data.scaleEar));
            this.fieldScaleBody.setText(String.valueOf(data.scaleBody));
            this.fieldScaleLegs.setText(String.valueOf(data.scaleLegs));
            this.fieldScaleTail.setText(String.valueOf(data.scaleTail));

            this.pickerBody.setRGBA(data.bodyR, data.bodyG, data.bodyB, data.bodyA);
            this.pickerEar.setRGBA(data.earR, data.earG, data.earB, data.earA);
            this.pickerEye.setRGBA(data.eyeR, data.eyeG, data.eyeB, data.eyeA);
            this.pickerTail.setRGBA(data.tailR, data.tailG, data.tailB, data.tailA);
        }
    }

    private void selectPreset(String type) {
        this.selectedType = type;
        if (RabbitConfig.TYPE_NORMAL.equals(type)) {
            this.pickerBody.setRGBA(RabbitConfig.NORMAL_BODY_R, RabbitConfig.NORMAL_BODY_G, RabbitConfig.NORMAL_BODY_B, 255);
            this.pickerEar.setRGBA(RabbitConfig.NORMAL_EAR_R, RabbitConfig.NORMAL_EAR_G, RabbitConfig.NORMAL_EAR_B, 255);
            this.pickerEye.setRGBA(RabbitConfig.NORMAL_EYE_R, RabbitConfig.NORMAL_EYE_G, RabbitConfig.NORMAL_EYE_B, 255);
            this.pickerTail.setRGBA(RabbitConfig.NORMAL_TAIL_R, RabbitConfig.NORMAL_TAIL_G, RabbitConfig.NORMAL_TAIL_B, 255);
        } else if (RabbitConfig.TYPE_BROWN.equals(type)) {
            this.pickerBody.setRGBA(RabbitConfig.BROWN_BODY_R, RabbitConfig.BROWN_BODY_G, RabbitConfig.BROWN_BODY_B, 255);
            this.pickerEar.setRGBA(RabbitConfig.BROWN_EAR_R, RabbitConfig.BROWN_EAR_G, RabbitConfig.BROWN_EAR_B, 255);
            this.pickerEye.setRGBA(RabbitConfig.BROWN_EYE_R, RabbitConfig.BROWN_EYE_G, RabbitConfig.BROWN_EYE_B, 255);
            this.pickerTail.setRGBA(RabbitConfig.BROWN_TAIL_R, RabbitConfig.BROWN_TAIL_G, RabbitConfig.BROWN_TAIL_B, 255);
        } else if (RabbitConfig.TYPE_BLACK.equals(type)) {
            this.pickerBody.setRGBA(RabbitConfig.BLACK_BODY_R, RabbitConfig.BLACK_BODY_G, RabbitConfig.BLACK_BODY_B, 255);
            this.pickerEar.setRGBA(RabbitConfig.BLACK_EAR_R, RabbitConfig.BLACK_EAR_G, RabbitConfig.BLACK_EAR_B, 255);
            this.pickerEye.setRGBA(RabbitConfig.BLACK_EYE_R, RabbitConfig.BLACK_EYE_G, RabbitConfig.BLACK_EYE_B, 255);
            this.pickerTail.setRGBA(RabbitConfig.BLACK_TAIL_R, RabbitConfig.BLACK_TAIL_G, RabbitConfig.BLACK_TAIL_B, 255);
        } else if (RabbitConfig.TYPE_WHITE.equals(type)) {
            this.pickerBody.setRGBA(RabbitConfig.WHITE_BODY_R, RabbitConfig.WHITE_BODY_G, RabbitConfig.WHITE_BODY_B, 255);
            this.pickerEar.setRGBA(RabbitConfig.WHITE_EAR_R, RabbitConfig.WHITE_EAR_G, RabbitConfig.WHITE_EAR_B, 255);
            this.pickerEye.setRGBA(RabbitConfig.WHITE_EYE_R, RabbitConfig.WHITE_EYE_G, RabbitConfig.WHITE_EYE_B, 255);
            this.pickerTail.setRGBA(RabbitConfig.WHITE_TAIL_R, RabbitConfig.WHITE_TAIL_G, RabbitConfig.WHITE_TAIL_B, 255);
        } else if (RabbitConfig.TYPE_GOLDEN.equals(type)) {
            this.pickerBody.setRGBA(RabbitConfig.GOLDEN_BODY_R, RabbitConfig.GOLDEN_BODY_G, RabbitConfig.GOLDEN_BODY_B, 255);
            this.pickerEar.setRGBA(RabbitConfig.GOLDEN_EAR_R, RabbitConfig.GOLDEN_EAR_G, RabbitConfig.GOLDEN_EAR_B, 255);
            this.pickerEye.setRGBA(RabbitConfig.GOLDEN_EYE_R, RabbitConfig.GOLDEN_EYE_G, RabbitConfig.GOLDEN_EYE_B, 255);
            this.pickerTail.setRGBA(RabbitConfig.GOLDEN_TAIL_R, RabbitConfig.GOLDEN_TAIL_G, RabbitConfig.GOLDEN_TAIL_B, 255);
        }
    }

    private void applySettings() {
        double health = RabbitUtils.parseDoubleSafe(this.fieldHealth.getText(), RabbitConfig.DEFAULT_HEALTH);
        double speed = RabbitUtils.parseDoubleSafe(this.fieldSpeed.getText(), RabbitConfig.DEFAULT_SPEED);
        double jump = RabbitUtils.parseDoubleSafe(this.fieldJump.getText(), RabbitConfig.DEFAULT_JUMP);
        double fall = RabbitUtils.parseDoubleSafe(this.fieldFall.getText(), RabbitConfig.DEFAULT_FALL);

        float sOverall = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleOverall.getText(), 1.0D);
        float sHead = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleHead.getText(), 1.0D);
        float sEar = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleEar.getText(), 1.0D);
        float sBody = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleBody.getText(), 1.0D);
        float sLegs = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleLegs.getText(), 1.0D);
        float sTail = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleTail.getText(), 1.0D);

        PacketRabbitSettings pkt = new PacketRabbitSettings(
                this.selectedType, health, speed, jump, fall,
                sOverall, sHead, sEar, sBody, sLegs, sTail,
                this.pickerBody.getR(), this.pickerBody.getG(), this.pickerBody.getB(), this.pickerBody.getA(),
                this.pickerEar.getR(), this.pickerEar.getG(), this.pickerEar.getB(), this.pickerEar.getA(),
                this.pickerEye.getR(), this.pickerEye.getG(), this.pickerEye.getB(), this.pickerEye.getA(),
                this.pickerTail.getR(), this.pickerTail.getG(), this.pickerTail.getB(), this.pickerTail.getA()
        );
        PacketHandler.INSTANCE.sendToServer(pkt);

        saveJsonConfig();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.fieldHealth.textboxKeyTyped(typedChar, keyCode);
        this.fieldSpeed.textboxKeyTyped(typedChar, keyCode);
        this.fieldJump.textboxKeyTyped(typedChar, keyCode);
        this.fieldFall.textboxKeyTyped(typedChar, keyCode);

        this.fieldScaleOverall.textboxKeyTyped(typedChar, keyCode);
        this.fieldScaleHead.textboxKeyTyped(typedChar, keyCode);
        this.fieldScaleEar.textboxKeyTyped(typedChar, keyCode);
        this.fieldScaleBody.textboxKeyTyped(typedChar, keyCode);
        this.fieldScaleLegs.textboxKeyTyped(typedChar, keyCode);
        this.fieldScaleTail.textboxKeyTyped(typedChar, keyCode);

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

        this.fieldScaleOverall.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldScaleHead.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldScaleEar.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldScaleBody.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldScaleLegs.mouseClicked(mouseX, mouseY, mouseButton);
        this.fieldScaleTail.mouseClicked(mouseX, mouseY, mouseButton);

        this.pickerBody.mouseClicked(mouseX, mouseY, mouseButton);
        this.pickerEar.mouseClicked(mouseX, mouseY, mouseButton);
        this.pickerEye.mouseClicked(mouseX, mouseY, mouseButton);
        this.pickerTail.mouseClicked(mouseX, mouseY, mouseButton);

        int previewX = 260;
        int previewY = 24;
        int previewW = this.width - previewX - 10;
        int previewH = this.height - 48;

        if (mouseButton == 0 && mouseX >= previewX && mouseX <= previewX + previewW && mouseY >= previewY && mouseY <= previewY + previewH) {
            this.isDraggingPreview = true;
            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.isDraggingPreview = false;
        }
        this.pickerBody.mouseReleased(mouseX, mouseY, state);
        this.pickerEar.mouseReleased(mouseX, mouseY, state);
        this.pickerEye.mouseReleased(mouseX, mouseY, state);
        this.pickerTail.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (this.isDraggingPreview && clickedMouseButton == 0) {
            int deltaX = mouseX - this.prevMouseX;
            int deltaY = mouseY - this.prevMouseY;

            this.previewYaw += deltaX * 1.2F;
            this.previewPitch += deltaY * 0.8F;

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

        String title = I18n.format("gui.rabbitmorph.title");
        this.fontRendererObj.drawStringWithShadow(title, 10, 6, 0xFFFF55);

        int leftX = 10;
        int topY = 22;
        int statsY = topY + 18;

        this.fontRendererObj.drawString(I18n.format("gui.rabbitmorph.health"), leftX, statsY + 2, 0xCCCCCC);
        this.fieldHealth.drawTextBox();

        this.fontRendererObj.drawString(I18n.format("gui.rabbitmorph.speed"), leftX, statsY + 17, 0xCCCCCC);
        this.fieldSpeed.drawTextBox();

        this.fontRendererObj.drawString(I18n.format("gui.rabbitmorph.jump"), leftX + 85, statsY + 2, 0xCCCCCC);
        this.fieldJump.drawTextBox();

        this.fontRendererObj.drawString(I18n.format("gui.rabbitmorph.fall"), leftX + 85, statsY + 17, 0xCCCCCC);
        this.fieldFall.drawTextBox();

        int scalesY = statsY + 30;
        this.fontRendererObj.drawString("Scale All:", leftX, scalesY + 2, 0xAAAAAA);
        this.fieldScaleOverall.drawTextBox();

        this.fontRendererObj.drawString("Head:", leftX + 80, scalesY + 2, 0xAAAAAA);
        this.fieldScaleHead.drawTextBox();

        this.fontRendererObj.drawString("Ear:", leftX + 155, scalesY + 2, 0xAAAAAA);
        this.fieldScaleEar.drawTextBox();

        this.fontRendererObj.drawString("Body:", leftX, scalesY + 16, 0xAAAAAA);
        this.fieldScaleBody.drawTextBox();

        this.fontRendererObj.drawString("Legs:", leftX + 80, scalesY + 16, 0xAAAAAA);
        this.fieldScaleLegs.drawTextBox();

        this.fontRendererObj.drawString("Tail:", leftX + 155, scalesY + 16, 0xAAAAAA);
        this.fieldScaleTail.drawTextBox();

        int colorY = scalesY + 30;
        this.pickerBody.draw(this.fontRendererObj, mouseX, mouseY);
        this.pickerEar.draw(this.fontRendererObj, mouseX, mouseY);
        this.pickerEye.draw(this.fontRendererObj, mouseX, mouseY);
        this.pickerTail.draw(this.fontRendererObj, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Draw Live 360 Preview
        int previewX = 260;
        int previewY = 24;
        int previewW = this.width - previewX - 10;
        int previewH = this.height - 48;

        if (previewW > 60 && previewH > 60) {
            float sOverall = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleOverall.getText(), 1.0D);
            float sHead = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleHead.getText(), 1.0D);
            float sEar = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleEar.getText(), 1.0D);
            float sBody = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleBody.getText(), 1.0D);
            float sLegs = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleLegs.getText(), 1.0D);
            float sTail = (float) RabbitUtils.parseDoubleSafe(this.fieldScaleTail.getText(), 1.0D);

            RabbitPreviewRenderer.renderPreview(previewX, previewY, previewW, previewH,
                    this.previewYaw, this.previewPitch, this.previewZoom,
                    sOverall, sHead, sEar, sBody, sLegs, sTail,
                    this.pickerBody.getR(), this.pickerBody.getG(), this.pickerBody.getB(), this.pickerBody.getA(),
                    this.pickerEar.getR(), this.pickerEar.getG(), this.pickerEar.getB(), this.pickerEar.getA(),
                    this.pickerEye.getR(), this.pickerEye.getG(), this.pickerEye.getB(), this.pickerEye.getA(),
                    this.pickerTail.getR(), this.pickerTail.getG(), this.pickerTail.getB(), this.pickerTail.getA(),
                    this.ticksOpen, this.previewPose);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
