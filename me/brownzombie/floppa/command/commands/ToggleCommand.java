package me.brownzombie.floppa.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import joptsimple.internal.Strings;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.command.Command;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super(new String[] { "toggle", "t"}, "toggle <Module> | t <Module>");
    }

    public void exec(String args) throws Exception {
        if (Strings.isNullOrEmpty(args)) {
            MessageManager.sendClientMessage("Module expected", false);
        } else {
            Module m = Floppa.moduleManager.getModule(args);

            if (m == null) {
                MessageManager.sendClientMessage("Unknown module: " + args, false);
            } else {
                m.toggle();
                MessageManager.sendClientMessage(m.getName() + (m.isToggled() ? " is " + ChatFormatting.GREEN + "On" : "is " + ChatFormatting.RED + "Off"), false);
            }
        }
    }
}
