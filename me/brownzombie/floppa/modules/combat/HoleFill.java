package me.brownzombie.floppa.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.BlockUtil;
import me.brownzombie.floppa.util.InventoryUtil;
import me.brownzombie.floppa.util.RenderUtil;
import me.brownzombie.floppa.util.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleFill extends Module {

    public Setting range = new Setting("Range", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(10));
    public Setting placeDelay = new Setting("Delay", Float.valueOf(4.0F), Float.valueOf(0.0F), Float.valueOf(300.0F));
    public Setting blockTypeSetting;
    public Setting rotate;
    public Setting switchBack;
    public Setting toggleAfter;
    public Setting finishedMessage;
    private final Timer placeTimer;
    public Class beforeSlot;

    public HoleFill() {
        super("HoleFill", "Fills holes... what else would it do nigga", Module.Category.COMBAT);
        this.blockTypeSetting = new Setting("BlockType", HoleFill.BlockType.OBSIDIAN);
        this.rotate = new Setting("Rotate", Boolean.valueOf(false));
        this.switchBack = new Setting("SwitchBack", Boolean.valueOf(false));
        this.toggleAfter = new Setting("ToggleAfterFill", Boolean.valueOf(false));
        this.finishedMessage = new Setting("FinishedMessage", Boolean.valueOf(false));
        this.placeTimer = new Timer();
    }

    public void onEnable() {
        if (HoleFill.mc.player != null && HoleFill.mc.world != null) {
            this.beforeSlot = HoleFill.mc.player.getHeldItemMainhand().getItem().getClass();
            this.placeTimer.reset();
        }

    }

    public void onUpdate() {
        if (HoleFill.mc.player != null && HoleFill.mc.world != null) {
            if (InventoryUtil.findSlotHotbar(BlockObsidian.class) == -1 && ((HoleFill.BlockType) this.blockTypeSetting.getValue()).equals(HoleFill.BlockType.OBSIDIAN)) {
                this.setToggled(false);
                MessageManager.sendClientMessage("Get Obsidian Retard.", false);
                return;
            }

            if (InventoryUtil.findSlotHotbar(BlockEnderChest.class) == -1 && ((HoleFill.BlockType) this.blockTypeSetting.getValue()).equals(HoleFill.BlockType.ECHEST)) {
                this.setToggled(false);
                MessageManager.sendClientMessage("Get EChests Retard.", false);
                return;
            }

            Iterator iterator = this.getSphere(HoleFill.mc.getRenderViewEntity().getPosition(), ((Integer) this.range.getValue()).floatValue(), (int) ((Integer) this.range.getValue()).floatValue(), false, true, 0).iterator();

            while (iterator.hasNext()) {
                BlockPos hole = (BlockPos) iterator.next();

                if (hole != null) {
                    Iterator iterator1 = HoleFill.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(hole)).iterator();

                    while (iterator1.hasNext()) {
                        Entity entity = (Entity) iterator1.next();

                        if (entity instanceof EntityLivingBase) {
                            return;
                        }
                    }

                    if (this.placeTimer.passedMs(((Float) this.placeDelay.getValue()).longValue())) {
                        BlockUtil.placeBlock(hole, false, ((Boolean) this.rotate.getValue()).booleanValue(), true, false, InventoryUtil.findSlotHotbar(((HoleFill.BlockType) this.blockTypeSetting.getValue()).equals(HoleFill.BlockType.OBSIDIAN) ? BlockObsidian.class : BlockEnderChest.class));
                        this.placeTimer.reset();
                    }
                }

                if (this.getSphere(HoleFill.mc.getRenderViewEntity().getPosition(), ((Integer) this.range.getValue()).floatValue(), (int) ((Integer) this.range.getValue()).floatValue(), false, true, 0).isEmpty()) {
                    if (((Boolean) this.switchBack.getValue()).booleanValue()) {
                        InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(this.beforeSlot));
                    }

                    if (((Boolean) this.toggleAfter.getValue()).booleanValue()) {
                        this.setToggled(false);
                        if (((Boolean) this.finishedMessage.getValue()).booleanValue()) {
                            MessageManager.sendClientMessage(ChatFormatting.DARK_AQUA + "Finished Filling Holes.", false);
                        }
                    }
                }
            }
        }

    }

    public void onRender(float partialTicks) {
        Iterator iterator = this.getSphere(HoleFill.mc.getRenderViewEntity().getPosition(), ((Integer) this.range.getValue()).floatValue(), (int) ((Integer) this.range.getValue()).floatValue(), false, true, 0).iterator();

        label30:
        while (iterator.hasNext()) {
            BlockPos hole = (BlockPos) iterator.next();
            Iterator bb = HoleFill.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(hole)).iterator();

            Entity entity;

            do {
                if (!bb.hasNext()) {
                    AxisAlignedBB bb1 = new AxisAlignedBB((double) hole.getX() - HoleFill.mc.getRenderManager().viewerPosX, (double) hole.getY() - HoleFill.mc.getRenderManager().viewerPosY, (double) hole.getZ() - HoleFill.mc.getRenderManager().viewerPosZ, (double) (hole.getX() + 1) - HoleFill.mc.getRenderManager().viewerPosX, (double) (hole.getY() + 1) - HoleFill.mc.getRenderManager().viewerPosY, (double) (hole.getZ() + 1) - HoleFill.mc.getRenderManager().viewerPosZ);

                    if (HoleFill.mc.world.getBlockState(hole).getBlock().equals(Blocks.AIR) && HoleFill.mc.world.getBlockState(hole.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && HoleFill.mc.world.getBlockState(hole.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                        RenderUtil.drawBlockESP(bb1, (new Color(255, 255, 255, 255)).getRGB(), 2.0F);
                    }
                    continue label30;
                }

                entity = (Entity) bb.next();
            } while (!(entity instanceof EntityLivingBase));

            return;
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

                        if (!l.equals(new BlockPos(HoleFill.mc.player.posX, HoleFill.mc.player.posY, HoleFill.mc.player.posZ)) && (this.isBedrock(l) || this.isObby(l))) {
                            circleblocks.add(l);
                            Iterator iterator = HoleFill.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(l)).iterator();

                            while (iterator.hasNext()) {
                                Entity entity = (Entity) iterator.next();

                                if (entity instanceof EntityLivingBase) {
                                    circleblocks.remove(l);
                                }
                            }
                        }
                    }

                    ++y;
                }
            }
        }

        return circleblocks;
    }

    private boolean obbyOrEchest(BlockPos blockPos) {
        return HoleFill.mc.world == null ? false : HoleFill.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || HoleFill.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST;
    }

    public boolean isBedrock(BlockPos blockPos) {
        boolean air = HoleFill.mc.world.getBlockState(blockPos).getBlock() instanceof BlockAir && HoleFill.mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockAir && HoleFill.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() instanceof BlockAir;
        boolean down = HoleFill.mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.BEDROCK;
        boolean north = HoleFill.mc.world.getBlockState(blockPos.north()).getBlock() == Blocks.BEDROCK;
        boolean south = HoleFill.mc.world.getBlockState(blockPos.south()).getBlock() == Blocks.BEDROCK;
        boolean west = HoleFill.mc.world.getBlockState(blockPos.west()).getBlock() == Blocks.BEDROCK;
        boolean east = HoleFill.mc.world.getBlockState(blockPos.east()).getBlock() == Blocks.BEDROCK;

        return air && down && north && south && west && east;
    }

    public boolean isObby(BlockPos blockPos) {
        boolean air = HoleFill.mc.world.getBlockState(blockPos).getBlock() instanceof BlockAir && HoleFill.mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockAir && HoleFill.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() instanceof BlockAir;
        boolean down = HoleFill.mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.BEDROCK || this.obbyOrEchest(blockPos.down());
        boolean north = HoleFill.mc.world.getBlockState(blockPos.north()).getBlock() == Blocks.BEDROCK || this.obbyOrEchest(blockPos.north());
        boolean south = HoleFill.mc.world.getBlockState(blockPos.south()).getBlock() == Blocks.BEDROCK || this.obbyOrEchest(blockPos.south());
        boolean west = HoleFill.mc.world.getBlockState(blockPos.west()).getBlock() == Blocks.BEDROCK || this.obbyOrEchest(blockPos.west());
        boolean east = HoleFill.mc.world.getBlockState(blockPos.east()).getBlock() == Blocks.BEDROCK || this.obbyOrEchest(blockPos.east());

        return air && down && north && south && west && east;
    }

    public static enum BlockType {

        OBSIDIAN, ECHEST;
    }
}
