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

public class OffhandCrystal extends Module {

    public Setting health = new Setting("Health", Integer.valueOf(16), Integer.valueOf(1), Integer.valueOf(36));
    public static OffhandCrystal INSTANCE;
    private boolean dragging = false;
    private int totems = 0;

    public OffhandCrystal() {
        super("Crystal", "Offhand Crystal", Module.Category.OFFHAND);
        OffhandCrystal.INSTANCE = this;
    }

    public void onEnable() {
        OffhandTotem autoTotem = OffhandTotem.INSTANCE;
        OffhandGap offhandGap = OffhandGap.INSTANCE;
        SimpleOffhand simpleOffhand = SimpleOffhand.INSTANCE;

        if (simpleOffhand.isToggled()) {
            simpleOffhand.setToggled(false);
        }

        if (autoTotem.isToggled()) {
            autoTotem.setToggled(false);
        }

        if (offhandGap.isToggled()) {
            offhandGap.setToggled(false);
        }

    }

    public void onDisable() {
        OffhandTotem autoTotem = (OffhandTotem) Floppa.moduleManager.getModule("Totem");

        autoTotem.setToggled(true);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (!this.isToggled()) {
            if (!(OffhandCrystal.mc.currentScreen instanceof GuiContainer) && OffhandCrystal.mc.player != null) {
                int i;
                int slot;

                if (!OffhandCrystal.mc.player.inventory.getItemStack().isEmpty() && !this.dragging) {
                    for (i = 0; i < 45; ++i) {
                        if (OffhandCrystal.mc.player.inventory.getStackInSlot(i).isEmpty() || OffhandCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.AIR) {
                            slot = i < 9 ? i + 36 : i;
                            OffhandCrystal.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, OffhandCrystal.mc.player);
                            return;
                        }
                    }
                }

                if (OffhandCrystal.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                    this.dragging = false;
                } else if (this.dragging) {
                    OffhandCrystal.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, OffhandCrystal.mc.player);
                    this.dragging = false;
                } else {
                    for (i = 0; i < 45; ++i) {
                        if (OffhandCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                            slot = i < 9 ? i + 36 : i;
                            OffhandCrystal.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, OffhandCrystal.mc.player);
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
        if (!(OffhandCrystal.mc.currentScreen instanceof GuiContainer)) {
            EntityPlayerSP player = OffhandCrystal.mc.player;

            if (player != null) {
                int i;

                if (!OffhandCrystal.mc.player.inventory.getItemStack().isEmpty() && !this.dragging) {
                    for (int item = 0; item < 45; ++item) {
                        if (OffhandCrystal.mc.player.inventory.getStackInSlot(item).isEmpty() || OffhandCrystal.mc.player.inventory.getStackInSlot(item).getItem() == Items.AIR) {
                            i = item < 9 ? item + 36 : item;
                            OffhandCrystal.mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, player);
                            return;
                        }
                    }
                }

                this.totems = OffhandCrystal.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING ? OffhandCrystal.mc.player.getHeldItemOffhand().getCount() : 0;
                Iterator iterator = OffhandCrystal.mc.player.inventory.mainInventory.iterator();

                while (iterator.hasNext()) {
                    ItemStack itemstack = (ItemStack) iterator.next();

                    if (itemstack.getItem() == Items.TOTEM_OF_UNDYING) {
                        this.totems += itemstack.getCount();
                    }
                }

                Item item = this.shouldTotem() ? Items.TOTEM_OF_UNDYING : Items.END_CRYSTAL;

                if (OffhandCrystal.mc.player.getHeldItemOffhand().getItem() != item) {
                    if (this.dragging) {
                        OffhandCrystal.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, player);
                        this.dragging = false;
                    } else {
                        for (i = 0; i < 45; ++i) {
                            if (OffhandCrystal.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                                int slot = i < 9 ? i + 36 : i;

                                OffhandCrystal.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, player);
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
        boolean hp = OffhandCrystal.mc.player.getHealth() + OffhandCrystal.mc.player.getAbsorptionAmount() <= (float) ((Integer) this.health.getValue()).intValue();
        boolean totemCount = this.totems > 0 || this.dragging || !OffhandCrystal.mc.player.inventory.getItemStack().isEmpty();

        return hp && totemCount;
    }
}
