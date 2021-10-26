package me.brownzombie.floppa.managers;

import java.util.ArrayList;
import java.util.List;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.gui.clickgui.ClickGui;
import me.brownzombie.floppa.gui.hudeditor.HudEditor;
import me.brownzombie.floppa.gui.windows95.Windows95;
import me.brownzombie.floppa.modules.Module;

public class GUIManager {

    public static Windows95 win95;
    public static ClickGui clickGui;
    public static HudEditor hudEditor;
    public static ModuleManager moduleManager = Floppa.moduleManager;
    private List guiList = new ArrayList();

    public GUIManager() {
        GUIManager.clickGui = new ClickGui();
        GUIManager.win95 = new Windows95();
        GUIManager.hudEditor = new HudEditor();
        this.guiList.add(GUIManager.moduleManager.getModule("ClickGUI"));
        this.guiList.add(GUIManager.moduleManager.getModule("HudEditor"));
        this.guiList.add(GUIManager.moduleManager.getModule("Windows 95 Test"));
    }

    public ClickGui getClickGui() {
        return GUIManager.clickGui;
    }

    public HudEditor getHudEditor() {
        return GUIManager.hudEditor;
    }

    public Windows95 getWin95() {
        return GUIManager.win95;
    }

    public void setGuis() {
        Module clickGuiModule = GUIManager.moduleManager.getModule("ClickGUI");
        Module hudEditorModule = GUIManager.moduleManager.getModule("HudEditor");
        Module win95Module = GUIManager.moduleManager.getModule("Windows 95 Test");

        clickGuiModule.setToggled(false);
        hudEditorModule.setToggled(false);
        win95Module.setToggled(false);
    }
}
