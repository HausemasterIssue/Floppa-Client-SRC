package me.brownzombie.floppa.gui.clickgui.part;

import java.util.ArrayList;
import java.util.Iterator;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.gui.clickgui.ClickGui;
import me.brownzombie.floppa.gui.clickgui.part.parts.Button;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.modules.client.ClickGuiModule;
import me.brownzombie.floppa.util.ColorUtil;
import me.brownzombie.floppa.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Frame {

    public ArrayList parts = new ArrayList();
    public Module.Category category;
    public ColorUtil colorUtil = new ColorUtil();
    public ClickGuiModule cgModule = new ClickGuiModule();
    private ResourceLocation categoryIcon;
    private boolean isDragging;
    private int barHeight;
    private boolean open;
    private int width;
    public int dragX;
    public int dragY;
    private int y;
    private int x;

    public Frame(Module.Category cat) {
        this.category = cat;
        this.width = 88;
        this.x = 5;
        this.y = 5;
        this.barHeight = 13;
        this.dragX = 0;
        this.open = true;
        this.isDragging = false;
        int tY = this.barHeight;
        Floppa floppa = Floppa.instance;

        for (Iterator iterator = Floppa.moduleManager.getModulesInCategory(this.category).iterator(); iterator.hasNext(); tY += 12) {
            Module mod = (Module) iterator.next();
            Button modButton = new Button(mod, this, tY);

            this.parts.add(modButton);
        }

    }

    public ArrayList getComponents() {
        return this.parts;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void renderFrame(FontRenderer fontRenderer) {
        if (!ClickGuiModule.isOutline) {
            GuiUtil.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, ClickGui.getInstance().color.getRGB(), ClickGui.getInstance().color.darker().getRGB(), 7, 1.0F);
        } else {
            GlStateManager.glLineWidth(1.0F);
            GuiUtil.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, ClickGui.getInstance().color.getRGB(), ClickGui.getInstance().color.darker().getRGB(), 2, 1.0F);
        }

        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        fontRenderer.drawStringWithShadow(this.category.name(), (float) ((this.x + 2) * 2 + 5), ((float) this.y + 2.5F) * 2.0F + 5.0F, ClickGuiModule.isOutline ? ClickGui.getInstance().color.getRGB() : 0);
        GL11.glPopMatrix();
        this.categoryIcon = new ResourceLocation("minecraft:images/" + this.category.name().toLowerCase() + ".png");
        Minecraft.getMinecraft().renderEngine.bindTexture(this.categoryIcon);
        if (ClickGuiModule.isOutline) {
            GlStateManager.color((float) ClickGui.getInstance().color.getRed() / 255.0F, (float) ClickGui.getInstance().color.getGreen() / 255.0F, (float) ClickGui.getInstance().color.getBlue() / 255.0F, 255.0F);
        } else {
            GlStateManager.color(0.0F, 0.0F, 0.0F, 255.0F);
        }

        GlStateManager.enableTexture2D();
        Gui.drawScaledCustomSizeModalRect(this.x + 76, this.y + 3, 7.0F, 7.0F, 243, 43, 7, 7, 250.0F, 50.0F);
        if (this.open && !this.parts.isEmpty()) {
            Iterator iterator = this.parts.iterator();

            while (iterator.hasNext()) {
                PartTemplate part = (PartTemplate) iterator.next();

                part.renderComponent();
            }
        }

    }

    public void refresh() {
        int off = this.barHeight;

        PartTemplate comp;

        for (Iterator iterator = this.parts.iterator(); iterator.hasNext(); off += comp.getHeight()) {
            comp = (PartTemplate) iterator.next();
            comp.setOff(off);
        }

    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }

    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
}
