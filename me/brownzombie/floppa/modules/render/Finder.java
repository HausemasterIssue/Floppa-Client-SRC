package me.brownzombie.floppa.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class Finder extends Module {

    private Setting range = new Setting("Range", Integer.valueOf(50), Integer.valueOf(10), Integer.valueOf(100));
    private Setting emeraldOre = new Setting("Emerald Ore", Boolean.valueOf(false));
    private Setting portals = new Setting("Portals", Boolean.valueOf(false));
    public Color blockColor;

    public Finder() {
        super("Finder", "Renders ESP on chosen Blocks", Module.Category.RENDER);
    }

    public void onRender(float partialTicks) {
        if (Finder.mc.player != null && Finder.mc.world != null) {
            Iterator iterator = this.getSphere(Finder.mc.getRenderViewEntity().getPosition(), ((Integer) this.range.getValue()).floatValue(), (int) ((Integer) this.range.getValue()).floatValue(), false, true, 0).iterator();

            while (iterator.hasNext()) {
                BlockPos pos = (BlockPos) iterator.next();

                RenderUtil.drawBlockESP(pos, this.blockColor.getRGB(), 2.0F);
            }
        }

    }

    public List getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList circleblocks = new ArrayList();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();

        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x) {
            int z = cz - (int) r;

            while ((float) z <= (float) cz + r) {
                int y = sphere ? cy - (int) r : cy;

                while (true) {
                    float f = sphere ? (float) cy + r : (float) (cy + h);

                    if ((float) y >= f) {
                        ++z;
                        break;
                    }

                    double dist = (double) ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));

                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);

                        if (!l.equals(new BlockPos(Finder.mc.player.posX, Finder.mc.player.posY, Finder.mc.player.posZ))) {
                            if (this.getBlock(l) == Blocks.PORTAL && ((Boolean) this.portals.getValue()).booleanValue()) {
                                circleblocks.add(l);
                                this.blockColor = new Color(200, 0, 255, 150);
                            } else if (this.getBlock(l) == Blocks.EMERALD_ORE && ((Boolean) this.emeraldOre.getValue()).booleanValue()) {
                                circleblocks.add(l);
                                this.blockColor = new Color(17, 255, 0, 150);
                            }
                        }
                    }

                    ++y;
                }
            }
        }

        return circleblocks;
    }

    public Block getBlock(BlockPos pos) {
        IBlockState ibs = Finder.mc.world.getBlockState(pos);
        Block block = ibs.getBlock();

        return block;
    }
}
