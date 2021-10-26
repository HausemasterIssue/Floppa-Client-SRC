package me.brownzombie.floppa.mixin.mixins;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ EntityRenderer.class})
public class MixinEntityRenderer {

    @Shadow
    public ItemStack itemActivationItem;
}
