package me.brownzombie.floppa.modules.hud.elements;

import java.awt.Color;
import java.util.Iterator;
import me.brownzombie.floppa.modules.hud.HudColorsModule;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import me.brownzombie.floppa.util.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;

public class TextRadar extends HudModuleTemplate {

    public TextRadar() {
        super("TextRadar");
    }

    public void onRenderOverlay(float partialTicks) {
        int count = 0;

        for (Iterator iterator = TextRadar.mc.world.playerEntities.iterator(); iterator.hasNext(); this.height = count) {
            EntityPlayer entity = (EntityPlayer) iterator.next();

            if (entity != TextRadar.mc.player && entity.getDistance(TextRadar.mc.player) < (float) (TextRadar.mc.gameSettings.renderDistanceChunks * 16)) {
                String name = entity.getName();

                if (((Boolean) HudColorsModule.INSTANCE.rainbow.value).booleanValue()) {
                    RenderUtil.drawRainbowString(entity.getName(), (float) this.x, (float) (this.y + count), this.colorUtil.getRainbow(((Integer) HudColorsModule.INSTANCE.rSpeed.getValue()).intValue(), ((Integer) HudColorsModule.INSTANCE.rSat.getValue()).intValue(), ((Integer) HudColorsModule.INSTANCE.rBright.getValue()).intValue()).getRGB(), 100.0F, true);
                } else {
                    this.fr.drawStringWithShadow(entity.getName(), (float) this.x, (float) (this.y + count), (new Color(((Integer) HudColorsModule.INSTANCE.red.getValue()).intValue(), ((Integer) HudColorsModule.INSTANCE.green.getValue()).intValue(), ((Integer) HudColorsModule.INSTANCE.blue.getValue()).intValue(), 255)).getRGB());
                }

                if (this.fr.getStringWidth(name) > this.width) {
                    this.width = this.fr.getStringWidth(name);
                }

                count += this.fr.FONT_HEIGHT;
            }
        }

    }
}
