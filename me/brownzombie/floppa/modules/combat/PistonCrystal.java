package me.brownzombie.floppa.modules.combat;

import java.util.List;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.BlockUtil;
import me.brownzombie.floppa.util.CombatUtil;
import me.brownzombie.floppa.util.InventoryUtil;
import me.brownzombie.floppa.util.Timer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class PistonCrystal extends Module {

    private Setting placeRange = new Setting("PlaceRange", Float.valueOf(4.5F), Float.valueOf(0.0F), Float.valueOf(10.0F));
    private Setting offhand = new Setting("Offhand", Boolean.valueOf(false));
    private Setting rotate = new Setting("Rotate", Boolean.valueOf(false));
    private Setting switchBack = new Setting("SwitchBack", Boolean.valueOf(false));
    private Setting pistonDelay = new Setting("PistonDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100));
    private Setting crystalDelay = new Setting("CrystalDelay", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(100));
    private Setting powerDelay = new Setting("PowerDelay", Integer.valueOf(4), Integer.valueOf(0), Integer.valueOf(100));
    private Setting breakDelay = new Setting("BreakDelay", Integer.valueOf(6), Integer.valueOf(0), Integer.valueOf(100));
    private Timer timer = new Timer();
    private EntityPlayer target;
    private EnumFacing offsetEnumFacing;

    public PistonCrystal() {
        super("PistonCrystal", "", Module.Category.COMBAT);
    }

    public void onEnable() {
        this.target = null;
        this.timer.reset();
    }

    public void onUpdate() {
        if (this.getTarget()) {
            this.offsetEnumFacing = CombatUtil.getCorrectEnumFacing(this.target);
            if (CombatUtil.checkBlocksEmpty(this.target, this.offsetEnumFacing)) {
                this.placeBlocks();
            }

        }
    }

    private void placeBlocks() {
        this.placeBlock(this.target.getPosition().offset(this.offsetEnumFacing).offset(this.offsetEnumFacing).up(), 0, ((Boolean) this.switchBack.value).booleanValue());
        this.placeBlock(this.target.getPosition().up(), 1, ((Boolean) this.switchBack.value).booleanValue());
        this.placeBlock(this.target.getPosition().offset(this.offsetEnumFacing).offset(this.offsetEnumFacing).offset(this.offsetEnumFacing).up(), 2, ((Boolean) this.switchBack.value).booleanValue());
        this.breakCrystal(this.target.getPosition().offset(this.offsetEnumFacing).up());
    }

    private void breakCrystal(BlockPos crystalPosition) {
        if (PistonCrystal.mc.world.getBlockState(crystalPosition).getBlock().equals(BlockPistonExtension.class)) {
            BlockPos crystalHittable = crystalPosition.offset(this.offsetEnumFacing.getOpposite());
            double x = (double) crystalHittable.getX();
            double y = (double) crystalHittable.getY() + 1.0D;
            double z = (double) crystalHittable.getZ();
            AxisAlignedBB scanArea = new AxisAlignedBB(x, y, z, x, y + 0.1D, z);
            List crystals = PistonCrystal.mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, scanArea);

            if (!crystals.isEmpty()) {
                CPacketUseEntity attackPacket = new CPacketUseEntity();

                attackPacket.entityId = ((EntityEnderCrystal) crystals.get(0)).getEntityId();
                attackPacket.action = Action.ATTACK;
                PistonCrystal.mc.player.connection.sendPacket(attackPacket);
                if (this.timer.passedMs((long) ((Integer) this.breakDelay.value).intValue())) {
                    PistonCrystal.mc.player.connection.sendPacket(new CPacketUseEntity());
                }
            }
        }

    }

    private void placeBlock(BlockPos pos, int mode, boolean switchBack) {
        int currentSlot = PistonCrystal.mc.player.inventory.currentItem;
        Class block;

        switch (mode) {
        case 0:
            block = BlockPistonBase.class;
            if (InventoryUtil.findSlotHotbar(block) == -1) {
                MessageManager.sendClientMessage("Find " + block.getName() + " retard.", true);
                return;
            }

            InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(block));
            if (this.timer.passedMs((long) ((Integer) this.pistonDelay.value).intValue())) {
                BlockUtil.placeBlock(pos, ((Boolean) this.offhand.value).booleanValue(), ((Boolean) this.rotate.value).booleanValue());
            }
            break;

        case 1:
            block = ItemEndCrystal.class;
            if (InventoryUtil.findSlotHotbar(block) == -1) {
                MessageManager.sendClientMessage("Find " + block.getName() + " retard.", true);
                return;
            }

            InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(block));
            if (this.timer.passedMs((long) ((Integer) this.crystalDelay.value).intValue())) {
                BlockUtil.placeBlock(pos, ((Boolean) this.offhand.value).booleanValue(), ((Boolean) this.rotate.value).booleanValue());
            }
            break;

        case 2:
            block = BlockRedstoneTorch.class;
            if (InventoryUtil.findSlotHotbar(block) == -1) {
                MessageManager.sendClientMessage("Find " + block.getName() + " retard.", true);
                return;
            }

            InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(block));
            if (this.timer.passedMs((long) ((Integer) this.powerDelay.value).intValue())) {
                BlockUtil.placeBlock(pos, ((Boolean) this.offhand.value).booleanValue(), ((Boolean) this.rotate.value).booleanValue());
            }
        }

        if (switchBack) {
            InventoryUtil.switchToSlot(currentSlot);
        }

    }

    private boolean getTarget() {
        List playerList = CombatUtil.getPlayersSorted(((Float) this.placeRange.value).floatValue());

        if (playerList.isEmpty()) {
            return false;
        } else {
            this.target = (EntityPlayer) playerList.get(0);
            return true;
        }
    }
}
