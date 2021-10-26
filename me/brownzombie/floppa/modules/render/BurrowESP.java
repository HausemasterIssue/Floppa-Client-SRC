package me.brownzombie.floppa.modules.render;

import java.awt.Color;
import java.util.Iterator;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BurrowESP extends Module {

    public Setting burrowEspRange = new Setting("Range", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(20));
    public Setting renderOwnBurrow = new Setting("RenderOwn", Boolean.valueOf(false));

    public BurrowESP() {
        super("BurrowESP", "Shows when someone is burrowed", Module.Category.RENDER);
    }

    public void onRender(float partialTicks) {
        Iterator iterator = BurrowESP.mc.world.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer) iterator.next();

            if (player.getDistance(BurrowESP.mc.player) < (float) ((Integer) this.burrowEspRange.getValue()).intValue() && BurrowESP.mc.world != null && BurrowESP.mc.player != null && !player.isSneaking() && (BurrowESP.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ)).getBlock() == Blocks.OBSIDIAN || BurrowESP.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ)).getBlock() == Blocks.BEDROCK) && (player != BurrowESP.mc.player || ((Boolean) this.renderOwnBurrow.getValue()).booleanValue())) {
                AxisAlignedBB bb = new AxisAlignedBB(player.posX - BurrowESP.mc.getRenderManager().viewerPosX, player.posY - BurrowESP.mc.getRenderManager().viewerPosY, player.posZ - BurrowESP.mc.getRenderManager().viewerPosZ, player.posX + 0.5D - BurrowESP.mc.getRenderManager().viewerPosX, player.posY + 0.5D - BurrowESP.mc.getRenderManager().viewerPosY, player.posZ + 0.5D - BurrowESP.mc.getRenderManager().viewerPosZ);

                RenderUtil.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (new Color(0, 0, 255, 150)).getRGB());
                RenderUtil.drawBox(bb, (new Color(0, 0, 255, 75)).getRGB());
            }
        }

    }
}
