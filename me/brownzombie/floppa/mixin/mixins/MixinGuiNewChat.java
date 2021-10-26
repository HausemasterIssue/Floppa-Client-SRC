package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.client.Chat;
import me.brownzombie.floppa.util.ColorUtil;
import me.brownzombie.floppa.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ GuiNewChat.class})
public class MixinGuiNewChat extends Gui {

    public ColorUtil colorUtil = new ColorUtil();

    @Redirect(
        method = { "drawChat"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"
            )
    )
    private int drawStringWithShadow(FontRenderer fontRenderer, String text, float x, float y, int color) {
        if (Chat.INSTANCE.isToggled() && ((Boolean) Chat.INSTANCE.rainbowSuffix.getValue()).booleanValue() && text.contains(MessageManager.prefix)) {
            RenderUtil.drawRainbowString(text, x, y, this.colorUtil.getRainbow(((Integer) Chat.INSTANCE.suffixSpeed.getValue()).intValue(), ((Integer) Chat.INSTANCE.suffixSat.getValue()).intValue(), ((Integer) Chat.INSTANCE.suffixBright.getValue()).intValue()).getRGB(), 100.0F, true);
        } else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, color);
        }

        return 0;
    }
}
