package me.brownzombie.floppa.modules.combat;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import me.brownzombie.floppa.event.events.PacketEvent;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.modules.misc.PacketMine;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.BlockUtil;
import me.brownzombie.floppa.util.InventoryUtil;
import me.brownzombie.floppa.util.MathUtil;
import me.brownzombie.floppa.util.RenderUtil;
import me.brownzombie.floppa.util.Timer;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class AutoCrystalBomb extends Module {

    private Setting rotate = new Setting("Rotate", Boolean.valueOf(false));
    private Setting silentSwitch = new Setting("SilentSwitch", Boolean.valueOf(false));
    private Setting packetBreak = new Setting("PacketBreak", Boolean.valueOf(true));
    private Setting toggleModule = new Setting("ToggleMineModule", Boolean.valueOf(true));
    private Setting fastChase = new Setting("ChaseMode", Boolean.valueOf(true));
    private Setting render = new Setting("Render", Boolean.valueOf(true));
    private Setting breakDelay = new Setting("BreakDelay", Float.valueOf(4.0F), Float.valueOf(0.0F), Float.valueOf(300.0F));
    private Setting mineModeSetting;
    private final Timer breakTimer;
    private BlockPos startBlockPos;
    private int slot;
    private float yaw;
    private float pitch;
    private boolean rotating;
    @EventHandler
    public Listener packetSendListener;

    public AutoCrystalBomb() {
        super("AutoCrystalBomb", "Automatically Crystal Bombs the Enemy.", Module.Category.COMBAT);
        this.mineModeSetting = new Setting("MineMode", AutoCrystalBomb.MineMode.INSTANT, test<invokedynamic>(this));
        this.breakTimer = new Timer();
        this.yaw = 0.0F;
        this.pitch = 0.0F;
        this.rotating = false;
        this.packetSendListener = new Listener(invoke<invokedynamic>(this), new Predicate[0]);
    }

    public void onEnable() {
        if (AutoCrystalBomb.mc.player != null && AutoCrystalBomb.mc.world != null) {
            this.breakTimer.reset();
            if (((Boolean) this.toggleModule.getValue()).booleanValue()) {
                if (this.mineModeSetting.getValue() == AutoCrystalBomb.MineMode.INSTANT) {
                    InstaMine.INSTANCE.setToggled(true);
                } else {
                    PacketMine.INSTANCE.setToggled(true);
                }
            }

            if (InventoryUtil.findSlotHotbar(ItemEndCrystal.class) == -1 && AutoCrystalBomb.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                MessageManager.sendClientMessage("Get Crystals Retard.", false);
                return;
            }

            RayTraceResult ray = AutoCrystalBomb.mc.objectMouseOver;

            if (ray != null && ray.typeOfHit == Type.BLOCK) {
                if (this.mineModeSetting.getValue() == AutoCrystalBomb.MineMode.INSTANT) {
                    AutoCrystalBomb.mc.playerController.clickBlock(ray.getBlockPos(), EnumFacing.UP);
                } else {
                    AutoCrystalBomb.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, ray.getBlockPos(), EnumFacing.UP));
                    AutoCrystalBomb.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, ray.getBlockPos(), EnumFacing.UP));
                }

                if (AutoCrystalBomb.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                    this.slot = InventoryUtil.findSlotHotbar(ItemEndCrystal.class);
                    InventoryUtil.switchToSlot(this.slot);
                }

                AutoCrystalBomb.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(ray.getBlockPos(), EnumFacing.UP, AutoCrystalBomb.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                MessageManager.sendClientMessage("Placing Crystal", false);
                this.startBlockPos = ray.getBlockPos();
            }
        }

    }

    public void onDisable() {
        if (AutoCrystalBomb.mc.player != null && AutoCrystalBomb.mc.world != null && ((Boolean) this.toggleModule.getValue()).booleanValue()) {
            if (this.mineModeSetting.getValue() == AutoCrystalBomb.MineMode.INSTANT) {
                InstaMine.INSTANCE.setToggled(false);
            } else {
                PacketMine.INSTANCE.setToggled(false);
            }
        }

    }

    public void onUpdate() {
        if (AutoCrystalBomb.mc.player != null && AutoCrystalBomb.mc.world != null && this.startBlockPos != null) {
            if (!AutoCrystalBomb.mc.player.onGround && ((Boolean) this.fastChase.getValue()).booleanValue()) {
                this.toggle();
            }

            if (InventoryUtil.findSlotHotbar(BlockObsidian.class) == -1) {
                MessageManager.sendClientMessage("Get Obsidian Retard.", false);
                return;
            }

            if (InventoryUtil.findSlotHotbar(ItemPickaxe.class) == -1) {
                MessageManager.sendClientMessage("Get a Pickaxe Retard.", false);
                return;
            }

            if (InventoryUtil.findSlotHotbar(ItemEndCrystal.class) == -1 && AutoCrystalBomb.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                MessageManager.sendClientMessage("Get Crystals Retard.", false);
                return;
            }

            double x = (double) this.startBlockPos.getX();
            double y = (double) this.startBlockPos.getY() + 1.0D;
            double z = (double) this.startBlockPos.getZ();
            AxisAlignedBB scanArea = new AxisAlignedBB(x, y, z, x, y + 0.1D, z);
            List crystals = AutoCrystalBomb.mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, scanArea);
            Iterator iterator = crystals.iterator();

            while (iterator.hasNext()) {
                EntityEnderCrystal crystal = (EntityEnderCrystal) iterator.next();

                if (crystal != null && this.breakTimer.passedMs(((Float) this.breakDelay.getValue()).longValue())) {
                    if (AutoCrystalBomb.mc.world.getBlockState(this.startBlockPos).getBlock() instanceof BlockAir) {
                        this.breakTimer.reset();
                        this.rotateTo(crystal);
                        if (((Boolean) this.packetBreak.getValue()).booleanValue()) {
                            CPacketUseEntity attackPacket = new CPacketUseEntity();

                            attackPacket.entityId = crystal.entityId;
                            attackPacket.action = net.minecraft.network.play.client.CPacketUseEntity.Action.ATTACK;
                            AutoCrystalBomb.mc.player.connection.sendPacket(attackPacket);
                        } else {
                            AutoCrystalBomb.mc.playerController.attackEntity(AutoCrystalBomb.mc.player, crystal);
                        }

                        AutoCrystalBomb.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                    } else {
                        InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(ItemPickaxe.class));
                        if (this.mineModeSetting.getValue() == AutoCrystalBomb.MineMode.PACKET) {
                            AutoCrystalBomb.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, this.startBlockPos, EnumFacing.UP));
                            AutoCrystalBomb.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.startBlockPos, EnumFacing.UP));
                        }
                    }
                }
            }

            if (AutoCrystalBomb.mc.world.getBlockState(this.startBlockPos).getBlock() instanceof BlockAir && crystals.isEmpty()) {
                BlockUtil.placeBlock(this.startBlockPos, false, ((Boolean) this.rotate.getValue()).booleanValue(), true, ((Boolean) this.silentSwitch.getValue()).booleanValue(), InventoryUtil.findSlotHotbar(BlockObsidian.class));
                if (AutoCrystalBomb.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                    this.slot = InventoryUtil.findSlotHotbar(ItemEndCrystal.class);
                    InventoryUtil.switchToSlot(this.slot);
                }

                AutoCrystalBomb.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.startBlockPos, EnumFacing.UP, AutoCrystalBomb.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
            }
        }

    }

    public void onRender(float partialTicks) {
        if (this.startBlockPos != null) {
            AxisAlignedBB bb = new AxisAlignedBB((double) this.startBlockPos.getX() - AutoCrystalBomb.mc.getRenderManager().viewerPosX, (double) this.startBlockPos.getY() - AutoCrystalBomb.mc.getRenderManager().viewerPosY, (double) this.startBlockPos.getZ() - AutoCrystalBomb.mc.getRenderManager().viewerPosZ, (double) (this.startBlockPos.getX() + 1) - AutoCrystalBomb.mc.getRenderManager().viewerPosX, (double) (this.startBlockPos.getY() + 1) - AutoCrystalBomb.mc.getRenderManager().viewerPosY, (double) (this.startBlockPos.getZ() + 1) - AutoCrystalBomb.mc.getRenderManager().viewerPosZ);

            if (((Boolean) this.render.getValue()).booleanValue() && !(AutoCrystalBomb.mc.world.getBlockState(this.startBlockPos).getBlock() instanceof BlockAir)) {
                RenderUtil.drawBlockESP(bb, (new Color(252, 196, 26, 255)).getRGB(), 2.0F);
            }
        }

    }

    private void rotateTo(Entity entity) {
        if (((Boolean) this.rotate.getValue()).booleanValue()) {
            float[] angle = MathUtil.calcAngle(AutoCrystalBomb.mc.player.getPositionEyes(AutoCrystalBomb.mc.getRenderPartialTicks()), entity.getPositionVector());

            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }

    }

    private void lambda$new$1(PacketEvent.Send event) {
        if (event.getStage() == 0 && ((Boolean) this.rotate.getValue()).booleanValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();

            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            this.rotating = false;
        }

    }

    private boolean lambda$new$0(AutoCrystalBomb.MineMode v) {
        return ((Boolean) this.toggleModule.getValue()).booleanValue();
    }

    static enum MineMode {

        INSTANT, PACKET;
    }
}
