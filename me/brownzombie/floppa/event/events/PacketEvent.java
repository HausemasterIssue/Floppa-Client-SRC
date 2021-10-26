package me.brownzombie.floppa.event.events;

import me.brownzombie.floppa.event.EventStage;
import net.minecraft.network.Packet;

public class PacketEvent extends EventStage {

    private final Packet packet;

    public PacketEvent(int stage, Packet packet) {
        super(stage);
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public static class PostSend extends PacketEvent {

        public PostSend(int stage, Packet packet) {
            super(stage, packet);
        }
    }

    public static class PostReceive extends PacketEvent {

        public PostReceive(int stage, Packet packet) {
            super(stage, packet);
        }
    }

    public static class Send extends PacketEvent {

        public Send(int stage, Packet packet) {
            super(stage, packet);
        }
    }

    public static class Receive extends PacketEvent {

        public Receive(int stage, Packet packet) {
            super(stage, packet);
        }
    }
}
