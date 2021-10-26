package me.brownzombie.floppa.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventRenderHurtCameraEffect extends Event {

    public float Ticks;

    public EventRenderHurtCameraEffect(float Ticks) {
        this.Ticks = Ticks;
    }
}
