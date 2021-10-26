package me.brownzombie.floppa.modules.render;

import java.util.function.Predicate;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class HandChams extends Module {

    public static HandChams INSTANCE;
    public Setting rainbow = new Setting("Rainbow", Boolean.valueOf(false));
    public Setting speed = new Setting("Speed", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(10), (v) -> {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting saturation = new Setting("Saturation", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting brightness = new Setting("Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting red = new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting green = new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting blue = new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), (v) -> {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting alpha = new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));

    public HandChams() {
        super("HandChams", "", Module.Category.RENDER);
        HandChams.INSTANCE = this;
    }
}
