package me.brownzombie.floppa.gui.clickgui.part.parts.sub;

import java.util.Arrays;
import java.util.List;
import me.brownzombie.floppa.gui.clickgui.part.PartTemplate;
import me.brownzombie.floppa.gui.clickgui.part.parts.Button;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import org.lwjgl.opengl.GL11;

public class ModeButton extends PartTemplate {

    private boolean hovered;
    private Button parent;
    private Setting setting;
    private int offset;
    private int x;
    private int y;
    private Module mod;
    private int modeIndex;

    public ModeButton(Setting set, Button button, Module mod, int offset) {
        this.setting = set;
        this.parent = button;
        this.mod = mod;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
        this.modeIndex = 0;
    }

    public void setOff(int newOff) {
        this.offset = newOff;
    }

    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -15658735);
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.setting.name + ": " + this.setting.getValue(), (float) ((this.parent.parent.getX() + 7) * 2), (float) ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
        GL11.glPopMatrix();
    }

    public void updateComponent(int mouseX, int mouseY) {
        this.setShown(this.setting.getShown());
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            List list = Arrays.asList(this.getEnum().getEnumConstants());
            int index = list.indexOf(this.setting.getValue());

            if (index + 1 < list.size()) {
                this.setting.setValue(list.get(index + 1));
            } else {
                this.setting.setValue(list.get(0));
            }

            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 2.0F));
        }

    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }

    private Class getEnum() {
        return ((Enum) this.setting.getValue()).getDeclaringClass();
    }
}
