package me.brownzombie.floppa.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GuiUtil {

    protected static float zLevel;
    private static final double twicePI = 6.283185307179586D;

    public static void drawRect(float left, float top, float right, float bottom, int color, int glMode, float lineWidth) {
        GlStateManager.glLineWidth(lineWidth);
        float j;

        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(glMode, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
        bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(int left, int top, int right, int bottom, int color, int glMode, float lineWidth) {
        GlStateManager.glLineWidth(lineWidth);
        int j;

        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(glMode, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
        bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, int glMode, float lineWidth) {
        GlStateManager.glLineWidth(lineWidth);
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(glMode, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) right, (double) top, (double) GuiUtil.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) top, (double) GuiUtil.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, (double) GuiUtil.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, (double) GuiUtil.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawCircleOne(double x, double y, int radius, int slices, int color) {
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;

        GL11.glBegin(6);
        GL11.glVertex2d(x, y);
        GL11.glColor4f(f, f1, f2, f3);
        GlStateManager.color(f, f1, f2, f3);

        for (int i = 0; i <= slices; ++i) {
            GL11.glVertex2d(x + (double) radius * Math.cos((double) i * 6.283185307179586D / (double) slices), y + (double) radius * Math.sin((double) i * 6.283185307179586D / (double) slices));
        }

        GL11.glEnd();
    }

    public static void drawCircleTwo(float x, float y, float r, int num_segments, int color) {
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glScalef(r, r, 1.0F);
        GL11.glBegin(6);
        GL11.glVertex2f(0.0F, 0.0F);
        GL11.glColor4f(f, f1, f2, f3);
        GlStateManager.color(f, f1, f2, f3);

        for (int i = 0; i <= num_segments; ++i) {
            double angle = 6.283185307179586D * (double) i / 20.0D;

            GL11.glVertex2f((float) Math.cos(angle), (float) Math.sin(angle));
        }

        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawCircle(float x, float y, float radius, Color color) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.001F);
        GlStateManager.enableBlend();
        GL11.glDisable(3553);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
        GL11.glBegin(9);

        double ps;
        double cs;
        double i;
        double[] outer;

        for (i = 0.0D; i < 36.0D; ++i) {
            cs = i * 10.0D * 3.141592653589793D / 180.0D;
            ps = (i * 10.0D - 1.0D) * 3.141592653589793D / 180.0D;
            outer = new double[] { Math.cos(cs) * (double) radius, -Math.sin(cs) * (double) radius, Math.cos(ps) * (double) radius, -Math.sin(ps) * (double) radius};
            GL11.glVertex2d((double) x + outer[0], (double) y + outer[1]);
        }

        GL11.glEnd();
        GL11.glEnable(2848);
        GL11.glBegin(3);

        for (i = 0.0D; i < 37.0D; ++i) {
            cs = i * 10.0D * 3.141592653589793D / 180.0D;
            ps = (i * 10.0D - 1.0D) * 3.141592653589793D / 180.0D;
            outer = new double[] { Math.cos(cs) * (double) radius, -Math.sin(cs) * (double) radius, Math.cos(ps) * (double) radius, -Math.sin(ps) * (double) radius};
            GL11.glVertex2d((double) x + outer[0], (double) y + outer[1]);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.alphaFunc(516, 0.1F);
    }

    public static void drawRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
        boolean numberOfArcs = true;
        float angleIncrement = 5.0F;
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(r, g, b, a);
        GL11.glBegin(5);
        GL11.glVertex2f(x0 + radius, y0);
        GL11.glVertex2f(x0 + radius, y1);
        GL11.glVertex2f(x1 - radius, y0);
        GL11.glVertex2f(x1 - radius, y1);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x0, y0 + radius);
        GL11.glVertex2f(x0 + radius, y0 + radius);
        GL11.glVertex2f(x0, y1 - radius);
        GL11.glVertex2f(x0 + radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x1, y0 + radius);
        GL11.glVertex2f(x1 - radius, y0 + radius);
        GL11.glVertex2f(x1, y1 - radius);
        GL11.glVertex2f(x1 - radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(6);
        float centerX = x1 - radius;
        float centerY = y0 + radius;

        GL11.glVertex2f(centerX, centerY);

        int i;
        float angle;

        for (i = 0; i <= 18; ++i) {
            angle = (float) i * 5.0F;
            GL11.glVertex2f((float) ((double) centerX + (double) radius * Math.cos(Math.toRadians((double) angle))), (float) ((double) centerY - (double) radius * Math.sin(Math.toRadians((double) angle))));
        }

        GL11.glEnd();
        GL11.glBegin(6);
        centerX = x0 + radius;
        centerY = y0 + radius;
        GL11.glVertex2f(centerX, centerY);

        for (i = 0; i <= 18; ++i) {
            angle = (float) i * 5.0F;
            GL11.glVertex2f((float) ((double) centerX - (double) radius * Math.cos(Math.toRadians((double) angle))), (float) ((double) centerY - (double) radius * Math.sin(Math.toRadians((double) angle))));
        }

        GL11.glEnd();
        GL11.glBegin(6);
        centerX = x0 + radius;
        centerY = y1 - radius;
        GL11.glVertex2f(centerX, centerY);

        for (i = 0; i <= 18; ++i) {
            angle = (float) i * 5.0F;
            GL11.glVertex2f((float) ((double) centerX - (double) radius * Math.cos(Math.toRadians((double) angle))), (float) ((double) centerY + (double) radius * Math.sin(Math.toRadians((double) angle))));
        }

        GL11.glEnd();
        GL11.glBegin(6);
        centerX = x1 - radius;
        centerY = y1 - radius;
        GL11.glVertex2f(centerX, centerY);

        for (i = 0; i <= 18; ++i) {
            angle = (float) i * 5.0F;
            GL11.glVertex2f((float) ((double) centerX + (double) radius * Math.cos(Math.toRadians((double) angle))), (float) ((double) centerY + (double) radius * Math.sin(Math.toRadians((double) angle))));
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GlStateManager.disableBlend();
    }

    public static void glScissor(int x, int y, int width, int height) {
        GL11.glScissor(x, Minecraft.getMinecraft().displayHeight - y * 2 - height, width, height);
    }

    public static void drawBorderedRect(float x, float y, float w, float h, float thickness, int c, int mode) {
        switch (mode) {
        case 1:
            drawRect((double) x, (double) y, (double) (x - thickness), (double) h, c);
            drawRect((double) (w + thickness), (double) y, (double) w, (double) h, c);
            drawRect((double) x, (double) y, (double) w, (double) (y - thickness), c);
            drawRect((double) x, (double) (h + thickness), (double) w, (double) h, c);
            break;

        case 2:
            drawRect((double) (w + thickness), (double) y, (double) w, (double) h, c);
            drawRect((double) x, (double) y, (double) w, (double) (y - thickness), c);
            break;

        case 3:
            drawRect((double) x, (double) y, (double) (x - thickness), (double) h, c);
            drawRect((double) x, (double) (h + thickness), (double) w, (double) h, c);
        }

    }

    public static void drawRect(double par0, double par1, double par2, double par3, int par4) {
        double d0;

        if (par0 < par2) {
            d0 = par0;
            par0 = par2;
            par2 = d0;
        }

        if (par1 < par3) {
            d0 = par1;
            par1 = par3;
            par3 = d0;
        }

        float f = (float) (par4 >> 24 & 255) / 255.0F;
        float f1 = (float) (par4 >> 16 & 255) / 255.0F;
        float f2 = (float) (par4 >> 8 & 255) / 255.0F;
        float f3 = (float) (par4 & 255) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(7);
        GL11.glVertex3d(par0, par3, 0.0D);
        GL11.glVertex3d(par2, par3, 0.0D);
        GL11.glVertex3d(par2, par1, 0.0D);
        GL11.glVertex3d(par0, par1, 0.0D);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
}
