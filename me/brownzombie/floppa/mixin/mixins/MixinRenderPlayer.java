package me.brownzombie.floppa.mixin.mixins;

import java.awt.Color;
import java.util.Random;
import me.brownzombie.floppa.modules.client.SkinChanger;
import me.brownzombie.floppa.modules.render.AmongUs;
import me.brownzombie.floppa.modules.render.HandChams;
import me.brownzombie.floppa.util.ColorUtil;
import me.brownzombie.floppa.util.SkinStorageManipulationer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RenderPlayer.class})
public class MixinRenderPlayer {

    public ColorUtil colorUtil = new ColorUtil();

    @Inject(
        method = { "renderRightArm"},
        at = {             @At(
                value = "FIELD",
                target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F",
                opcode = 181
            )},
        cancellable = true
    )
    public void renderRightArmHook(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandChams.INSTANCE.isToggled()) {
            GL11.glPushClientAttrib(-1);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5F);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            Color color = ((Boolean) HandChams.INSTANCE.rainbow.getValue()).booleanValue() ? new Color(this.colorUtil.getRainbow(((Integer) HandChams.INSTANCE.speed.getValue()).intValue(), ((Integer) HandChams.INSTANCE.saturation.getValue()).intValue(), ((Integer) HandChams.INSTANCE.brightness.getValue()).intValue()).getRGB()) : new Color(((Integer) HandChams.INSTANCE.red.getValue()).intValue(), ((Integer) HandChams.INSTANCE.green.getValue()).intValue(), ((Integer) HandChams.INSTANCE.blue.getValue()).intValue(), ((Integer) HandChams.INSTANCE.alpha.getValue()).intValue());

            GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) ((Integer) HandChams.INSTANCE.alpha.getValue()).intValue() / 255.0F);
        }

    }

    @Inject(
        method = { "renderRightArm"},
        at = {             @At("RETURN")},
        cancellable = true
    )
    public void renderRightArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandChams.INSTANCE.isToggled()) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }

    }

    @Inject(
        method = { "renderLeftArm"},
        at = {             @At(
                value = "FIELD",
                target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F",
                opcode = 181
            )},
        cancellable = true
    )
    public void renderLeftArmHook(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandChams.INSTANCE.isToggled()) {
            GL11.glPushClientAttrib(-1);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5F);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            Color color = ((Boolean) HandChams.INSTANCE.rainbow.getValue()).booleanValue() ? new Color(this.colorUtil.getRainbow(((Integer) HandChams.INSTANCE.speed.getValue()).intValue(), ((Integer) HandChams.INSTANCE.saturation.getValue()).intValue(), ((Integer) HandChams.INSTANCE.brightness.getValue()).intValue()).getRGB()) : new Color(((Integer) HandChams.INSTANCE.red.getValue()).intValue(), ((Integer) HandChams.INSTANCE.green.getValue()).intValue(), ((Integer) HandChams.INSTANCE.blue.getValue()).intValue(), ((Integer) HandChams.INSTANCE.alpha.getValue()).intValue());

            GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) ((Integer) HandChams.INSTANCE.alpha.getValue()).intValue() / 255.0F);
        }

    }

    @Inject(
        method = { "renderLeftArm"},
        at = {             @At("RETURN")},
        cancellable = true
    )
    public void renderLeftArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandChams.INSTANCE.isToggled()) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }

    }

    @Overwrite
    public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
        if (AmongUs.INSTANCE.isToggled() && entity != Minecraft.getMinecraft().player) {
            if (((Boolean) AmongUs.INSTANCE.colored.getValue()).booleanValue()) {
                GL11.glColor4f((float) ((Integer) AmongUs.INSTANCE.red.getValue()).intValue() / 255.0F, (float) ((Integer) AmongUs.INSTANCE.green.getValue()).intValue() / 255.0F, (float) ((Integer) AmongUs.INSTANCE.blue.getValue()).intValue() / 255.0F, (float) ((Integer) AmongUs.INSTANCE.alpha.getValue()).intValue() / 255.0F);
            }

            boolean numb = false;
            Random rand = new Random();
            int numb1 = rand.nextInt(3);

            if (entity != Minecraft.getMinecraft().player) {
                if (numb1 == 1) {
                    return new ResourceLocation("minecraft:images/red.png");
                }

                return new ResourceLocation("minecraft:images/orange.png");
            }
        } else if (SkinChanger.INSTANCE.isToggled() && entity == Minecraft.getMinecraft().player) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            return new ResourceLocation(SkinStorageManipulationer.getTexture().toString());
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        return entity.getLocationSkin();
    }
}
