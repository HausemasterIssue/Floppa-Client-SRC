package me.brownzombie.floppa.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class MessageManager {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static String prefix = TextFormatting.GRAY + "《" + TextFormatting.GOLD + "Floppa" + TextFormatting.GRAY + "》" + TextFormatting.RESET;

    public static void sendClientMessage(String message, boolean forcePermanent) {
        if (MessageManager.mc.player != null) {
            try {
                TextComponentString e = new TextComponentString(MessageManager.prefix + " " + message);
                int i = forcePermanent ? 0 : 12076;

                MessageManager.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(e, i);
            } catch (NullPointerException nullpointerexception) {
                nullpointerexception.printStackTrace();
            }

        }
    }
}
