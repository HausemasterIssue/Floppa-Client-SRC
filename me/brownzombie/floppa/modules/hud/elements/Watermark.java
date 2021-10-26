package me.brownzombie.floppa.modules.hud.elements;

import java.awt.Color;
import me.brownzombie.floppa.modules.hud.HudColorsModule;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import me.brownzombie.floppa.util.RenderUtil;

public class Watermark extends HudModuleTemplate {

    public Watermark() {
        super("Watermark");
    }

    public void onUpdate() {
        if (Watermark.mc.player != null && Watermark.mc.world != null) {
            this.stringVal = "Floppa 1.0";
            this.width = Watermark.mc.fontRenderer.getStringWidth(this.stringVal);
        }

    }

    public void onRenderOverlay(float partialTicks) {
        if (((Boolean) HudColorsModule.INSTANCE.rainbow.getValue()).booleanValue()) {
            RenderUtil.drawRainbowString(this.stringVal, (float) this.x, (float) this.y, HudColorsModule.INSTANCE.getHudRainbow().getRGB(), 100.0F, true);
        } else {
            Watermark.mc.fontRenderer.drawStringWithShadow(this.stringVal, (float) this.x, (float) this.y, (new Color(((Integer) HudColorsModule.INSTANCE.red.getValue()).intValue(), ((Integer) HudColorsModule.INSTANCE.green.getValue()).intValue(), ((Integer) HudColorsModule.INSTANCE.blue.getValue()).intValue(), 255)).getRGB());
        }

    }
}
