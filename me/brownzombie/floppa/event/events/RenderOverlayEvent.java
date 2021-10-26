package me.brownzombie.floppa.event.events;

import me.brownzombie.floppa.event.EventStage;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;

public class RenderOverlayEvent extends EventStage {

    public OverlayType overlayType;
    public float partialTicks;

    public RenderOverlayEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public OverlayType getOverlayType() {
        return this.overlayType;
    }

    public void setOverlayType(OverlayType overlayType) {
        this.overlayType = overlayType;
    }
}
