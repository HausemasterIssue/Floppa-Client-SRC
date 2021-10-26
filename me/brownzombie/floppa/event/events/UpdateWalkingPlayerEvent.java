package me.brownzombie.floppa.event.events;

import me.brownzombie.floppa.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class UpdateWalkingPlayerEvent extends EventStage {

    public UpdateWalkingPlayerEvent(int stage) {
        super(stage);
    }
}
