package me.brownzombie.floppa.gui.hudeditor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.gui.clickgui.ClickGui;
import me.brownzombie.floppa.gui.hudeditor.element.HudElement;
import me.brownzombie.floppa.gui.hudeditor.element.HudElementTemplate;
import me.brownzombie.floppa.modules.client.ClickGuiModule;
import me.brownzombie.floppa.modules.hud.HudColorsModule;
import me.brownzombie.floppa.modules.hud.HudEditorModule;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import me.brownzombie.floppa.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HudEditor extends GuiScreen {

    public ArrayList elements = new ArrayList();
    private ResourceLocation shader;
    private boolean dragging;
    private boolean open;
    private int height = 13;
    private int width = 88;
    public int dragX;
    public int dragY;
    private int x;
    private int y;

    public HudEditor() {
        int tY = this.height;

        this.dragging = false;
        this.open = true;
        this.dragX = 0;
        this.dragY = 0;
        this.x = 5;
        this.y = 5;
        Floppa floppa = Floppa.instance;

        for (Iterator iterator = Floppa.moduleManager.getElements().iterator(); iterator.hasNext(); tY += 12) {
            HudModuleTemplate element = (HudModuleTemplate) iterator.next();
            HudElement elementButton = new HudElement(element, this, tY);

            this.elements.add(elementButton);
        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!ClickGuiModule.isOutline) {
            GuiUtil.drawRoundedRect((float) this.x, (float) this.y, (float) (this.x + this.width), (float) (this.y + this.height), 3.0F, HudColorsModule.INSTANCE.getHudRainbow().getRGB());
        } else {
            GlStateManager.glLineWidth(1.0F);
            GuiUtil.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.height, ClickGui.getInstance().color.getRGB(), ClickGui.getInstance().color.darker().getRGB(), 2, 1.0F);
        }

        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.fontRenderer.drawStringWithShadow("HUD Editor", (float) ((this.x + 2) * 2 + 5), ((float) this.y + 2.5F) * 2.0F + 5.0F, ClickGuiModule.isOutline ? ClickGui.getInstance().color.getRGB() : 0);
        this.fontRenderer.drawStringWithShadow(this.open ? "-" : "+", (float) ((this.x + this.width - 10) * 2 + 5), ((float) this.y + 2.5F) * 2.0F + 5.0F, -1);
        GL11.glPopMatrix();
        Iterator iterator;
        HudElementTemplate element;

        if (this.open && !this.elements.isEmpty()) {
            iterator = this.elements.iterator();

            while (iterator.hasNext()) {
                element = (HudElementTemplate) iterator.next();
                element.renderComponent();
                element.updateComponent(mouseX, mouseY);
            }
        }

        iterator = ((List) Floppa.moduleManager.getElements().stream().filter(HudModuleTemplate::isToggled).collect(Collectors.toList())).iterator();

        while (iterator.hasNext()) {
            HudModuleTemplate element1 = (HudModuleTemplate) iterator.next();

            element1.drawScreen(mouseX, mouseY, partialTicks);
        }

        iterator = this.elements.iterator();

        while (iterator.hasNext()) {
            element = (HudElementTemplate) iterator.next();
            element.updateComponent(mouseX, mouseY);
        }

        this.shader = new ResourceLocation("minecraft", "shaders/post/blur.json");
        if (!this.mc.entityRenderer.isShaderActive() && ClickGuiModule.blur) {
            this.mc.entityRenderer.loadShader(this.shader);
        }

        if (!ClickGuiModule.blur) {
            this.mc.entityRenderer.stopUseShader();
        }

        this.updatePosition(mouseX, mouseY);
    }

    public void refresh() {
        int off = this.height;

        HudElementTemplate element;

        for (Iterator iterator = this.elements.iterator(); iterator.hasNext(); off += element.getHeight()) {
            element = (HudElementTemplate) iterator.next();
            element.setOff(off);
        }

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.isMouseOnButton(mouseX, mouseY) && mouseButton == 0) {
            this.dragging = true;
            this.dragX = mouseX - this.x;
            this.dragY = mouseY - this.y;
        }

        if (this.isMouseOnButton(mouseX, mouseY) && mouseButton == 1) {
            this.open = !this.open;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 2.0F));
        }

        Iterator iterator = ((List) Floppa.moduleManager.getElements().stream().filter(HudModuleTemplate::isToggled).collect(Collectors.toList())).iterator();

        while (iterator.hasNext()) {
            HudModuleTemplate element = (HudModuleTemplate) iterator.next();

            element.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (this.open) {
            iterator = this.elements.iterator();

            while (iterator.hasNext()) {
                HudElementTemplate element1 = (HudElementTemplate) iterator.next();

                element1.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        Iterator iterator = ((List) Floppa.moduleManager.getElements().stream().filter(HudModuleTemplate::isToggled).collect(Collectors.toList())).iterator();

        while (iterator.hasNext()) {
            HudModuleTemplate element = (HudModuleTemplate) iterator.next();

            element.mouseReleased(mouseX, mouseY, state);
        }

        if (this.open) {
            iterator = this.elements.iterator();

            while (iterator.hasNext()) {
                HudElementTemplate element1 = (HudElementTemplate) iterator.next();

                element1.mouseReleased(mouseX, mouseY, state);
            }
        }

        this.dragging = false;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.dragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }

    }

    public void onGuiClosed() {
        HudEditorModule.INSTANCE.toggle();
        this.mc.entityRenderer.stopUseShader();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
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

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
