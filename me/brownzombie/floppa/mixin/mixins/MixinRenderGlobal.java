package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.modules.render.BlockHighlight;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ RenderGlobal.class})
public class MixinRenderGlobal {

    @Redirect(
        method = { "drawSelectionBox"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/util/math/AxisAlignedBB;offset(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"
            )
    )
    public AxisAlignedBB offsetHook(AxisAlignedBB axisAlignedBB, double x, double y, double z) {
        return BlockHighlight.INSTANCE.isToggled() ? axisAlignedBB.grow(0.0D, 0.0D, 0.0D) : axisAlignedBB.offset(x, y, z);
    }
}
