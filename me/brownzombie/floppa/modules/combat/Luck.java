package me.brownzombie.floppa.modules.combat;

import me.brownzombie.floppa.modules.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Luck extends Module {

    PotionEffect luck = new PotionEffect(Potion.getPotionById(26));

    public Luck() {
        super("Luck", "The Secret to Winning.", Module.Category.COMBAT);
    }

    public void onUpdate() {
        if (Luck.mc.player != null && Luck.mc.world != null) {
            Luck.mc.player.addPotionEffect(this.luck);
        }

    }

    public void onDisable() {
        if (Luck.mc.player != null && Luck.mc.world != null) {
            Luck.mc.player.removeActivePotionEffect(this.luck.getPotion());
        }

    }
}
