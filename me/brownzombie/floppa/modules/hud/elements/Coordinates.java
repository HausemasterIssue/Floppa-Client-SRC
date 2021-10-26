package me.brownzombie.floppa.modules.hud.elements;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.brownzombie.floppa.modules.hud.HudColorsModule;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import me.brownzombie.floppa.util.RenderUtil;

public class Coordinates extends HudModuleTemplate {

    public Coordinates() {
        super("Coordinates");
    }

    public void onUpdate() {
        if (Coordinates.mc.player != null && Coordinates.mc.world != null) {
            String x = ((Boolean) HudColorsModule.INSTANCE.rainbow.getValue()).booleanValue() ? ChatFormatting.RESET + String.valueOf((int) Coordinates.mc.player.posX) : ChatFormatting.WHITE + String.valueOf((int) Coordinates.mc.player.posX);
            String y = ((Boolean) HudColorsModule.INSTANCE.rainbow.getValue()).booleanValue() ? ChatFormatting.RESET + String.valueOf((int) Coordinates.mc.player.posY) : ChatFormatting.WHITE + String.valueOf((int) Coordinates.mc.player.posY);
            String z = ((Boolean) HudColorsModule.INSTANCE.rainbow.getValue()).booleanValue() ? ChatFormatting.RESET + String.valueOf((int) Coordinates.mc.player.posZ) : ChatFormatting.WHITE + String.valueOf((int) Coordinates.mc.player.posZ);

            this.stringVal = "XYZ: " + x + " " + y + " " + z;
        }

    }

    public void onRenderOverlay(float partialTicks) {
        if (((Boolean) HudColorsModule.INSTANCE.rainbow.getValue()).booleanValue()) {
            RenderUtil.drawRainbowString(this.stringVal, (float) this.x, (float) this.y, HudColorsModule.INSTANCE.getHudRainbow().getRGB(), 100.0F, true);
        } else {
            Coordinates.mc.fontRenderer.drawStringWithShadow(this.stringVal, (float) this.x, (float) this.y, (new Color(((Integer) HudColorsModule.INSTANCE.red.getValue()).intValue(), ((Integer) HudColorsModule.INSTANCE.green.getValue()).intValue(), ((Integer) HudColorsModule.INSTANCE.blue.getValue()).intValue(), 255)).getRGB());
        }

    }
}
