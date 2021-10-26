package me.brownzombie.floppa.modules.player;

import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.BlockUtil;
import me.brownzombie.floppa.util.InventoryUtil;
import net.minecraft.block.BlockWeb;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class SelfWeb extends Module {

    public Setting switchBackSetting = new Setting("SwitchBack", Boolean.valueOf(true));
    public Setting rotateSetting = new Setting("Rotate", Boolean.valueOf(false));
    private BlockPos playerPos;
    private int beforeSlot;

    public SelfWeb() {
        super("SelfWeb", "Places webs on yourself", Module.Category.PLAYER);
    }

    public void onUpdate() {
        if (SelfWeb.mc.player != null) {
            if (InventoryUtil.findSlotHotbar(BlockWeb.class) == -1) {
                this.setToggled(false);
                MessageManager.sendClientMessage("Get Webs Retard.", false);
                return;
            }

            this.beforeSlot = InventoryUtil.findSlotHotbar(SelfWeb.mc.player.getHeldItemMainhand().getItem().getClass());
            this.playerPos = new BlockPos(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY, SelfWeb.mc.player.posZ);
            InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(BlockWeb.class));
            BlockUtil.placeBlock(this.playerPos, EnumHand.MAIN_HAND, ((Boolean) this.rotateSetting.getValue()).booleanValue(), true, false);
            if (((Boolean) this.switchBackSetting.getValue()).booleanValue()) {
                InventoryUtil.switchToSlot(this.beforeSlot);
            }

            this.setToggled(false);
        }

    }
}
