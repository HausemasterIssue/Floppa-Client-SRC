package me.brownzombie.floppa.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.gui.clickgui.ClickGui;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class ClickGuiModule extends Module {

    public Setting red = new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting green = new Setting("Green", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting blue = new Setting("Blue", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting alpha = new Setting("Alpha", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(255));
    public Setting rainbow = new Setting("Rainbow", Boolean.valueOf(false));
    public Setting rainbowSpeed = new Setting("Rainbow Speed", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(10), test<invokedynamic>(this));
    public Setting rainbowSat = new Setting("Rainbow Saturation", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting rainbowBright = new Setting("Rainbow Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting guiModeSetting;
    public Setting blurSetting;
    public static boolean blur = false;
    public static boolean isOutline = false;

    public ClickGuiModule() {
        super("ClickGUI", "The gui you can click.", Module.Category.CLIENT);
        this.guiModeSetting = new Setting("GL Mode", ClickGuiModule.GuiMode.SOLID);
        this.blurSetting = new Setting("Blur", Boolean.valueOf(false));
        this.setKey(23);
    }

    public void onEnable() {
        super.onEnable();
        Floppa floppa = Floppa.instance;

        ClickGuiModule.mc.displayGuiScreen(Floppa.clickGui);
    }

    public void onDisable() {
        if (((Boolean) Chat.INSTANCE.toggleMsg.getValue()).booleanValue() && this.visible) {
            MessageManager.sendClientMessage("ClickGui is " + ChatFormatting.RED + "Off", false);
        }

    }

    public void onUpdate() {
        ClickGuiModule.blur = ((Boolean) this.blurSetting.getValue()).booleanValue();
        ClickGuiModule.isOutline = this.guiModeSetting.getValue() != ClickGuiModule.GuiMode.SOLID;
        if (((Boolean) this.rainbow.getValue()).booleanValue()) {
            ClickGui.getInstance().color = ClickGui.getInstance().colorUtil.getRainbow(((Integer) this.rainbowSpeed.getValue()).intValue(), ((Integer) this.rainbowSat.getValue()).intValue(), ((Integer) this.rainbowBright.getValue()).intValue());
        } else {
            ClickGui.getInstance().color = new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.alpha.getValue()).intValue());
        }

        if (!(ClickGuiModule.mc.currentScreen instanceof ClickGui)) {
            this.setToggled(false);
        }

    }

    private boolean lambda$new$5(Integer g) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$4(Integer g) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$3(Integer g) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$2(Integer g) {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$1(Integer g) {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Integer g) {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    }

    public static enum GuiMode {

        SOLID, WIREFRAME;
    }
}
