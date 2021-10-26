package me.brownzombie.floppa.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;

public class RotationUtil {

    public static final Minecraft mc = Minecraft.getMinecraft();
    private float yaw;
    private float pitch;

    public void updateRotations() {
        this.yaw = RotationUtil.mc.player.rotationYaw;
        this.pitch = RotationUtil.mc.player.rotationPitch;
    }

    public void restoreRotations() {
        RotationUtil.mc.player.rotationYaw = this.yaw;
        RotationUtil.mc.player.rotationYawHead = this.yaw;
        RotationUtil.mc.player.rotationPitch = this.pitch;
    }

    public static void legitRotate(float yaw, float pitch) {
        RotationUtil.mc.player.rotationYaw = yaw;
        RotationUtil.mc.player.rotationYawHead = yaw;
        RotationUtil.mc.player.rotationPitch = pitch;
    }

    public static void packetRotate(float yaw, float pitch) {
        RotationUtil.mc.player.connection.sendPacket(new Rotation(yaw, pitch, RotationUtil.mc.player.onGround));
    }

    public static void legitRotateToEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(RotationUtil.mc.player.getPositionEyes(RotationUtil.mc.getRenderPartialTicks()), entity.getPositionEyes(RotationUtil.mc.getRenderPartialTicks()));

        legitRotate(angle[0], angle[1]);
    }

    public static void packetRotateToEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(RotationUtil.mc.player.getPositionEyes(RotationUtil.mc.getRenderPartialTicks()), entity.getPositionEyes(RotationUtil.mc.getRenderPartialTicks()));

        packetRotate(angle[0], angle[1]);
    }
}
