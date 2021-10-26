package me.brownzombie.floppa.modules.offhand;

import java.util.Iterator;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class OffhandGap extends Module {

    public Setting health = new Setting("Health", Integer.valueOf(16), Integer.valueOf(1), Integer.valueOf(36));
    public static OffhandGap INSTANCE;
    private boolean dragging = false;
    private int totems = 0;

    public OffhandGap() {
        super("Gapple", "Offhand Gap", Module.Category.OFFHAND);
        OffhandGap.INSTANCE = this;
    }

    public void onEnable() {
        OffhandTotem autoTotem = OffhandTotem.INSTANCE;
        OffhandCrystal offhandCrystal = OffhandCrystal.INSTANCE;
        SimpleOffhand simpleOffhand = SimpleOffhand.INSTANCE;

        if (simpleOffhand.isToggled()) {
            simpleOffhand.setToggled(false);
        }

        if (autoTotem.isToggled()) {
            autoTotem.setToggled(false);
        }

        if (offhandCrystal.isToggled()) {
            offhandCrystal.setToggled(false);
        }

    }

    public void onDisable() {
        OffhandTotem autoTotem = (OffhandTotem) Floppa.moduleManager.getModule("Totem");

        autoTotem.setToggled(true);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (!this.isToggled()) {
            if (!(OffhandGap.mc.currentScreen instanceof GuiContainer) && OffhandGap.mc.player != null) {
                int i;
                int slot;

                if (!OffhandGap.mc.player.inventory.getItemStack().isEmpty() && !this.dragging) {
                    for (i = 0; i < 45; ++i) {
                        if (OffhandGap.mc.player.inventory.getStackInSlot(i).isEmpty() || OffhandGap.mc.player.inventory.getStackInSlot(i).getItem() == Items.AIR) {
                            slot = i < 9 ? i + 36 : i;
                            OffhandGap.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, OffhandGap.mc.player);
                            return;
                        }
                    }
                }

                if (OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                    this.dragging = false;
                } else if (this.dragging) {
                    OffhandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, OffhandGap.mc.player);
                    this.dragging = false;
                } else {
                    for (i = 0; i < 45; ++i) {
                        if (OffhandGap.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                            slot = i < 9 ? i + 36 : i;
                            OffhandGap.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, OffhandGap.mc.player);
                            this.dragging = true;
                            return;
                        }
                    }

                }
            } else {
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

    public void onUpdate() {
        if (!(OffhandGap.mc.currentScreen instanceof GuiContainer)) {
            EntityPlayerSP player = OffhandGap.mc.player;

            if (player != null) {
                int i;

                if (!OffhandGap.mc.player.inventory.getItemStack().isEmpty() && !this.dragging) {
                    for (int item = 0; item < 45; ++item) {
                        if (OffhandGap.mc.player.inventory.getStackInSlot(item).isEmpty() || OffhandGap.mc.player.inventory.getStackInSlot(item).getItem() == Items.AIR) {
                            i = item < 9 ? item + 36 : item;
                            OffhandGap.mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, player);
                            return;
                        }
                    }
                }

                this.totems = OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING ? OffhandGap.mc.player.getHeldItemOffhand().getCount() : 0;
                Iterator iterator = OffhandGap.mc.player.inventory.mainInventory.iterator();

                while (iterator.hasNext()) {
                    ItemStack itemstack = (ItemStack) iterator.next();

                    if (itemstack.getItem() == Items.TOTEM_OF_UNDYING) {
                        this.totems += itemstack.getCount();
                    }
                }

                Item item = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.GOLDEN_APPLE;

                if (OffhandGap.mc.player.getHeldItemOffhand().getItem() != item) {
                    if (this.dragging) {
                        OffhandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                        this.dragging = false;
                    } else {
                        for (i = 0; i < 45; ++i) {
                            if (OffhandGap.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                                int slot = i < 9 ? i + 36 : i;

                                OffhandGap.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
                                this.dragging = true;
                                return;
                            }
                        }

                    }
                }
            }
        }
    }

    private boolean shouldTotem() {
        boolean hp = OffhandGap.mc.player.getHealth() + OffhandGap.mc.player.getAbsorptionAmount() <= (float) ((Integer) this.health.getValue()).intValue();
        boolean totemCount = this.totems > 0 || this.dragging || !OffhandGap.mc.player.inventory.getItemStack().isEmpty();

        return hp && totemCount;
    }
}
