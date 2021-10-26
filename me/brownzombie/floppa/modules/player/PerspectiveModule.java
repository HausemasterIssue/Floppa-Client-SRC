package me.brownzombie.floppa.modules.player;

import me.brownzombie.floppa.modules.Module;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public class PerspectiveModule extends Module {

    public static PerspectiveModule INSTANCE;
    public float cameraPitch;
    public float cameraYaw;

    public PerspectiveModule() {
        super("Perspective", "Perspective Mod", Module.Category.PLAYER);
        PerspectiveModule.INSTANCE = this;
    }

    public void onDisable() {
        if (PerspectiveModule.mc.player != null && PerspectiveModule.mc.gameSettings.thirdPersonView == 1) {
            PerspectiveModule.mc.gameSettings.thirdPersonView = 0;
        }

    }

    public void onUpdate() {
        if (PerspectiveModule.mc.player != null && PerspectiveModule.mc.gameSettings.thirdPersonView != 1) {
            PerspectiveModule.mc.gameSettings.thirdPersonView = 1;
        }

    }

    @SubscribeEvent
    public void onKey(KeyInputEvent event) {
        if (Keyboard.isKeyDown(this.getKey())) {
            this.cameraPitch = PerspectiveModule.mc.player.rotationPitch;
            this.cameraYaw = PerspectiveModule.mc.player.rotationYaw;
            PerspectiveModule.mc.gameSettings.thirdPersonView = this.isToggled() ? 1 : 0;
        }

    }

    @SubscribeEvent
    public void cameraSetup(CameraSetup event) {
        if (this.isToggled()) {
            event.setPitch(this.cameraPitch);
            event.setYaw(this.cameraYaw);
        }

    }
}
