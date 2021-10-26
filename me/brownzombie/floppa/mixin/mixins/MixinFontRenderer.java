package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.modules.client.NameChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ FontRenderer.class})
public abstract class MixinFontRenderer {

    @Shadow
    protected abstract void renderStringAtPos(String s, boolean flag);

    @Redirect(
        method = { "renderString(Ljava/lang/String;FFIZ)I"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"
            )
    )
    public void renderStringAtPosHook(FontRenderer renderer, String text, boolean shadow) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            if (NameChanger.getInstance().isToggled()) {
                this.renderStringAtPos(text.replace(NameChanger.getOldName(), (CharSequence) NameChanger.getInstance().newUsername.getValue()), shadow);
            } else {
                this.renderStringAtPos(text, shadow);
            }
        } else {
            this.renderStringAtPos(text, shadow);
        }

    }
}
