package me.brownzombie.floppa.gui.clickgui.part.parts.sub;

import java.text.DecimalFormat;
import me.brownzombie.floppa.gui.clickgui.part.PartTemplate;
import me.brownzombie.floppa.gui.clickgui.part.parts.Button;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import org.lwjgl.opengl.GL11;

public class FloatSlider extends PartTemplate {

    protected boolean hovered;
    private Setting setting;
    private Button parent;
    protected int offset;
    protected int x;
    protected int y;
    protected boolean dragging = false;
    protected double renderWidth;

    public FloatSlider(Setting value, Button button, int offset) {
        this.setting = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }

    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + (int) this.renderWidth, this.parent.parent.getY() + this.offset + 12, this.hovered ? -11184811 : -12303292);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -15658735);
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.setting.name + ": " + this.setting.getValue(), (float) (this.parent.parent.getX() * 2 + 15), (float) ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
        GL11.glPopMatrix();
    }

    public void setOff(int newOff) {
        this.offset = newOff;
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButtonDub(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 2.0F));
        }

        if (this.isMouseOnButtonInt(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 2.0F));
        }

    }

    public boolean isMouseOnButtonDub(int x, int y) {
        return x > this.x && x < this.x + this.parent.parent.getWidth() / 2 + 1 && y > this.y && y < this.y + 12;
    }

    public boolean isMouseOnButtonInt(int x, int y) {
        return x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 12;
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (this.dragging) {
            this.dragging = false;
        }

    }

    public void updateComponent(int mouseX, int mouseY) {
        this.setShown(this.setting.getShown());
        this.hovered = this.isMouseOnButtonDub(mouseX, mouseY) || this.isMouseOnButtonInt(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        double diff = (double) Math.min(88, Math.max(0, mouseX - this.x));
        double min = (double) ((Float) this.setting.min).floatValue();
        double max = (double) ((Float) this.setting.max).floatValue();

        this.renderWidth = 88.0D * ((double) ((Float) this.setting.getValue()).floatValue() - min) / (max - min);
        if (this.dragging) {
            if (diff == 0.0D) {
                this.setting.setValue(this.setting.min);
            } else {
                DecimalFormat format = new DecimalFormat("##.0");
                String newValueFloat = format.format(diff / 88.0D * (max - min) + min);

                this.setting.setValue(Float.valueOf(Float.parseFloat(newValueFloat)));
            }
        }

    }
}
