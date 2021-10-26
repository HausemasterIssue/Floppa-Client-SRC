package me.brownzombie.floppa.event.events;

import me.brownzombie.floppa.event.EventStage;

public class RenderModelEvent extends EventStage {

    public boolean rotating = false;
    public float pitch = 0.0F;

    public RenderModelEvent(int stage) {
        super(stage);
    }
}
