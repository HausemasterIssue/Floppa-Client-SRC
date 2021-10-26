package me.brownzombie.floppa.event.events;

import me.brownzombie.floppa.event.EventStage;
import net.minecraft.entity.Entity;

public class EventEntityCollision extends EventStage {

    Entity entity;
    double x;
    double y;
    double z;

    public EventEntityCollision(Entity entity, double x, double y, double z) {
        this.entity = entity;
        this.setX(this.x);
        this.setY(this.y);
        this.setZ(this.z);
    }

    public Entity getEntity() {
        return this.entity;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
