package me.brownzombie.floppa.gui.windows95;

import java.awt.Color;
import me.brownzombie.floppa.util.GuiUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Windows95 extends GuiScreen {

    private ResourceLocation xImage = new ResourceLocation("minecraft:images/x.png");
    private ResourceLocation slabImage = new ResourceLocation("minecraft:images/slab.png");
    private ResourceLocation boxImage = new ResourceLocation("minecraft:images/square.png");

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GuiUtil.drawRect(100, 100, 175, 115, (new Color(185, 185, 185, 255)).getRGB(), 7, 1.0F);
        GuiUtil.drawRect(102, 102, 173, 113, (new Color(20, 20, 200, 255)).getRGB(), 7, 1.0F);
        GuiUtil.drawRect(164, 104, 171, 111, (new Color(185, 185, 185, 255)).getRGB(), 7, 1.0F);
        GuiUtil.drawRect(155, 104, 162, 111, (new Color(185, 185, 185, 255)).getRGB(), 7, 1.0F);
        GuiUtil.drawRect(146, 104, 153, 111, (new Color(185, 185, 185, 255)).getRGB(), 7, 1.0F);
        this.mc.renderEngine.bindTexture(this.xImage);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        GlStateManager.enableTexture2D();
        Gui.drawScaledCustomSizeModalRect(164, 104, 7.0F, 7.0F, 243, 43, 7, 7, 250.0F, 50.0F);
        this.mc.renderEngine.bindTexture(this.boxImage);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        Gui.drawScaledCustomSizeModalRect(155, 104, 7.0F, 7.0F, 243, 43, 7, 7, 250.0F, 50.0F);
        this.mc.renderEngine.bindTexture(this.slabImage);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        Gui.drawScaledCustomSizeModalRect(146, 104, 7.0F, 7.0F, 243, 43, 7, 7, 250.0F, 50.0F);
        GlStateManager.scale(0.75D, 0.75D, 0.75D);
        this.fontRenderer.drawStringWithShadow("Combat", 140.0F, 140.0F, (new Color(185, 185, 185, 255)).getRGB());
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
