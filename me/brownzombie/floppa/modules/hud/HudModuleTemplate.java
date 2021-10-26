package me.brownzombie.floppa.modules.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.client.Chat;
import me.brownzombie.floppa.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.common.MinecraftForge;

public class HudModuleTemplate {

    protected static Minecraft mc = Minecraft.getMinecraft();
    protected ColorUtil colorUtil = new ColorUtil();
    public boolean visible = true;
    private boolean dragging;
    private boolean toggled;
    public String stringVal;
    protected int height;
    private String name;
    protected int width;
    private int dragX;
    private int dragY;
    protected int x;
    protected int y;
    public FontRenderer fr;

    public HudModuleTemplate(String name) {
        this.fr = Minecraft.getMinecraft().fontRenderer;
        this.stringVal = "fl0ppa";
        this.dragging = false;
        this.toggled = false;
        this.height = this.fr.FONT_HEIGHT;
        this.width = 10;
        this.name = name;
        this.dragX = 0;
        this.dragY = 0;
        this.x = 5;
        this.y = 5;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, (new Color(75, 75, 75, 150)).getRGB());
        if (this.dragging) {
            this.x = this.dragX + mouseX;
            this.y = this.dragY + mouseY;
        }

        this.width = HudModuleTemplate.mc.fontRenderer.getStringWidth(this.stringVal);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height) {
            this.dragX = this.x - mouseX;
            this.dragY = this.y - mouseY;
            this.dragging = true;
        }

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.dragging = false;
        }

    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            this.onEnable();
            if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue() && this.visible) {
                MessageManager.sendClientMessage(this.getName() + " is " + ChatFormatting.GREEN + "On", false);
            }
        } else {
            this.onDisable();
            if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue() && this.visible) {
                MessageManager.sendClientMessage(this.getName() + " is " + ChatFormatting.RED + "Off", false);
            }
        }

    }

    public void toggle() {
        this.toggled = !this.toggled;
        if (this.toggled) {
            this.onEnable();
            if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue() && this.visible) {
                MessageManager.sendClientMessage(this.getName() + " is " + ChatFormatting.GREEN + "On", false);
            }
        } else {
            this.onDisable();
            if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue() && this.visible) {
                MessageManager.sendClientMessage(this.getName() + " is " + ChatFormatting.RED + "Off", false);
            }
        }

    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void onDisconnect() {}

    public void onRenderOverlay(float partialTicks) {}

    public void onRender(float partialTicks) {}

    public void onUpdate() {}

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }
}
