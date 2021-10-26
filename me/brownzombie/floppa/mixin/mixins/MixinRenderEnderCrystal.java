package me.brownzombie.floppa.mixin.mixins;

import java.awt.Color;
import me.brownzombie.floppa.event.events.RenderEntityModelEvent;
import me.brownzombie.floppa.modules.render.CrystalChams;
import me.brownzombie.floppa.util.ColorUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {

    public ColorUtil colorUtil = new ColorUtil();

    @Redirect(
        method = { "doRender"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"
            )
    )
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (CrystalChams.INSTANCE.isToggled()) {
            GlStateManager.scale(((Float) CrystalChams.INSTANCE.crystalScale.getValue()).floatValue(), ((Float) CrystalChams.INSTANCE.crystalScale.getValue()).floatValue(), ((Float) CrystalChams.INSTANCE.crystalScale.getValue()).floatValue());
        } else {
            GlStateManager.scale(1.0F, 1.0F, 1.0F);
        }

        if (CrystalChams.INSTANCE.isToggled() && !((Boolean) CrystalChams.INSTANCE.renderModel.getValue()).booleanValue()) {
            GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        if (CrystalChams.INSTANCE.isToggled() && CrystalChams.INSTANCE.renderMode.getValue() == CrystalChams.Mode.WIREFRAME || CrystalChams.INSTANCE.renderMode.getValue() == CrystalChams.Mode.FULL) {
            RenderEntityModelEvent visibleColor = new RenderEntityModelEvent(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            CrystalChams.INSTANCE.onRenderModel(visibleColor);
        }

        if (CrystalChams.INSTANCE.isToggled() && (((CrystalChams.Mode) CrystalChams.INSTANCE.renderMode.getValue()).equals(CrystalChams.Mode.SOLID) || CrystalChams.INSTANCE.renderMode.getValue() == CrystalChams.Mode.FULL)) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5F);
            GL11.glEnable(2960);
            Color visibleColor1;

            if (((Boolean) CrystalChams.INSTANCE.rainbow.getValue()).booleanValue()) {
                visibleColor1 = new Color(this.colorUtil.getRainbow(((Integer) CrystalChams.INSTANCE.speed.getValue()).intValue(), ((Integer) CrystalChams.INSTANCE.saturation.getValue()).intValue(), ((Integer) CrystalChams.INSTANCE.brightness.getValue()).intValue()).getRGB());
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glEnable(10754);
                GL11.glColor4f((float) visibleColor1.getRed() / 255.0F, (float) visibleColor1.getGreen() / 255.0F, (float) visibleColor1.getBlue() / 255.0F, (float) ((Integer) CrystalChams.INSTANCE.alpha.getValue()).intValue() / 255.0F);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
            } else {
                visibleColor1 = new Color(((Integer) CrystalChams.INSTANCE.red.getValue()).intValue(), ((Integer) CrystalChams.INSTANCE.green.getValue()).intValue(), ((Integer) CrystalChams.INSTANCE.blue.getValue()).intValue(), ((Integer) CrystalChams.INSTANCE.alpha.getValue()).intValue());
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glEnable(10754);
                GL11.glColor4f((float) visibleColor1.getRed() / 255.0F, (float) visibleColor1.getGreen() / 255.0F, (float) visibleColor1.getBlue() / 255.0F, (float) ((Integer) CrystalChams.INSTANCE.alpha.getValue()).intValue() / 255.0F);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
            }

            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        if (CrystalChams.INSTANCE.isToggled()) {
            GlStateManager.scale(((Float) CrystalChams.INSTANCE.crystalScale.getValue()).floatValue(), ((Float) CrystalChams.INSTANCE.crystalScale.getValue()).floatValue(), ((Float) CrystalChams.INSTANCE.crystalScale.getValue()).floatValue());
        } else {
            GlStateManager.scale(1.0F, 1.0F, 1.0F);
        }

    }
}
