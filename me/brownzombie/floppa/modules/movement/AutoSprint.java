package me.brownzombie.floppa.modules.movement;

import me.brownzombie.floppa.modules.Module;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint", "Lets you sprint without pressing le key", Module.Category.MOVEMENT);
    }

    public void onUpdate() {
        if (AutoSprint.mc.player != null && (!AutoSprint.mc.player.isSprinting() && (AutoSprint.mc.gameSettings.keyBindForward.isPressed() || AutoSprint.mc.gameSettings.keyBindLeft.isPressed()) || AutoSprint.mc.gameSettings.keyBindRight.isPressed())) {
            AutoSprint.mc.player.setSprinting(true);
        }

    }
}
