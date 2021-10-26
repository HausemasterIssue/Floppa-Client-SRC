package me.brownzombie.floppa.modules.client;

import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.gui.windows95.Windows95;
import me.brownzombie.floppa.modules.Module;

public class Windows95Test extends Module {

    public Windows95Test() {
        super("Windows 95 Test", "Windows 95 ClickGUI Test", Module.Category.CLIENT);
    }

    public void onEnable() {
        super.onEnable();
        Floppa floppa = Floppa.instance;

        Windows95Test.mc.displayGuiScreen(Floppa.win95);
    }

    public void onUpdate() {
        if (!(Windows95Test.mc.currentScreen instanceof Windows95)) {
            this.setToggled(false);
        }

    }
}
