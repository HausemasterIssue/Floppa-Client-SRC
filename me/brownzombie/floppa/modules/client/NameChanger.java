package me.brownzombie.floppa.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class NameChanger extends Module {

    public Setting newUsername = new Setting("Name", "New Name");
    private static NameChanger instance;

    public NameChanger() {
        super("NameChanger", "Changes your name.", Module.Category.CLIENT);
        NameChanger.instance = this;
    }

    public void onEnable() {
        MessageManager.sendClientMessage(ChatFormatting.GRAY + "Name changed to " + ChatFormatting.GREEN + (String) this.newUsername.getValue(), true);
    }

    public static NameChanger getInstance() {
        if (NameChanger.instance == null) {
            NameChanger.instance = new NameChanger();
        }

        return NameChanger.instance;
    }

    public static String getOldName() {
        return NameChanger.mc.player != null && NameChanger.mc.world != null ? NameChanger.mc.player.getDisplayNameString() : "abcdefghijklmnopqrstuvwxyz";
    }
}
