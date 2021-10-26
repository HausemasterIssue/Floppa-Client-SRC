package me.brownzombie.floppa.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CombatUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static EnumFacing getCorrectEnumFacing(EntityPlayer target) {
        boolean ableToRunOnCurrentFacing = true;
        EnumFacing correctEnumFacing = null;
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing facing = aenumfacing[j];
            BlockPos posToCheck = target.getPosition().offset(facing).add(0, 1, 0);
            BlockPos posToCheck1 = target.getPosition().offset(facing).offset(facing).add(0, 1, 0);
            BlockPos posToCheck2 = target.getPosition().offset(facing).offset(facing).offset(facing).add(0, 1, 0);

            if (!CombatUtil.mc.world.getBlockState(posToCheck).getBlock().equals(Blocks.AIR) || !CombatUtil.mc.world.getBlockState(posToCheck1).getBlock().equals(Blocks.AIR) || !CombatUtil.mc.world.getBlockState(posToCheck2).getBlock().equals(Blocks.AIR)) {
                ableToRunOnCurrentFacing = false;
            }

            if (ableToRunOnCurrentFacing) {
                correctEnumFacing = facing;
            }
        }

        return correctEnumFacing;
    }

    public static boolean checkBlocksEmpty(EntityPlayer target, EnumFacing facing) {
        BlockPos posToCheck = target.getPosition().offset(facing).add(0, 1, 0);
        BlockPos posToCheck1 = target.getPosition().offset(facing).offset(facing).add(0, 1, 0);
        BlockPos posToCheck2 = target.getPosition().offset(facing).offset(facing).offset(facing).add(0, 1, 0);

        return !CombatUtil.mc.world.getBlockState(posToCheck).getBlock().equals(Blocks.AIR) || !CombatUtil.mc.world.getBlockState(posToCheck1).getBlock().equals(Blocks.AIR) || !CombatUtil.mc.world.getBlockState(posToCheck2).getBlock().equals(Blocks.AIR);
    }

    public static List getPlayersSorted(float range) {
        if (CombatUtil.mc.world != null && CombatUtil.mc.player != null) {
            List list = CombatUtil.mc.world.playerEntities;

            synchronized (CombatUtil.mc.world.playerEntities) {
                ArrayList playerList = new ArrayList();
                Iterator iterator = CombatUtil.mc.world.playerEntities.iterator();

                while (iterator.hasNext()) {
                    EntityPlayer player = (EntityPlayer) iterator.next();

                    if (CombatUtil.mc.player != player && CombatUtil.mc.player.getDistance(player) <= range) {
                        playerList.add(player);
                    }
                }

                playerList.sort(Comparator.comparing((eP) -> {
                    return Float.valueOf(CombatUtil.mc.player.getDistance(eP));
                }));
                return playerList;
            }
        } else {
            return new ArrayList();
        }
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0D;
        double difZ = to.z - from.z;
        double dist = (double) MathHelper.sqrt(difX * difX + difZ * difZ);

        return new float[] { (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static List getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList circleblocks = new ArrayList();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();

        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x) {
            for (int z = cz - (int) r; (float) z <= (float) cz + r; ++z) {
                for (int y = sphere ? cy - (int) r : cy; (float) y < (sphere ? (float) cy + r : (float) (cy + h)); ++y) {
                    double dist = (double) ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));

                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);

                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }
}
