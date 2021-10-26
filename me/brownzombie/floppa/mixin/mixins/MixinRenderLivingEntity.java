package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.modules.render.Chams;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RenderLivingBase.class})
public abstract class MixinRenderLivingEntity extends Render {

    protected MixinRenderLivingEntity() {
        super((RenderManager) null);
    }

    @Inject(
        method = { "doRender"},
        at = {             @At("HEAD")}
    )
    public void doRenderPre(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.INSTANCE.isToggled() && entity != null && entity instanceof EntityPlayer) {
            GL11.glEnable('耷');
            GL11.glPolygonOffset(1.0F, -1100000.0F);
        }

    }

    @Inject(
        method = { "doRender"},
        at = {             @At("RETURN")}
    )
    public void doRenderPost(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.INSTANCE.isToggled() && entity != null && entity instanceof EntityPlayer) {
            GL11.glPolygonOffset(1.0F, 1000000.0F);
            GL11.glDisable('耷');
        }

    }
}
