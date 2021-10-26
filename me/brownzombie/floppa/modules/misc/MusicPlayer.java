package me.brownzombie.floppa.modules.misc;

import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class MusicPlayer extends Module {

    public Setting word = new Setting("Word", "nut");

    public MusicPlayer() {
        super("MusicPlayer", "", Module.Category.MISC);
    }

    public void onEnable() {}
}
