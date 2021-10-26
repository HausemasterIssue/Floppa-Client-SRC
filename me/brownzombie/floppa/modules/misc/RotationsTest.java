package me.brownzombie.floppa.modules.misc;

import java.util.Iterator;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.event.events.PacketEvent;
import me.brownzombie.floppa.event.events.RenderModelEvent;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.EntityUtil;
import me.brownzombie.floppa.util.MathUtil;
import me.brownzombie.floppa.util.RotationUtil;
import me.brownzombie.floppa.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RotationsTest extends Module {

    public Setting packet = new Setting("Packet", Boolean.valueOf(false));
    public static RotationsTest INSTANCE;
    public static double yaw;
    public static double pitch;
    public static double renderPitch;
    public static boolean isSpoofingAngles;
    private final Timer attackTimer = new Timer();

    public RotationsTest() {
        super("RotationsTest", "Im testing rotations", Module.Category.MISC);
        RotationsTest.INSTANCE = this;
    }

    public void onEnable() {
        if (RotationsTest.mc.player != null && RotationsTest.mc.world != null) {
            this.attackTimer.reset();
        }

    }

    public void onUpdate() {
        if (RotationsTest.mc.player != null && RotationsTest.mc.world != null) {
            this.doAttack();
        }

    }

    public void doAttack() {
        if (RotationsTest.mc.player != null && RotationsTest.mc.world != null && this.attackTimer.passedMs(500L)) {
            EntityPlayer entity = this.getTarget();

            if (!entity.equals(RotationsTest.mc.player) && RotationsTest.mc.player.getDistanceSq(entity) <= 10.0D) {
                this.attack(this.getTarget());
                this.attackTimer.reset();
            }
        }

    }

    private void attack(EntityPlayer target) {
        if (((Boolean) this.packet.getValue()).booleanValue()) {
            this.lookAtEntity(target);
            RotationUtil.packetRotateToEntity(target);
        } else {
            RotationUtil rotationutil = Floppa.rotationUtil;

            RotationUtil.legitRotateToEntity(target);
        }

        RotationsTest.mc.playerController.attackEntity(RotationsTest.mc.player, target);
        RotationsTest.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double distance = 6.0D;
        double maxHealth = 36.0D;
        Iterator iterator = RotationsTest.mc.world.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entity = (EntityPlayer) iterator.next();

            if (target == null) {
                target = entity;
                distance = RotationsTest.mc.player.getDistanceSq(entity);
                maxHealth = (double) EntityUtil.getHealth(entity);
            } else {
                if (entity != null) {
                    target = entity;
                    break;
                }

                if (RotationsTest.mc.player.getDistanceSq(entity) < distance) {
                    target = entity;
                    distance = RotationsTest.mc.player.getDistanceSq(entity);
                    maxHealth = (double) EntityUtil.getHealth(entity);
                }

                if ((double) EntityUtil.getHealth(entity) < maxHealth) {
                    target = entity;
                    distance = RotationsTest.mc.player.getDistanceSq(entity);
                    maxHealth = (double) EntityUtil.getHealth(entity);
                }
            }
        }

        return target;
    }

    public void lookAtEntity(Entity entity) {
        float[] v = MathUtil.calcAngle(RotationsTest.mc.player.getPositionEyes(RotationsTest.mc.getRenderPartialTicks()), entity.getPositionEyes(RotationsTest.mc.getRenderPartialTicks()));
        float[] v2 = MathUtil.calcAngle(RotationsTest.mc.player.getPositionEyes(RotationsTest.mc.getRenderPartialTicks()), entity.getPositionVector().add(new Vec3d(0.0D, -0.5D, 0.0D)));

        this.setYawAndPitch(v[0], v[1], v2[1]);
        RotationsTest.isSpoofingAngles = true;
    }

    public void setYawAndPitch(float yaw1, float pitch1, float renderPitch1) {
        RotationsTest.yaw = (double) yaw1;
        RotationsTest.pitch = (double) pitch1;
        RotationsTest.renderPitch = (double) renderPitch1;
    }

    @SubscribeEvent
    public void renderModelRotation(RenderModelEvent event) {
        event.rotating = true;
        event.pitch = (float) RotationsTest.renderPitch;
        MessageManager.sendClientMessage("REnderModelEvent", true);
        System.out.println("RenderModelEventSysOut");
    }

    @SubscribeEvent
    public void updateRotation(RenderWorldLastEvent event) {
        RotationsTest.mc.player.rotationYawHead = (float) RotationsTest.yaw;
        RotationsTest.mc.player.renderYawOffset = (float) RotationsTest.yaw;
        MessageManager.sendClientMessage("RenderWorldLastEvent", true);
        System.out.println("RenderWorldLastEventSysOut");
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent event) {
        if (RotationsTest.mc.player != null) {
            Packet packet = event.getPacket();

            if (packet instanceof CPacketPlayer) {
                if (RotationsTest.isSpoofingAngles) {
                    ((CPacketPlayer) packet).yaw = (float) RotationsTest.yaw;
                    ((CPacketPlayer) packet).pitch = (float) RotationsTest.pitch;
                    RotationsTest.isSpoofingAngles = false;
                    MessageManager.sendClientMessage("PacketSendEvent", true);
                    System.out.println("PacketSendEventSysOut");
                }

                MessageManager.sendClientMessage("PacketSendEvent", true);
                System.out.println("PacketSendEventSysOut");
            }

        }
    }
}
