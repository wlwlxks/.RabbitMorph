package com.jiwan.rabbitmorph.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiTabEffects {

    private final FontRenderer font;
    private final int startX, startY;

    public boolean isGlowing = false;
    public int particleType = 0; // 0=Off, 1=Hearts, 2=Stars, 3=Dust
    public int firstPersonMode = 0; // 0=Rabbit Paws, 1=Default Player Hands

    private GuiButton btnGlow;
    private GuiButton btnParticles;
    private GuiButton btnFirstPerson;

    public GuiTabEffects(FontRenderer font, int x, int y) {
        this.font = font;
        this.startX = x;
        this.startY = y;
    }

    public void initGui(List<GuiButton> buttonList, int startBtnId) {
        this.btnGlow = new GuiButton(startBtnId, startX, startY, 110, 14, getGlowText());
        this.btnParticles = new GuiButton(startBtnId + 1, startX, startY + 18, 140, 14, getParticleText());
        this.btnFirstPerson = new GuiButton(startBtnId + 2, startX, startY + 36, 170, 14, getFirstPersonText());

        buttonList.add(btnGlow);
        buttonList.add(btnParticles);
        buttonList.add(btnFirstPerson);

        setVisible(false);
    }

    public void setVisible(boolean visible) {
        if (btnGlow != null) btnGlow.visible = visible;
        if (btnParticles != null) btnParticles.visible = visible;
        if (btnFirstPerson != null) btnFirstPerson.visible = visible;
    }

    public boolean actionPerformed(GuiButton button) {
        if (!button.visible) return false;

        if (button == btnGlow) {
            this.isGlowing = !this.isGlowing;
            this.btnGlow.displayString = getGlowText();
            return true;
        } else if (button == btnParticles) {
            this.particleType = (this.particleType + 1) % 4;
            this.btnParticles.displayString = getParticleText();
            return true;
        } else if (button == btnFirstPerson) {
            this.firstPersonMode = (this.firstPersonMode + 1) % 2;
            this.btnFirstPerson.displayString = getFirstPersonText();
            return true;
        }
        return false;
    }

    public void draw(int mouseX, int mouseY) {
        font.drawString("Visual & Audio Options", startX, startY - 10, 0xFFFF55);
    }

    private String getGlowText() {
        return "Glowing Effect: " + (isGlowing ? "ON" : "OFF");
    }

    private String getParticleText() {
        String p = "Off";
        if (particleType == 1) p = "Hearts";
        else if (particleType == 2) p = "Stars";
        else if (particleType == 3) p = "Speed Dust";
        return "Particle Trail: " + p;
    }

    private String getFirstPersonText() {
        return "1st Person View: " + (firstPersonMode == 0 ? "Rabbit Paws" : "Human Hands");
    }
}
