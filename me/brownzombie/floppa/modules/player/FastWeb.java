package me.brownzombie.floppa.modules.player;

import java.lang.reflect.Field;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.Mappings;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

public class FastWeb extends Module {

    public Setting webModeSetting;
    public Setting timerValue;

    public FastWeb() {
        super("FastWeb", "Makes u go thru webs fast", Module.Category.PLAYER);
        this.webModeSetting = new Setting("Mode", FastWeb.WebMode.TIMER);
        this.timerValue = new Setting("Timer", Float.valueOf(5.0F), Float.valueOf(0.1F), Float.valueOf(50.0F), test<invokedynamic>(this));
    }

    public void onUpdate() {
        if (FastWeb.mc.player != null && FastWeb.mc.world != null) {
            if (((FastWeb.WebMode) this.webModeSetting.getValue()).equals(FastWeb.WebMode.TELEPORT)) {
                if (FastWeb.mc.player.isInWeb && !FastWeb.mc.player.onGround) {
                    --FastWeb.mc.player.motionY;
                }
            } else if (((FastWeb.WebMode) this.webModeSetting.getValue()).equals(FastWeb.WebMode.TIMER)) {
                if (FastWeb.mc.player.isInWeb && !FastWeb.mc.player.onGround) {
                    this.setTimer(((Float) this.timerValue.getValue()).floatValue());
                } else if (FastWeb.mc.player.onGround) {
                    this.setTimer(1.0F);
                } else if (FastWeb.mc.player.isInsideOfMaterial(Material.AIR)) {
                    this.setTimer(1.0F);
                } else {
                    this.setTimer(1.0F);
                }
            } else if (FastWeb.mc.player.isInWeb && !FastWeb.mc.player.onGround) {
                FastWeb.mc.player.isInWeb = false;
            }
        }

    }

    public void onDisable() {
        if (FastWeb.mc.player != null && FastWeb.mc.world != null) {
            this.setTimer(1.0F);
        }

    }

    private void setTimer(float value) {
        try {
            Field e = Minecraft.class.getDeclaredField(Mappings.timer);

            e.setAccessible(true);
            Field tickLength = Timer.class.getDeclaredField(Mappings.tickLength);

            tickLength.setAccessible(true);
            tickLength.setFloat(e.get(FastWeb.mc), 50.0F / value);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private boolean lambda$new$0(Float w) {
        return ((FastWeb.WebMode) this.webModeSetting.getValue()).equals(FastWeb.WebMode.TIMER);
    }

    public static enum WebMode {

        VANILLA, TIMER, TELEPORT;
    }
}
