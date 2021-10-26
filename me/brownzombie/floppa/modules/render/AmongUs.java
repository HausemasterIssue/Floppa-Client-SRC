package me.brownzombie.floppa.modules.render;

import java.util.function.Predicate;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class AmongUs extends Module {

    public static AmongUs INSTANCE;
    public Setting colored = new Setting("Colored", Boolean.valueOf(false));
    public Setting red = new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.colored.getValue()).booleanValue();
    });
    public Setting green = new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.colored.getValue()).booleanValue();
    });
    public Setting blue = new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.colored.getValue()).booleanValue();
    });
    public Setting alpha = new Setting("Alpha", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.colored.getValue()).booleanValue();
    });

    public AmongUs() {
        super("AmongUs", "Sus", Module.Category.RENDER);
        AmongUs.INSTANCE = this;
    }
}
