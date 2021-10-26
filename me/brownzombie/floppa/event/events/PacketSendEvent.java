package me.brownzombie.floppa.event.events;

import me.brownzombie.floppa.event.EventStage;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PacketSendEvent extends EventStage {

    private Packet packet;

    public PacketSendEvent(int stage, Packet packet) {
        super(stage);
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public static class Post extends PacketSendEvent {

        public Post(int stage, Packet packet) {
            super(stage, packet);
        }
    }
}
