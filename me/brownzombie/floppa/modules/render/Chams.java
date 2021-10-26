package me.brownzombie.floppa.modules.render;

import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class Chams extends Module {

    public static Chams INSTANCE;
    public Setting red = new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    public Setting green = new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    public Setting blue = new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    public Setting alpha = new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    public Setting showLines = new Setting("ShowLines", Boolean.valueOf(false));
    public Setting colored = new Setting("Colored", Boolean.valueOf(false));

    public Chams() {
        super("Chams", "", Module.Category.RENDER);
        Chams.INSTANCE = this;
    }
}
