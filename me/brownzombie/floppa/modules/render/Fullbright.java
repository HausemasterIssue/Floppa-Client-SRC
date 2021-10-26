package me.brownzombie.floppa.modules.render;

import me.brownzombie.floppa.modules.Module;

public class Fullbright extends Module {

    public Fullbright() {
        super("Fullbright", "Seatbelt can\'t make this", Module.Category.RENDER);
        this.setKey(35);
    }

    public void onEnable() {
        Fullbright.mc.gameSettings.gammaSetting = 100.0F;
    }

    public void onDisable() {
        Fullbright.mc.gameSettings.gammaSetting = 1.0F;
    }
}
