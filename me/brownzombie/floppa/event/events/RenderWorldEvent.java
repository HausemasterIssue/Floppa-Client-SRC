package me.brownzombie.floppa.event.events;

import me.brownzombie.floppa.event.EventStage;

public class RenderWorldEvent extends EventStage {

    float partialTicks;

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
