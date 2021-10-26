package me.brownzombie.floppa.managers;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.brownzombie.floppa.command.Command;
import me.brownzombie.floppa.command.commands.BindCommand;
import me.brownzombie.floppa.command.commands.HelpCommand;
import me.brownzombie.floppa.command.commands.ToggleCommand;
import me.brownzombie.floppa.modules.client.Chat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandManager {

    private final List commands;
    public static CommandManager INSTANCE;

    public CommandManager() {
        CommandManager.INSTANCE = this;
        this.commands = Lists.newArrayList(new Command[] { new ToggleCommand(), new HelpCommand(), new BindCommand()});
        MinecraftForge.EVENT_BUS.register(this);
    }

    public List getCommands() {
        return this.commands;
    }

    public Command getCommand(String cmdName) {
        Iterator iterator = this.commands.iterator();

        Command command;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            command = (Command) iterator.next();
        } while (!Arrays.asList(command.getName()).contains(cmdName));

        return command;
    }

    private void callCommand(String text) {
        if (!text.contains(" ")) {
            text = text + " ";
        }

        String[] split = text.split(" ");
        Iterator iterator = this.commands.iterator();

        while (iterator.hasNext()) {
            Command c = (Command) iterator.next();
            String[] astring = c.getName();
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring[j];

                if (s.equalsIgnoreCase(split[0])) {
                    try {
                        c.exec(text.substring(split[0].length() + 1));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        MessageManager.sendClientMessage("ERR: " + exception.toString(), false);
                    }

                    return;
                }
            }
        }

        MessageManager.sendClientMessage("Unknown command: " + split[0], false);
    }

    @SubscribeEvent
    public void onClientChat(ClientChatEvent event) {
        String prefix = (String) Chat.INSTANCE.chatPrefix.getValue();

        if (event.getMessage().startsWith(prefix)) {
            event.setCanceled(true);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
            if (event.getMessage().equalsIgnoreCase(prefix)) {
                this.callCommand("help");
            } else {
                this.callCommand(event.getMessage().substring(prefix.length()));
            }
        }

    }
}
