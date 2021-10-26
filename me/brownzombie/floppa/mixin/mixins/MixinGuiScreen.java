package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.modules.render.ToolTips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GuiScreen.class})
public final class MixinGuiScreen {

    ToolTips toolTips;

    public MixinGuiScreen() {
        this.toolTips = ToolTips.INSTANCE;
    }

    @Inject(
        method = { "renderToolTip"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void renderToolTip(ItemStack itemStack, int x, int y, CallbackInfo callbackInfo) {
        if (this.toolTips.isToggled() && !(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu)) {
            callbackInfo.cancel();
            ToolTips.renderTooltip(itemStack, x + 6, y - 33);
        }

    }
}
