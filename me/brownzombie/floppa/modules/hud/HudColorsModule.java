package me.brownzombie.floppa.modules.hud;

import java.awt.Color;
import java.util.function.Predicate;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class HudColorsModule extends Module {

    public Setting rainbow = new Setting("Rainbow", Boolean.valueOf(false));
    public Setting red = new Setting("Red", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting green = new Setting("Green", Integer.valueOf(200), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting blue = new Setting("Blue", Integer.valueOf(200), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting rSpeed = new Setting("RainbowSpeed", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(20), (c) -> {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting rSat = new Setting("RainbowSat", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public Setting rBright = new Setting("RainbowBright", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    });
    public static HudColorsModule INSTANCE;
    private float hue = 0.0F;

    public HudColorsModule() {
        super("HudColors", "Module to choose hud colors.", Module.Category.HUD);
        HudColorsModule.INSTANCE = this;
    }

    public void onUpdate() {
        this.hue += (float) ((Integer) this.rSpeed.getValue()).intValue() / 4000.0F;
    }

    public void onDisable() {
        this.setToggled(true);
    }

    public Color getHudRainbow() {
        return Color.getHSBColor(this.hue, (float) ((Integer) this.rSat.getValue()).intValue() / 255.0F, (float) ((Integer) this.rBright.getValue()).intValue() / 255.0F);
    }
}
