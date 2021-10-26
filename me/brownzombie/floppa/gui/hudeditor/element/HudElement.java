package me.brownzombie.floppa.gui.hudeditor.element;

import me.brownzombie.floppa.gui.clickgui.ClickGui;
import me.brownzombie.floppa.gui.hudeditor.HudEditor;
import me.brownzombie.floppa.modules.client.ClickGuiModule;
import me.brownzombie.floppa.modules.hud.HudColorsModule;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import me.brownzombie.floppa.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import org.lwjgl.opengl.GL11;

public class HudElement extends HudElementTemplate {

    public HudModuleTemplate element;
    public HudEditor parent;
    public int offset;
    private boolean isHovered;

    public HudElement(HudModuleTemplate element, HudEditor parent, int offset) {
        this.element = element;
        this.parent = parent;
        this.offset = offset;
        int opY = offset + 12;
    }

    public void setOff(int newOff) {
        this.offset = newOff;
        int opY = this.offset + 12;
    }

    public void updateComponent(int mouseX, int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
    }

    public void renderComponent() {
        if (!ClickGuiModule.isOutline) {
            GuiUtil.drawRect(this.parent.getX() + 2, this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth() - 2, this.parent.getY() + 12 + this.offset, this.isHovered ? (this.element.isToggled() ? HudColorsModule.INSTANCE.getHudRainbow().darker().darker().getRGB() : -13553359) : (this.element.isToggled() ? HudColorsModule.INSTANCE.getHudRainbow().darker().getRGB() : -14277082), 7, 1.0F);
        } else {
            GuiUtil.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? (this.element.isToggled() ? ClickGui.instance.color.darker().getRGB() : -13553359) : (this.element.isToggled() ? ClickGui.instance.color.getRGB() : -14277082), 2, 1.0F);
        }

        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.element.getName(), (float) ((this.parent.getX() + 4) * 2), (float) ((this.parent.getY() + this.offset + 2) * 2 + 4), ClickGuiModule.isOutline ? ClickGui.getInstance().color.getRGB() : 0);
        GL11.glPopMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.element.toggle();
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F));
        }

    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
}
