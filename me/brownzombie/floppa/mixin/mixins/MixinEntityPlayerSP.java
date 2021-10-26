package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.event.events.UpdateWalkingPlayerEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ EntityPlayerSP.class})
public class MixinEntityPlayerSP {

    @Inject(
        method = { "onUpdateWalkingPlayer"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void preMotion(CallbackInfo info) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);

        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
        }

    }

    @Inject(
        method = { "onUpdateWalkingPlayer"},
        at = {             @At("RETURN")}
    )
    private void postMotion(CallbackInfo info) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);

        MinecraftForge.EVENT_BUS.post(event);
    }
}
