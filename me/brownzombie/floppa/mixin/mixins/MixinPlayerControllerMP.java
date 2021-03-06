package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.event.events.BlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ PlayerControllerMP.class})
public class MixinPlayerControllerMP {

    @Inject(
        method = { "clickBlock"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void clickBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable info) {
        BlockEvent event = new BlockEvent(3, pos, face);

        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(
        method = { "onPlayerDamageBlock"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable info) {
        BlockEvent event = new BlockEvent(4, pos, face);

        MinecraftForge.EVENT_BUS.post(event);
    }
}
