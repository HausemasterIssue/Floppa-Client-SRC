package me.brownzombie.floppa.modules.render;

import java.awt.Color;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import org.lwjgl.opengl.GL11;

public class BlockHighlight extends Module {

    public Setting red = new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255));
    public Setting green = new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255));
    public Setting blue = new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255));
    public Setting alpha = new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    public Setting linewidth = new Setting("LineWidth", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(10));
    public static BlockHighlight INSTANCE;

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block you\'re looking at", Module.Category.RENDER);
        BlockHighlight.INSTANCE = this;
    }

    public void onRender(float partialTicks) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;

        if (ray != null && ray.typeOfHit == Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth((float) ((Integer) this.linewidth.getValue()).intValue());
            AxisAlignedBB bb = new AxisAlignedBB((double) blockpos.getX() - BlockHighlight.mc.getRenderManager().viewerPosX, (double) blockpos.getY() - BlockHighlight.mc.getRenderManager().viewerPosY, (double) blockpos.getZ() - BlockHighlight.mc.getRenderManager().viewerPosZ, (double) (blockpos.getX() + 1) - BlockHighlight.mc.getRenderManager().viewerPosX, (double) (blockpos.getY() + 1) - BlockHighlight.mc.getRenderManager().viewerPosY, (double) (blockpos.getZ() + 1) - BlockHighlight.mc.getRenderManager().viewerPosZ);

            RenderUtil.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.alpha.getValue()).intValue())).getRGB());
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }
}
