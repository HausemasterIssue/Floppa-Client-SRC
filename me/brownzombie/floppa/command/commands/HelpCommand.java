package me.brownzombie.floppa.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Arrays;
import java.util.Iterator;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.command.Command;
import me.brownzombie.floppa.managers.CommandManager;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.client.Chat;

public class HelpCommand extends Command {

    public HelpCommand() {
        super(new String[] { "help", "h"}, "help | help <Command>");
    }

    public void exec(String args) throws Exception {
        if (!args.isEmpty()) {
            if (CommandManager.INSTANCE.getCommand(args) == null) {
                MessageManager.sendClientMessage("Could not find command " + args, true);
            }
        } else {
            String commands = "";

            Command c;

            for (Iterator iterator = Floppa.commandManager.getCommands().iterator(); iterator.hasNext(); commands = commands.concat(Arrays.toString(c.getName()) + " ")) {
                c = (Command) iterator.next();
            }

            MessageManager.sendClientMessage("Client Made By " + ChatFormatting.DARK_GREEN + "BrownZombie " + ChatFormatting.RESET + "and" + ChatFormatting.LIGHT_PURPLE + " divisiion", true);
            MessageManager.sendClientMessage("Commands: " + commands, true);
            MessageManager.sendClientMessage("Prefix: " + (String) Chat.INSTANCE.chatPrefix.getValue(), true);
        }

    }
}
