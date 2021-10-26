package me.brownzombie.floppa.modules.misc;

import me.brownzombie.floppa.modules.Module;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsTest extends Module {

    private final SoundEvent ping = this.registerSound("ping");

    public SoundsTest() {
        super("SoundsTest", "To troll much", Module.Category.MISC);
    }

    public void onUpdate() {
        if (SoundsTest.mc.player != null && SoundsTest.mc.world != null) {
            SoundsTest.mc.player.playSound(this.ping, 1.0F, 1.0F);
        }

    }

    public void onEnable() {
        if (SoundsTest.mc.player != null && SoundsTest.mc.world != null) {
            SoundsTest.mc.player.playSound(this.ping, 1.0F, 1.0F);
        }

    }

    private SoundEvent registerSound(String name) {
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation("minecraft:sounds/" + name));

        soundEvent.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(soundEvent);
        return soundEvent;
    }
}
