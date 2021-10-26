package me.brownzombie.floppa.util;

import net.minecraft.client.Minecraft;

public class PositionUtil {

    private static double x;
    private static double y;
    private static double z;
    private static boolean onground;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void updatePosition() {
        PositionUtil.x = PositionUtil.mc.player.posX;
        PositionUtil.y = PositionUtil.mc.player.posY;
        PositionUtil.z = PositionUtil.mc.player.posZ;
        PositionUtil.onground = PositionUtil.mc.player.onGround;
    }

    public static void restorePosition() {
        PositionUtil.mc.player.posX = PositionUtil.x;
        PositionUtil.mc.player.posY = PositionUtil.y;
        PositionUtil.mc.player.posZ = PositionUtil.z;
        PositionUtil.mc.player.onGround = PositionUtil.onground;
    }
}
