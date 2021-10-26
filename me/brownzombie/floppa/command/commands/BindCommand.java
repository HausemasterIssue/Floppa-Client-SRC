package me.brownzombie.floppa.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import joptsimple.internal.Strings;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.command.Command;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super(new String[] { "bind", "b"}, "bind | bind <Module> <Key>");
    }

    public void exec(String args) throws Exception {
        String[] split = args.split(" ");
        Floppa floppa = Floppa.instance;
        Module m = Floppa.moduleManager.getModule(split[0]);

        if (Strings.isNullOrEmpty(args)) {
            MessageManager.sendClientMessage(ChatFormatting.RED + "Module expected", false);
        } else if (Strings.isNullOrEmpty(split[1])) {
            MessageManager.sendClientMessage(ChatFormatting.RED + "Key expected", false);
        } else if (m == null) {
            MessageManager.sendClientMessage(ChatFormatting.RED + "Unknown module: " + split[0], false);
        } else if (split[1].length() <= 1 && split[1].length() >= 1) {
            m.setKey(Keyboard.getKeyIndex(split[1].toUpperCase()));
            MessageManager.sendClientMessage(ChatFormatting.RED + m.getName() + ChatFormatting.RESET + " is now bound to " + ChatFormatting.RED + split[1].toUpperCase(), true);
        } else {
            MessageManager.sendClientMessage(ChatFormatting.RED + "Unknown Key", false);
        }
    }
}
