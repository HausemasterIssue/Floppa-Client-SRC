package me.brownzombie.floppa.gui.clickgui.part.parts.sub;

import me.brownzombie.floppa.gui.clickgui.part.PartTemplate;
import me.brownzombie.floppa.gui.clickgui.part.parts.Button;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

public class StringButton extends PartTemplate {

    private StringButton.CurrentString currentString = new StringButton.CurrentString("");
    private Setting setting;
    public boolean isListening;
    private boolean hovered;
    private Button parent;
    private int offset;
    private int x;
    private int y;

    public StringButton(Setting option, Button button, int offset) {
        this.setting = option;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }

    public static String removeLastChar(String str) {
        String output = "";

        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }

        return output;
    }

    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.hovered ? -14540254 : -15658735);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -15658735);
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        if (this.isListening) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.currentString.getString() + "|", (float) ((this.parent.parent.getX() + 7) * 2), (float) ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
        } else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((String) this.setting.getValue(), (float) ((this.parent.parent.getX() + 7) * 2), (float) ((this.parent.parent.getY() + this.offset + 2) * 2 + 5), -1);
        }

        GL11.glPopMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isMouseOnButton(mouseX, mouseY)) {
            this.isListening = !this.isListening;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 2.0F));
        }

    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            switch (keyCode) {
            case 1:
                return;

            case 28:
                this.enterString();
                this.isListening = !this.isListening;
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 1.5F));

            case 14:
                this.setString(removeLastChar(this.currentString.getString()));

            default:
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    this.setString(this.currentString.getString() + typedChar);
                }
            }
        }

    }

    public void updateComponent(int mouseX, int mouseY) {
        this.setShown(this.setting.getShown());
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setValue(this.setting.defaultValue);
        } else {
            this.setting.setValue(this.currentString.getString());
        }

        this.setString("");
    }

    public int getHeight() {
        return 14;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }

    public void setString(String newString) {
        this.currentString = new StringButton.CurrentString(newString);
    }

    public static class CurrentString {

        private final String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}
