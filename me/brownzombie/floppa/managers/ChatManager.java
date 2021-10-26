package me.brownzombie.floppa.managers;

import me.brownzombie.floppa.gui.chat.GuiSmoothChat;
import me.brownzombie.floppa.modules.client.Chat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ChatManager {

    public static GuiSmoothChat smoothChatGUI;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (Chat.INSTANCE.isToggled()) {
            MinecraftForge.EVENT_BUS.unregister(this);
            ChatManager.smoothChatGUI = new GuiSmoothChat(Minecraft.getMinecraft());
            ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, Minecraft.getMinecraft().ingameGUI, ChatManager.smoothChatGUI, "persistantChatGUI");
        }

    }
}
