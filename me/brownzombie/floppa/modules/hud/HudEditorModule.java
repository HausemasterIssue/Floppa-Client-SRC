package me.brownzombie.floppa.modules.hud;

import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.modules.Module;

public class HudEditorModule extends Module {

    public static HudEditorModule INSTANCE;

    public HudEditorModule() {
        super("HudEditor", "Opens the HUD editor GUI.", Module.Category.HUD);
        HudEditorModule.INSTANCE = this;
    }

    public void onEnable() {
        super.onEnable();
        Floppa floppa = Floppa.instance;

        HudEditorModule.mc.displayGuiScreen(Floppa.hudEditor);
    }
}
