package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.modules.client.SkinChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Minecraft.class})
public abstract class MixinMinecraft {

    @Inject(
        method = { "shutdown"},
        at = {             @At("HEAD")}
    )
    public void shutdown(CallbackInfo ci) {
        SkinChanger.INSTANCE.deleteSkinChangerFiles();
        Floppa.configManager.save();
    }

    @Inject(
        method = { "crashed"},
        at = {             @At("HEAD")}
    )
    public void crashed(CrashReport crash, CallbackInfo ci) {
        SkinChanger.INSTANCE.deleteSkinChangerFiles();
        Floppa.configManager.save();
    }
}
