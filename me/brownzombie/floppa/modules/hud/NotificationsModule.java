package me.brownzombie.floppa.modules.hud;

import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class NotificationsModule extends Module {

    public Setting notifModules = new Setting("Modules", Boolean.valueOf(true));
    public Setting notifModeSetting;
    public Setting notifRenderModeSetting;
    public static NotificationsModule INSTANCE;

    public NotificationsModule() {
        super("Notifications", "Notifications in HUD", Module.Category.HUD);
        this.notifModeSetting = new Setting("NotifMode", NotificationsModule.NotifMode.COMPACT);
        this.notifRenderModeSetting = new Setting("RenderMode", NotificationsModule.NotifRenderMode.ADVANCED);
        NotificationsModule.INSTANCE = this;
    }

    public static enum NotifRenderMode {

        SIMPLE, ADVANCED;
    }

    public static enum NotifMode {

        COMPACT, NONCOMPACT;
    }
}
