package me.brownzombie.floppa.modules.misc;

import me.brownzombie.floppa.managers.RPCManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class RPCModule extends Module {

    public Setting server = new Setting("Server", Boolean.valueOf(false));
    public Setting rpcModeSetting;
    public static RPCModule instance;

    public RPCModule() {
        super("RPC", "Discord Rich Presence", Module.Category.MISC);
        this.rpcModeSetting = new Setting("RPC Mode", RPCModule.RPCMode.SIMPLE);
        RPCModule.instance = this;
    }

    public void onEnable() {
        RPCManager.start();
    }

    public void onDisable() {
        RPCManager.stop();
    }

    public static enum RPCMode {

        SIMPLE, ADVANCED;
    }
}
