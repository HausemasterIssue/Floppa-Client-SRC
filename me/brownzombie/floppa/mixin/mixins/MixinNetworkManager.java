package me.brownzombie.floppa.mixin.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.event.events.PacketEvent;
import me.brownzombie.floppa.event.events.PacketSendEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ NetworkManager.class})
public class MixinNetworkManager {

    @Inject(
        method = { "sendPacket(Lnet/minecraft/network/Packet;)V"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void sendPacketPre(Packet packetIn, CallbackInfo ci) {
        PacketSendEvent event = new PacketSendEvent(0, packetIn);

        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }

    }

    @Inject(
        method = { "channelRead0"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void channelRead0Pre(ChannelHandlerContext context, Packet packet, CallbackInfo info) {
        PacketEvent.Receive event = new PacketEvent.Receive(0, packet);

        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
        }

    }

    @Inject(
        method = { "sendPacket(Lnet/minecraft/network/Packet;)V"},
        at = {             @At("TAIL")},
        cancellable = true
    )
    private void sendPacketPost(Packet packetIn, CallbackInfo ci) {
        PacketSendEvent.Post event = new PacketSendEvent.Post(1, packetIn);

        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(
        method = { "channelRead0"},
        at = {             @At("RETURN")},
        cancellable = true
    )
    public void channelRead0Post(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_, CallbackInfo ci) {
        PacketEvent.PostReceive packet = new PacketEvent.PostReceive(1, p_channelRead0_2_);

        Floppa.EVENT_BUS.post(packet);
        if (packet.isCanceled()) {
            ci.cancel();
        }

    }
}
