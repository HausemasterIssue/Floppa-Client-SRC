package me.brownzombie.floppa.modules.offhand;

import java.util.Iterator;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class OffhandTotem extends Module {

    private Setting soft = new Setting("Soft", Boolean.valueOf(false));
    public static OffhandTotem INSTANCE;
    private boolean dragging = false;
    private int totems = 0;

    public OffhandTotem() {
        super("Totem", "Offhand Totem", Module.Category.OFFHAND);
        OffhandTotem.INSTANCE = this;
    }

    public void onEnable() {
        OffhandCrystal offhandCrystal = OffhandCrystal.INSTANCE;
        OffhandGap offhandGap = OffhandGap.INSTANCE;
        SimpleOffhand simpleOffhand = SimpleOffhand.INSTANCE;

        if (simpleOffhand.isToggled()) {
            simpleOffhand.setToggled(false);
        }

        if (offhandCrystal.isToggled()) {
            offhandCrystal.setToggled(false);
        }

        if (offhandGap.isToggled()) {
            offhandGap.setToggled(false);
        }

    }

    public void onUpdate() {
        if (!(OffhandTotem.mc.currentScreen instanceof GuiContainer)) {
            EntityPlayerSP player = OffhandTotem.mc.player;

            if (player != null) {
                int i;
                int slot;

                if (!OffhandTotem.mc.player.inventory.getItemStack().isEmpty() && !this.dragging) {
                    for (i = 0; i < 45; ++i) {
                        if (OffhandTotem.mc.player.inventory.getStackInSlot(i).isEmpty() || OffhandTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.AIR) {
                            slot = i < 9 ? i + 36 : i;
                            OffhandTotem.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                            return;
                        }
                    }
                }

                this.totems = 0;
                Iterator iterator = OffhandTotem.mc.player.inventory.mainInventory.iterator();

                while (iterator.hasNext()) {
                    ItemStack itemstack = (ItemStack) iterator.next();

                    if (itemstack.getItem() == Items.TOTEM_OF_UNDYING) {
                        this.totems += itemstack.getCount();
                    }
                }

                if (OffhandTotem.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                    this.totems += OffhandTotem.mc.player.getHeldItemOffhand().getCount();
                } else if (!((Boolean) this.soft.getValue()).booleanValue() || OffhandTotem.mc.player.getHeldItemOffhand().isEmpty()) {
                    if (this.dragging) {
                        OffhandTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                        this.dragging = false;
                    } else {
                        for (i = 0; i < 45; ++i) {
                            if (OffhandTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                                slot = i < 9 ? i + 36 : i;
                                OffhandTotem.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                                this.dragging = true;
                                return;
                            }
                        }

                    }
                }
            }
        }
    }
}
