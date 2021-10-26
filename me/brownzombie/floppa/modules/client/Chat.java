package me.brownzombie.floppa.modules.client;

import me.brownzombie.floppa.gui.chat.GuiSmoothChat;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class Chat extends Module {

    public Setting toggleMsg = new Setting("Toggle Messages", Boolean.valueOf(false));
    public Setting smoothChat = new Setting("SmoothChat", Boolean.valueOf(false));
    public Setting clearChat = new Setting("ClearChat", Boolean.valueOf(false));
    public Setting rainbowSuffix = new Setting("RainbowSuffix", Boolean.valueOf(false), test<invokedynamic>(this));
    public Setting xOffset = new Setting("X Offset", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(600.0F));
    public Setting yOffset = new Setting("Y Offset", Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(30.0F));
    public Setting renderMode;
    public Setting vSpeed;
    public Setting vLength;
    public Setting vIncrements;
    public Setting suffixSpeed;
    public Setting suffixSat;
    public Setting suffixBright;
    public Setting chatPrefix;
    public static GuiSmoothChat smoothChatGUI;
    public static GuiNewChat guiChat;
    public static Chat INSTANCE;

    public Chat() {
        super("Chat", "Chat Modifications", Module.Category.CLIENT);
        this.renderMode = new Setting("Render Mode", Chat.RenderMode.HORIZONTAL, test<invokedynamic>(this));
        this.vSpeed = new Setting("V Speed", Integer.valueOf(30), Integer.valueOf(1), Integer.valueOf(100), test<invokedynamic>(this));
        this.vLength = new Setting("V Distance", Integer.valueOf(10), Integer.valueOf(5), Integer.valueOf(100), test<invokedynamic>(this));
        this.vIncrements = new Setting("V Increments", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(5), test<invokedynamic>(this));
        this.suffixSpeed = new Setting("R-Suffix Speed", Integer.valueOf(5), Integer.valueOf(1), Integer.valueOf(10), test<invokedynamic>(this));
        this.suffixSat = new Setting("R-Suffix Sat", Integer.valueOf(200), Integer.valueOf(1), Integer.valueOf(255), test<invokedynamic>(this));
        this.suffixBright = new Setting("R-Suffix Bright", Integer.valueOf(200), Integer.valueOf(1), Integer.valueOf(255), test<invokedynamic>(this));
        this.chatPrefix = new Setting("Prefix", ",");
        Chat.INSTANCE = this;
    }

    public void onEnable() {
        if (Chat.mc.player != null && Chat.mc.world != null) {
            MinecraftForge.EVENT_BUS.unregister(this);
            Chat.smoothChatGUI = new GuiSmoothChat(Minecraft.getMinecraft());
            ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, Minecraft.getMinecraft().ingameGUI, Chat.smoothChatGUI, "persistantChatGUI");
        }

    }

    public void onDisable() {
        if (Chat.mc.player != null && Chat.mc.world != null) {
            MinecraftForge.EVENT_BUS.unregister(this);
            Chat.guiChat = new GuiNewChat(Minecraft.getMinecraft());
            ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, Minecraft.getMinecraft().ingameGUI, Chat.guiChat, "persistantChatGUI");
        }

    }

    private boolean lambda$new$7(Integer k) {
        return ((Boolean) this.toggleMsg.getValue()).booleanValue() && ((Boolean) this.rainbowSuffix.getValue()).booleanValue();
    }

    private boolean lambda$new$6(Integer k) {
        return ((Boolean) this.toggleMsg.getValue()).booleanValue() && ((Boolean) this.rainbowSuffix.getValue()).booleanValue();
    }

    private boolean lambda$new$5(Integer k) {
        return ((Boolean) this.toggleMsg.getValue()).booleanValue() && ((Boolean) this.rainbowSuffix.getValue()).booleanValue();
    }

    private boolean lambda$new$4(Integer k) {
        return ((Chat.RenderMode) this.renderMode.getValue()).equals(Chat.RenderMode.VERTICAL) && ((Boolean) this.smoothChat.getValue()).booleanValue();
    }

    private boolean lambda$new$3(Integer k) {
        return ((Chat.RenderMode) this.renderMode.getValue()).equals(Chat.RenderMode.VERTICAL) && ((Boolean) this.smoothChat.getValue()).booleanValue();
    }

    private boolean lambda$new$2(Integer k) {
        return ((Chat.RenderMode) this.renderMode.getValue()).equals(Chat.RenderMode.VERTICAL) && ((Boolean) this.smoothChat.getValue()).booleanValue();
    }

    private boolean lambda$new$1(Chat.RenderMode k) {
        return ((Boolean) this.smoothChat.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Boolean k) {
        return ((Boolean) this.toggleMsg.getValue()).booleanValue();
    }

    public static enum RenderMode {

        HORIZONTAL, VERTICAL;
    }
}
