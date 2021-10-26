package me.brownzombie.floppa.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.List;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.managers.NotificationManager;
import me.brownzombie.floppa.modules.client.Chat;
import me.brownzombie.floppa.modules.hud.NotificationsModule;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module {

    protected static Minecraft mc = Minecraft.getMinecraft();
    private String name;
    private String description;
    public List settings = new ArrayList();
    private int key;
    private Module.Category category;
    private boolean toggled;
    public boolean visible = true;

    public Module(String name, String description, Module.Category category) {
        this.name = name;
        this.description = description;
        this.key = 0;
        this.category = category;
        this.toggled = false;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        if (key != 211 && key != 14) {
            this.key = key;
        } else {
            this.key = 0;
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
        String msg;

        if (this.toggled) {
            this.onEnable();
            if (this.visible) {
                if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue()) {
                    MessageManager.sendClientMessage(this.getName() + " is " + ChatFormatting.GREEN + "On", false);
                }

                if (NotificationsModule.INSTANCE.isToggled() && ((Boolean) NotificationsModule.INSTANCE.notifModules.getValue()).booleanValue()) {
                    msg = this.getName() + " is " + ChatFormatting.GREEN + "On";
                    NotificationManager.Get().AddNotification("ToggleNotif", msg, Boolean.valueOf(true));
                }
            }
        } else {
            this.onDisable();
            if (this.visible) {
                if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue()) {
                    MessageManager.sendClientMessage(this.getName() + " is " + ChatFormatting.RED + "Off", false);
                }

                if (NotificationsModule.INSTANCE.isToggled() && ((Boolean) NotificationsModule.INSTANCE.notifModules.getValue()).booleanValue()) {
                    msg = this.getName() + " is " + ChatFormatting.RED + "Off";
                    NotificationManager.Get().AddNotification("ToggleNotif", msg, Boolean.valueOf(true));
                }
            }
        }

    }

    public void toggle() {
        this.toggled = !this.toggled;
        String msg;

        if (this.toggled) {
            this.onEnable();
            if (Module.mc.player != null && Module.mc.world != null && this.visible) {
                if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue()) {
                    MessageManager.sendClientMessage(this.getName() + " is " + ChatFormatting.GREEN + "On", false);
                }

                if (NotificationsModule.INSTANCE.isToggled() && ((Boolean) NotificationsModule.INSTANCE.notifModules.getValue()).booleanValue()) {
                    msg = this.getName() + " is " + ChatFormatting.GREEN + "On";
                    NotificationManager.Get().AddNotification("ToggleNotif", msg, Boolean.valueOf(true));
                }
            }
        } else {
            this.onDisable();
            if (Module.mc.player != null && Module.mc.world != null && this.visible) {
                if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue()) {
                    MessageManager.sendClientMessage(this.getName() + " is " + ChatFormatting.RED + "Off", false);
                }

                if (NotificationsModule.INSTANCE.isToggled() && ((Boolean) NotificationsModule.INSTANCE.notifModules.getValue()).booleanValue()) {
                    msg = this.getName() + " is " + ChatFormatting.RED + "Off";
                    NotificationManager.Get().AddNotification("ToggleNotif", msg, Boolean.valueOf(true));
                }
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

    public void onRenderOverlay() {}

    public void onRender(float partialTicks) {}

    public void onUpdate() {}

    public String getName() {
        return this.name;
    }

    public Module.Category getCategory() {
        return this.category;
    }

    public static enum Category {

        COMBAT, RENDER, PLAYER, MOVEMENT, HUD, MISC, OFFHAND, CLIENT;
    }
}
