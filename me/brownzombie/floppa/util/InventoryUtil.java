package me.brownzombie.floppa.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static int findSlotFullInventory(Class clazz) {
        for (int i = 0; i < 45; ++i) {
            ItemStack item = InventoryUtil.mc.player.inventory.getStackInSlot(i);

            if (item != ItemStack.EMPTY) {
                if (clazz.isInstance(item.getItem())) {
                    return i;
                }

                if (item.getItem() instanceof ItemBlock) {
                    Block block = ((ItemBlock) item.getItem()).getBlock();

                    if (clazz.isInstance(block)) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    public static int findSlotHotbar(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = InventoryUtil.mc.player.inventory.getStackInSlot(i);

            if (item != ItemStack.EMPTY) {
                if (clazz.isInstance(item.getItem())) {
                    return i;
                }

                if (item.getItem() instanceof ItemBlock) {
                    Block block = ((ItemBlock) item.getItem()).getBlock();

                    if (clazz.isInstance(block)) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    public static void switchToSlot(int slot) {
        InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        InventoryUtil.mc.player.inventory.currentItem = slot;
        InventoryUtil.mc.playerController.updateController();
    }
}
