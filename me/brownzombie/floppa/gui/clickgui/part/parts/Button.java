package me.brownzombie.floppa.gui.clickgui.part.parts;

import java.util.ArrayList;
import java.util.Iterator;
import me.brownzombie.floppa.gui.clickgui.ClickGui;
import me.brownzombie.floppa.gui.clickgui.part.Frame;
import me.brownzombie.floppa.gui.clickgui.part.PartTemplate;
import me.brownzombie.floppa.gui.clickgui.part.parts.sub.Checkbox;
import me.brownzombie.floppa.gui.clickgui.part.parts.sub.DoubleSlider;
import me.brownzombie.floppa.gui.clickgui.part.parts.sub.FloatSlider;
import me.brownzombie.floppa.gui.clickgui.part.parts.sub.IntSlider;
import me.brownzombie.floppa.gui.clickgui.part.parts.sub.Keybind;
import me.brownzombie.floppa.gui.clickgui.part.parts.sub.ModeButton;
import me.brownzombie.floppa.gui.clickgui.part.parts.sub.StringButton;
import me.brownzombie.floppa.gui.clickgui.part.parts.sub.VisibleButton;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.modules.client.ClickGuiModule;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import org.lwjgl.opengl.GL11;

public class Button extends PartTemplate {

    public Module mod;
    public Frame parent;
    public int offset;
    private boolean isHovered;
    private ArrayList subcomponents;
    public boolean open;
    private int height;

    public Button(Module mod, Frame parent, int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList();
        this.open = false;
        this.height = 12;
        int opY = offset + 12;
        Iterator iterator = mod.settings.iterator();

        while (iterator.hasNext()) {
            Setting s = (Setting) iterator.next();

            if (s.getValue() instanceof Enum) {
                this.subcomponents.add(new ModeButton(s, this, mod, opY));
                opY += 12;
            }

            if (s.getValue() instanceof Integer) {
                this.subcomponents.add(new IntSlider(s, this, opY));
                opY += 12;
            }

            if (s.getValue() instanceof Float) {
                this.subcomponents.add(new FloatSlider(s, this, opY));
                opY += 12;
            }

            if (s.getValue() instanceof Double) {
                this.subcomponents.add(new DoubleSlider(s, this, opY));
                opY += 12;
            }

            if (s.getValue() instanceof Boolean) {
                this.subcomponents.add(new Checkbox(s, this, opY));
                opY += 12;
            }

            if (s.getValue() instanceof String) {
                this.subcomponents.add(new StringButton(s, this, opY));
                opY += 12;
            }
        }

        this.subcomponents.add(new Keybind(this, opY));
        this.subcomponents.add(new VisibleButton(this, mod, opY));
    }

    public void setOff(int newOff) {
        this.offset = newOff;
        int opY = this.offset + 12;
        Iterator iterator = this.subcomponents.iterator();

        while (iterator.hasNext()) {
            PartTemplate comp = (PartTemplate) iterator.next();

            comp.setOff(opY);
            if (comp.isShown()) {
                opY += 12;
            }
        }

    }

    public void renderComponent() {
        if (!ClickGuiModule.isOutline) {
            GuiUtil.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? (this.mod.isToggled() ? ClickGui.instance.color.darker().getRGB() : 1966158129) : (this.mod.isToggled() ? ClickGui.instance.color.getRGB() : 1965434406), 7, 1.0F);
        } else {
            GuiUtil.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? (this.mod.isToggled() ? ClickGui.instance.color.darker().getRGB() : 1966158129) : (this.mod.isToggled() ? ClickGui.instance.color.getRGB() : 1965434406), 2, 1.0F);
        }

        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.mod.getName(), (float) ((this.parent.getX() + 2) * 2), (float) ((this.parent.getY() + this.offset + 2) * 2 + 4), ClickGuiModule.isOutline ? ClickGui.getInstance().color.getRGB() : 0);
        if (this.subcomponents.size() > 2) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.open ? "-" : "+", (float) ((this.parent.getX() + this.parent.getWidth() - 10) * 2), (float) ((this.parent.getY() + this.offset + 2) * 2 + 4), -1);
        }

        GL11.glPopMatrix();
        if (this.open) {
            if (!this.subcomponents.isEmpty()) {
                Iterator iterator = this.subcomponents.iterator();

                while (iterator.hasNext()) {
                    PartTemplate comp = (PartTemplate) iterator.next();

                    if (comp.isShown()) {
                        comp.renderComponent();
                    }
                }
            }

            Gui.drawRect(this.parent.getX() + 1, this.parent.getY() + this.offset + 12, this.parent.getX() + 2, this.parent.getY() + this.offset + this.getHeight(), ClickGui.instance.color.getRGB());
        }

    }

    public int getHeight() {
        return this.open ? 12 * (this.subcomponents.size() + 1) : 12;
    }

    public void updateComponent(int mouseX, int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            Iterator iterator = this.subcomponents.iterator();

            while (iterator.hasNext()) {
                PartTemplate comp = (PartTemplate) iterator.next();

                comp.updateComponent(mouseX, mouseY);
            }
        }

    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F));
        }

        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 0.5F));
        }

        Iterator iterator = this.subcomponents.iterator();

        while (iterator.hasNext()) {
            PartTemplate comp = (PartTemplate) iterator.next();

            comp.mouseClicked(mouseX, mouseY, button);
        }

    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        Iterator iterator = this.subcomponents.iterator();

        while (iterator.hasNext()) {
            PartTemplate comp = (PartTemplate) iterator.next();

            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }

        this.parent.refresh();
    }

    public void keyTyped(char typedChar, int key) {
        Iterator iterator = this.subcomponents.iterator();

        while (iterator.hasNext()) {
            PartTemplate comp = (PartTemplate) iterator.next();

            comp.keyTyped(typedChar, key);
        }

    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
}
