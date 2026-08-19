package com.jiwan.rabbitmorph.gui;

import com.jiwan.rabbitmorph.client.RabbitPreviewRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiPreviewPanel {

    private final int x, y, width, height;

    private float yaw = 0.0F;
    private float pitch = 0.0F;
    private float zoom = 1.0F;
    private int pose = 0; // 0=Idle, 1=Walk, 2=Jump, 3=Sneak
    private int bgIndex = 0;

    private boolean isDragging = false;
    private int prevMouseX, prevMouseY;

    private GuiButton btnPose;
    private GuiButton btnBg;
    private GuiButton btnReset;

    public GuiPreviewPanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void initGui(List<GuiButton> buttonList, int startBtnId) {
        int ctrlY = y - 14;
        this.btnPose = new GuiButton(startBtnId, x, ctrlY, 60, 12, "Pose: Idle");
        this.btnBg = new GuiButton(startBtnId + 1, x + 63, ctrlY, 70, 12, RabbitPreviewRenderer.BG_NAMES[0]);
        this.btnReset = new GuiButton(startBtnId + 2, x + 136, ctrlY, 50, 12, "Reset");

        buttonList.add(btnPose);
        buttonList.add(btnBg);
        buttonList.add(btnReset);
    }

    public boolean actionPerformed(GuiButton button) {
        if (button == btnPose) {
            this.pose = (this.pose + 1) % 4;
            String name = "Pose: Idle";
            if (this.pose == 1) name = "Pose: Walk";
            else if (this.pose == 2) name = "Pose: Jump";
            else if (this.pose == 3) name = "Pose: Sneak";
            this.btnPose.displayString = name;
            return true;
        } else if (button == btnBg) {
            this.bgIndex = (this.bgIndex + 1) % RabbitPreviewRenderer.BG_COLORS.length;
            this.btnBg.displayString = RabbitPreviewRenderer.BG_NAMES[this.bgIndex];
            return true;
        } else if (button == btnReset) {
            this.yaw = 0.0F;
            this.pitch = 0.0F;
            this.zoom = 1.0F;
            return true;
        }
        return false;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            this.isDragging = true;
            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.isDragging = false;
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
        if (this.isDragging && clickedMouseButton == 0) {
            int deltaX = mouseX - this.prevMouseX;
            int deltaY = mouseY - this.prevMouseY;

            this.yaw += deltaX * 1.2F;
            this.pitch += deltaY * 0.8F;

            if (this.pitch < -75.0F) this.pitch = -75.0F;
            if (this.pitch > 75.0F) this.pitch = 75.0F;

            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }
    }

    public void handleMouseInput() {
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            if (dWheel > 0) this.zoom += 0.1F;
            else this.zoom -= 0.1F;
            if (this.zoom < 0.4F) this.zoom = 0.4F;
            if (this.zoom > 2.5F) this.zoom = 2.5F;
        }
    }

    public void draw(FontRenderer font, float ticksOpen,
                     float sOverall, float sHead, float sEar, float sBody, float sLegs, float sTail,
                     int bR, int bG, int bB, int bA,
                     int eR, int eG, int eB, int eA,
                     int eyR, int eyG, int eyB, int eyA,
                     int tR, int tG, int tB, int tA) {

        if (width > 40 && height > 40) {
            RabbitPreviewRenderer.renderPreview(x, y, width, height,
                    this.yaw, this.pitch, this.zoom,
                    sOverall, sHead, sEar, sBody, sLegs, sTail,
                    bR, bG, bB, bA, eR, eG, eB, eA, eyR, eyG, eyB, eyA, tR, tG, tB, tA,
                    ticksOpen, this.pose, this.bgIndex);
        }
    }

    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public float getZoom() { return zoom; }
}
