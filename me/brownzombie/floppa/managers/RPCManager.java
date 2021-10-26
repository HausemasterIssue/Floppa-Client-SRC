package me.brownzombie.floppa.managers;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.brownzombie.floppa.modules.misc.RPCModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class RPCManager {

    private static String discordID = "817590317075267584";
    private static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    private static Thread thread = null;

    public static void start() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();

        eventHandlers.disconnected = (var1, var2) -> {
            System.out.println("Discord RPC disconnected, var1: " + i + ", var2: " + s);
        };
        RPCManager.discordRPC.Discord_Initialize(RPCManager.discordID, eventHandlers, true, (String) null);
        RPCManager.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        if (RPCModule.instance.rpcModeSetting.getValue() == RPCModule.RPCMode.ADVANCED) {
            RPCManager.discordRichPresence.details = Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ? "In Main Menu" : "Playing " + (Minecraft.getMinecraft().currentServerData != null ? (((Boolean) RPCModule.instance.server.getValue()).booleanValue() ? Minecraft.getMinecraft().currentServerData.serverIP : " Multiplayer") : " Singleplayer");
        } else {
            RPCManager.discordRichPresence.details = "1.0";
        }

        RPCManager.discordRichPresence.largeImageKey = "coverimage";
        RPCManager.discordRichPresence.largeImageText = "Crippin\'";
        RPCManager.discordRPC.Discord_UpdatePresence(RPCManager.discordRichPresence);
        RPCManager.thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                RPCManager.discordRPC.Discord_RunCallbacks();
                if (RPCModule.instance.rpcModeSetting.getValue() == RPCModule.RPCMode.ADVANCED) {
                    RPCManager.discordRichPresence.details = Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ? "In Main Menu" : "Playing " + (Minecraft.getMinecraft().currentServerData != null ? (((Boolean) RPCModule.instance.server.getValue()).booleanValue() ? Minecraft.getMinecraft().currentServerData.serverIP : " Multiplayer") : " Singleplayer");
                } else {
                    RPCManager.discordRichPresence.details = "1.0";
                }

                RPCManager.discordRPC.Discord_UpdatePresence(RPCManager.discordRichPresence);

                try {
                    Thread.sleep(2500L);
                } catch (InterruptedException interruptedexception) {
                    ;
                }
            }

        }, "RPC-Callback-Handler");
        RPCManager.thread.start();
    }

    public static void stop() {
        if (RPCManager.thread != null && !RPCManager.thread.isInterrupted()) {
            RPCManager.thread.interrupt();
        }

        RPCManager.discordRPC.Discord_Shutdown();
        RPCManager.discordRPC.Discord_ClearPresence();
    }
}
