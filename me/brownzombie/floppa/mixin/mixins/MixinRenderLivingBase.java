package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.event.events.RenderEntityModelEvent;
import me.brownzombie.floppa.modules.render.Chams;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RenderLivingBase.class})
public abstract class MixinRenderLivingBase extends Render {

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    @Redirect(
        method = { "renderModel"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"
            )
    )
    public void renderModelHook(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        boolean cancelled = false;
        RenderEntityModelEvent event = new RenderEntityModelEvent(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        if (event.isCanceled()) {
            cancelled = true;
        }

        if (Chams.INSTANCE.isToggled() && entityIn instanceof EntityPlayer) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5F);
            GL11.glEnable(2960);
            if (((Boolean) Chams.INSTANCE.showLines.value).booleanValue()) {
                GL11.glPolygonMode(1028, 6913);
            }

            if (((Boolean) Chams.INSTANCE.colored.value).booleanValue()) {
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glEnable(10754);
                GL11.glColor4f((float) ((Integer) Chams.INSTANCE.red.value).intValue() / 255.0F, (float) ((Integer) Chams.INSTANCE.green.value).intValue() / 255.0F, (float) ((Integer) Chams.INSTANCE.blue.value).intValue() / 255.0F, (float) ((Integer) Chams.INSTANCE.alpha.value).intValue() / 255.0F);
                modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
            }

            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        } else if (!cancelled) {
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

    }

    @Inject(
        method = { "doRender"},
        at = {             @At("HEAD")}
    )
    public void doRenderPre(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.INSTANCE.isToggled() && !((Boolean) Chams.INSTANCE.colored.getValue()).booleanValue() && entity != null) {
            GL11.glEnable('耷');
            GL11.glPolygonOffset(1.0F, -1100000.0F);
        }

    }

    @Inject(
        method = { "doRender"},
        at = {             @At("RETURN")}
    )
    public void doRenderPost(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if (Chams.INSTANCE.isToggled() && !((Boolean) Chams.INSTANCE.colored.getValue()).booleanValue() && entity != null) {
            GL11.glPolygonOffset(1.0F, 1000000.0F);
            GL11.glDisable('耷');
        }

    }
}
