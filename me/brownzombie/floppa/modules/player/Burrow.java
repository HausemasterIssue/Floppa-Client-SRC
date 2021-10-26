package me.brownzombie.floppa.modules.player;

import java.lang.reflect.Field;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.BlockUtil;
import me.brownzombie.floppa.util.InventoryUtil;
import me.brownzombie.floppa.util.Mappings;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;

public class Burrow extends Module {

    private BlockPos playerPos;
    private int currentSlot;
    private Setting silentSwitch = new Setting("SilentSwitch", Boolean.valueOf(false));
    private Setting rotate = new Setting("Rotate", Boolean.valueOf(false));
    private Setting switchBack = new Setting("SwitchBack", Boolean.valueOf(false));
    private Setting offset = new Setting("Offset", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(5));
    private Setting blockType;
    private Setting placeMode;

    public Burrow() {
        super("Burrow", "Places blocks inside you (No homo)", Module.Category.PLAYER);
        this.blockType = new Setting("BlockType", Burrow.Mode.OBBY);
        this.placeMode = new Setting("PlaceMode", Burrow.PlaceMode.VIRTUE);
    }

    public void onEnable() {
        this.currentSlot = Burrow.mc.player.inventory.currentItem;
        this.playerPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        if (Burrow.mc.world.getBlockState(this.playerPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && Burrow.mc.player.onGround) {
            if (!Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.OBSIDIAN) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.ENDER_CHEST) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.REDSTONE_WIRE) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.SOUL_SAND) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.ENCHANTING_TABLE) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.SKULL) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.RAIL) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.DETECTOR_RAIL) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.ACTIVATOR_RAIL) && !Burrow.mc.world.getBlockState(this.playerPos).getBlock().equals(Blocks.GOLDEN_RAIL)) {
                if ((InventoryUtil.findSlotHotbar(BlockObsidian.class) != -1 || this.blockType.getValue() != Burrow.Mode.OBBY) && (InventoryUtil.findSlotHotbar(BlockEnderChest.class) != -1 || this.blockType.getValue() != Burrow.Mode.ECHEST) && (InventoryUtil.findSlotHotbar(BlockSoulSand.class) != -1 || this.blockType.getValue() != Burrow.Mode.SOULSAND) && (InventoryUtil.findSlotHotbar(BlockEnchantmentTable.class) != -1 || this.blockType.getValue() != Burrow.Mode.ENCHANTMENTTABLE)) {
                    if (!((Burrow.PlaceMode) this.placeMode.value).equals(Burrow.PlaceMode.PACKET)) {
                        Burrow.mc.player.motionY = 0.405D;
                    }

                } else {
                    this.setToggled(false);
                }
            } else {
                this.setToggled(false);
            }
        } else {
            this.setToggled(false);
        }
    }

    public void onDisable() {
        this.setTimer(1.0F);
    }

    public void onUpdate() {
        if (Burrow.mc.player != null) {
            if (InventoryUtil.findSlotHotbar(this.getBlockFromEnum()) == -1) {
                this.setToggled(false);
                MessageManager.sendClientMessage("Get " + this.getBlockFromEnum().toString() + "s Retard.", false);
            } else {
                InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(this.getBlockFromEnum()));
                if (!((Burrow.PlaceMode) this.placeMode.getValue()).equals(Burrow.PlaceMode.PACKET)) {
                    if (((Burrow.PlaceMode) this.placeMode.value).equals(Burrow.PlaceMode.FAST)) {
                        this.setTimer(50.0F);
                    }

                    if (Burrow.mc.player.posY > (double) this.playerPos.getY() + 1.04D) {
                        MessageManager.sendClientMessage("El Borrow", false);
                        this.doBurrow(this.getBlockFromEnum());
                        Burrow.mc.player.motionY = 0.405D;
                        this.toggle();
                    }
                } else {
                    float[] currentYawPitch = new float[] { Burrow.mc.player.cameraYaw, Burrow.mc.player.cameraPitch};

                    Burrow.mc.player.connection.sendPacket(new Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41999998688698D, Burrow.mc.player.posZ, true));
                    Burrow.mc.player.connection.sendPacket(new Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805211997D, Burrow.mc.player.posZ, true));
                    Burrow.mc.player.connection.sendPacket(new Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.00133597911214D, Burrow.mc.player.posZ, true));
                    Burrow.mc.player.connection.sendPacket(new Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.16610926093821D, Burrow.mc.player.posZ, true));
                    BlockUtil.placeBlock(this.playerPos, EnumHand.MAIN_HAND, ((Boolean) this.rotate.getValue()).booleanValue(), true, false);
                    Burrow.mc.player.connection.sendPacket(new Position(Burrow.mc.player.posX, Burrow.mc.player.posY + (double) ((Integer) this.offset.getValue()).intValue(), Burrow.mc.player.posZ, false));
                    Burrow.mc.player.connection.sendPacket(new Rotation(currentYawPitch[0], currentYawPitch[1], true));
                    Burrow.mc.player.connection.sendPacket(new CPacketEntityAction(Burrow.mc.player, Action.STOP_SNEAKING));
                    if (((Boolean) this.switchBack.value).booleanValue()) {
                        Burrow.mc.player.inventory.currentItem = this.currentSlot;
                    }

                    this.toggle();
                }

            }
        }
    }

    private void doBurrow(Class block) {
        BlockUtil.placeBlock(this.playerPos, InventoryUtil.findSlotHotbar(block));
        this.toggle();
    }

    private void setTimer(float value) {
        try {
            Field e = Minecraft.class.getDeclaredField(Mappings.timer);

            e.setAccessible(true);
            Field tickLength = Timer.class.getDeclaredField(Mappings.tickLength);

            tickLength.setAccessible(true);
            tickLength.setFloat(e.get(Burrow.mc), 50.0F / value);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public Class getBlockFromEnum() {
        Class type = BlockObsidian.class;

        switch ((Burrow.Mode) this.blockType.getValue()) {
        case OBBY:
            type = BlockObsidian.class;
            break;

        case ECHEST:
            type = BlockEnderChest.class;
            break;

        case SOULSAND:
            type = BlockSoulSand.class;
            break;

        case ENCHANTMENTTABLE:
            type = BlockEnchantmentTable.class;
        }

        return type;
    }

    static enum PlaceMode {

        PACKET, VIRTUE, FAST;
    }

    static enum Mode {

        SOULSAND, ECHEST, OBBY, ENCHANTMENTTABLE;
    }
}
