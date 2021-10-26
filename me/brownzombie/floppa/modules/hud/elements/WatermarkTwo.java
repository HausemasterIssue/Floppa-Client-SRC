package me.brownzombie.floppa.modules.hud.elements;

import java.awt.Color;
import me.brownzombie.floppa.modules.hud.HudColorsModule;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class WatermarkTwo extends HudModuleTemplate {

    private ResourceLocation logo;

    public WatermarkTwo() {
        super("WatermarkTwo");
    }

    public void onUpdate() {
        if (WatermarkTwo.mc.player != null && WatermarkTwo.mc.world != null) {
            this.stringVal = "Floppa 1.0";
            this.width = WatermarkTwo.mc.fontRenderer.getStringWidth(this.stringVal);
        }

    }

    public void onEnable() {
        this.logo = new ResourceLocation("minecraft:images/watermark.png");
    }

    public void onRenderOverlay(float partialTicks) {
        if (WatermarkTwo.mc.player != null && WatermarkTwo.mc.world != null) {
            Color color = HudColorsModule.INSTANCE.getHudRainbow();

            WatermarkTwo.mc.renderEngine.bindTexture(this.logo);
            GlStateManager.color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F);
            Gui.drawScaledCustomSizeModalRect(this.x + 4, this.y + 4, 7.0F, 7.0F, 243, 43, 250, 50, 250.0F, 50.0F);
        }

    }
}
