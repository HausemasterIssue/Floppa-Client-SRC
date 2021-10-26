package me.brownzombie.floppa.modules.offhand;

import java.util.Iterator;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SimpleOffhand extends Module {

    private Setting health = new Setting("Health", Integer.valueOf(16), Integer.valueOf(0), Integer.valueOf(40));
    private Setting soft = new Setting("Soft", Boolean.valueOf(false));
    private Setting mode;
    public static SimpleOffhand INSTANCE;
    Item item;
    private boolean dragging;
    private int totems;

    public SimpleOffhand() {
        super("SimpleOffhand", "Puts stuff in ur offhand.", Module.Category.OFFHAND);
        this.mode = new Setting("Mode", SimpleOffhand.Mode.TOTEM);
        this.dragging = false;
        this.totems = 0;
        SimpleOffhand.INSTANCE = this;
    }

    public void onEnable() {
        OffhandCrystal offhandCrystal = OffhandCrystal.INSTANCE;
        OffhandTotem autoTotem = OffhandTotem.INSTANCE;
        OffhandGap offhandGap = OffhandGap.INSTANCE;

        if (offhandCrystal.isToggled()) {
            offhandCrystal.setToggled(false);
        }

        if (offhandGap.isToggled()) {
            offhandGap.setToggled(false);
        }

        if (autoTotem.isToggled()) {
            autoTotem.setToggled(false);
        }

    }

    public void onUpdate() {
        if (!(SimpleOffhand.mc.currentScreen instanceof GuiContainer)) {
            EntityPlayerSP player = SimpleOffhand.mc.player;

            if (player != null) {
                int i;
                int slot;

                if (!player.inventory.getItemStack().isEmpty() && !this.dragging) {
                    for (i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).isEmpty() || player.inventory.getStackInSlot(i).getItem() == Items.AIR) {
                            slot = i < 9 ? i + 36 : i;
                            SimpleOffhand.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                            return;
                        }
                    }
                }

                Iterator iterator;
                ItemStack itemstack;

                if (((SimpleOffhand.Mode) this.mode.getValue()).equals(SimpleOffhand.Mode.TOTEM)) {
                    this.totems = 0;
                    iterator = player.inventory.mainInventory.iterator();

                    while (iterator.hasNext()) {
                        itemstack = (ItemStack) iterator.next();
                        if (itemstack.getItem() == Items.TOTEM_OF_UNDYING) {
                            this.totems += itemstack.getCount();
                        }
                    }

                    if (player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                        this.totems += player.getHeldItemOffhand().getCount();
                        return;
                    }

                    if (((Boolean) this.soft.getValue()).booleanValue() && !player.getHeldItemOffhand().isEmpty()) {
                        return;
                    }

                    if (this.dragging) {
                        SimpleOffhand.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                        this.dragging = false;
                        return;
                    }

                    for (i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                            slot = i < 9 ? i + 36 : i;
                            SimpleOffhand.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                            this.dragging = true;
                            return;
                        }
                    }
                } else {
                    this.totems = player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING ? player.getHeldItemOffhand().getCount() : 0;
                    iterator = player.inventory.mainInventory.iterator();

                    while (iterator.hasNext()) {
                        itemstack = (ItemStack) iterator.next();
                        if (itemstack.getItem() == Items.TOTEM_OF_UNDYING) {
                            this.totems += itemstack.getCount();
                        }
                    }

                    if (((SimpleOffhand.Mode) this.mode.getValue()).equals(SimpleOffhand.Mode.CRYSTAL)) {
                        this.item = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.END_CRYSTAL;
                    } else if (((SimpleOffhand.Mode) this.mode.getValue()).equals(SimpleOffhand.Mode.GAP)) {
                        this.item = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.GOLDEN_APPLE;
                    } else if (((SimpleOffhand.Mode) this.mode.getValue()).equals(SimpleOffhand.Mode.CHORUS)) {
                        this.item = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.CHORUS_FRUIT;
                    } else {
                        this.item = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.ENDER_PEARL;
                    }

                    if (player.getHeldItemOffhand().getItem() == this.item) {
                        return;
                    }

                    if (this.dragging) {
                        SimpleOffhand.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                        this.dragging = false;
                        return;
                    }

                    for (i = 0; i < 45; ++i) {
                        if (player.inventory.getStackInSlot(i).getItem() == this.item) {
                            slot = i < 9 ? i + 36 : i;
                            SimpleOffhand.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                            this.dragging = true;
                            return;
                        }
                    }
                }

            }
        }
    }

    private boolean shouldTotem() {
        boolean hp = SimpleOffhand.mc.player.getHealth() + SimpleOffhand.mc.player.getAbsorptionAmount() <= (float) ((Integer) this.health.getValue()).intValue();
        boolean totemCount = this.totems > 0 || this.dragging || !SimpleOffhand.mc.player.inventory.getItemStack().isEmpty();

        return hp && totemCount;
    }

    public static enum Mode {

        TOTEM, CRYSTAL, GAP, CHORUS, PEARL;
    }
}
