package me.brownzombie.floppa.modules.misc;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import me.brownzombie.floppa.modules.Module;

public class DesktopNotifs extends Module {

    public DesktopNotifs() {
        super("DesktopNotifs", "Sends Desktop Notifcations", Module.Category.MISC);
    }

    public void onEnable() {
        if (DesktopNotifs.mc.player != null) {
            sendNotification("Hello", MessageType.NONE);
        }

    }

    public static void sendNotification(String message, MessageType messageType) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("images/background.png");
        TrayIcon icon = new TrayIcon(image, "Floppa");

        icon.setImageAutoSize(true);
        icon.setToolTip("Floppa");

        try {
            tray.add(icon);
        } catch (AWTException awtexception) {
            awtexception.printStackTrace();
        }

        icon.displayMessage("Floppa", message, messageType);
    }
}
