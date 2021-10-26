package me.brownzombie.floppa.mixin.mixins;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GuiMainMenu.class})
public class MixinGuiMainMenu extends GuiScreen {

    @Redirect(
        method = { "drawScreen"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/GuiMainMenu;renderSkybox(IIF)V"
            )
    )
    private void voided(GuiMainMenu guiMainMenu, int mouseX, int mouseY, float partialTicks) {}

    @Redirect(
        method = { "drawScreen"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V",
                ordinal = 0
            )
    )
    private void noRect1(GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor) {}

    @Redirect(
        method = { "drawScreen"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V",
                ordinal = 1
            )
    )
    private void noRect2(GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor) {}

    @Inject(
        method = { "drawScreen"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void drawScreenShader(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        new ScaledResolution(this.mc);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("minecraft:images/background.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, this.width, this.height, (float) this.width, (float) this.height);
    }
}
