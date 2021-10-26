package me.brownzombie.floppa.modules.render;

import java.awt.Color;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.modules.hud.HudColorsModule;
import me.brownzombie.floppa.util.GuiUtil;
import me.brownzombie.floppa.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ToolTips extends Module {

    public static ToolTips INSTANCE;

    public ToolTips() {
        super("ToolTips", "Renders better looking tooltips", Module.Category.RENDER);
        ToolTips.INSTANCE = this;
    }

    public static void renderTooltip(ItemStack itemStack, int x, int y) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GuiUtil.drawRoundedRect((float) (x + 5), (float) (y + 10), (float) (x + ToolTips.mc.fontRenderer.getStringWidth(itemStack.getDisplayName()) + 30), (float) (y + 30), 3.0F, (new Color(0, 0, 0, 225)).getRGB());
        RenderUtil.drawRainbowString(itemStack.getDisplayName(), (float) (x + 8), (float) (y + 16), HudColorsModule.INSTANCE.getHudRainbow().getRGB(), 100.0F, false);
        GlStateManager.enableDepth();
        ToolTips.mc.getRenderItem().zLevel = 150.0F;
        RenderHelper.enableGUIStandardItemLighting();
        ToolTips.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x + ToolTips.mc.fontRenderer.getStringWidth(itemStack.getDisplayName()) + 12, y + 12);
        ToolTips.mc.getRenderItem().renderItemOverlayIntoGUI(ToolTips.mc.fontRenderer, itemStack, x + ToolTips.mc.fontRenderer.getStringWidth(itemStack.getDisplayName()) + 12, y + 12, (String) null);
        RenderHelper.disableStandardItemLighting();
        ToolTips.mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.enableLighting();
    }
}
