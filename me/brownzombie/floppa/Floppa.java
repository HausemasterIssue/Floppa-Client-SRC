package me.brownzombie.floppa;

import java.awt.TrayIcon.MessageType;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;
import me.brownzombie.floppa.event.Adapter;
import me.brownzombie.floppa.gui.chat.GuiSmoothChat;
import me.brownzombie.floppa.gui.clickgui.ClickGui;
import me.brownzombie.floppa.gui.hudeditor.HudEditor;
import me.brownzombie.floppa.gui.windows95.Windows95;
import me.brownzombie.floppa.managers.ChatManager;
import me.brownzombie.floppa.managers.CommandManager;
import me.brownzombie.floppa.managers.ConfigManager;
import me.brownzombie.floppa.managers.GUIManager;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.managers.ModuleManager;
import me.brownzombie.floppa.managers.NotificationManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.modules.client.Chat;
import me.brownzombie.floppa.modules.misc.DesktopNotifs;
import me.brownzombie.floppa.util.GuiUtil;
import me.brownzombie.floppa.util.RotationUtil;
import me.brownzombie.floppa.util.ServerManager;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Floppa {

    public static final String MODID = "floppa";
    public static final String NAME = "Floppa";
    public static final String VERSION = "1.0";
    public static final EventBus EVENT_BUS = new EventManager();
    public static final String appId = "";
    public static Floppa instance;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static GuiUtil guiUtil;
    public static ModuleManager moduleManager;
    public static GUIManager guiManager;
    public static MessageManager messageManager;
    public static RotationUtil rotationUtil;
    public static CommandManager commandManager;
    public static NotificationManager notifManager;
    public static ClickGui clickGui;
    public static HudEditor hudEditor;
    public static Windows95 win95;
    public static GuiSmoothChat smoothChatGUI;

    public void init() throws IllegalAccessException {
        Floppa.messageManager = new MessageManager();
        Floppa.configManager = new ConfigManager();
        Floppa.moduleManager = new ModuleManager();
        Floppa.serverManager = new ServerManager();
        Floppa.commandManager = new CommandManager();
        Floppa.notifManager = new NotificationManager();
        Display.setTitle("Floppa");
        Floppa.clickGui = new ClickGui();
        Floppa.hudEditor = new HudEditor();
        Floppa.win95 = new Windows95();
        Floppa.guiUtil = new GuiUtil();
        Floppa.rotationUtil = new RotationUtil();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Adapter());
        MinecraftForge.EVENT_BUS.register(new ChatManager());
        Floppa.moduleManager.getModules().sort(Comparator.comparing((s) -> {
            return Integer.valueOf(s.getName().length());
        }));
        DesktopNotifs.sendNotification("Floppa 1.0 initialized!", MessageType.NONE);
    }

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event) {
        Iterator chatKey = Floppa.moduleManager.getModules().iterator();

        while (chatKey.hasNext()) {
            Module m = (Module) chatKey.next();

            if (Keyboard.isKeyDown(m.getKey())) {
                m.setToggled(!m.isToggled());
            }
        }

        if (Keyboard.isKeyDown(Keyboard.getKeyIndex((String) Chat.INSTANCE.chatPrefix.getValue())) && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            int chatKey1 = Minecraft.getMinecraft().gameSettings.keyBindChat.getKeyCode();

            KeyBinding.setKeyBindState(chatKey1, true);
            KeyBinding.onTick(chatKey1);
        }

    }

    public static Floppa getInstance() {
        return Floppa.instance;
    }
}
