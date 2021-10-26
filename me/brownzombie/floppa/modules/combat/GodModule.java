package me.brownzombie.floppa.modules.combat;

import java.util.Objects;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.event.events.PacketEvent;
import me.brownzombie.floppa.event.events.UpdateWalkingPlayerEvent;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GodModule extends Module {

    public Minecraft mc = Minecraft.getMinecraft();
    public Entity entity;
    private final Setting remount = new Setting("Remount", Boolean.valueOf(false));

    public GodModule() {
        super("Godmode", "Hi there :D", Module.Category.PLAYER);
    }

    public void onEnable() {
        super.onEnable();
        if (this.mc.world != null && this.mc.player.getRidingEntity() != null) {
            this.entity = this.mc.player.getRidingEntity();
            this.mc.renderGlobal.loadRenderers();
            this.hideEntity();
            this.mc.player.setPosition((double) Minecraft.getMinecraft().player.getPosition().getX(), (double) (Minecraft.getMinecraft().player.getPosition().getY() - 1), (double) Minecraft.getMinecraft().player.getPosition().getZ());
        }

        if (this.mc.world != null && ((Boolean) this.remount.getValue()).booleanValue()) {
            this.remount.setValue(Boolean.valueOf(false));
        }

    }

    public void onDisable() {
        super.onDisable();
        if (((Boolean) this.remount.getValue()).booleanValue()) {
            this.remount.setValue(Boolean.valueOf(false));
        }

        this.mc.player.dismountRidingEntity();
        this.mc.getConnection().sendPacket(new CPacketEntityAction(this.mc.player, Action.START_SNEAKING));
        this.mc.getConnection().sendPacket(new CPacketEntityAction(this.mc.player, Action.STOP_SNEAKING));
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof Position || event.getPacket() instanceof PositionRotation) {
            event.setCanceled(true);
        }

    }

    private void hideEntity() {
        if (this.mc.player.getRidingEntity() != null) {
            this.mc.player.dismountRidingEntity();
            this.mc.world.removeEntity(this.entity);
        }

    }

    private void showEntity(Entity entity2) {
        entity2.isDead = false;
        this.mc.world.loadedEntityList.add(entity2);
        this.mc.player.startRiding(entity2, true);
    }

    @SubscribeEvent
    public void onPlayerWalkingUpdate(UpdateWalkingPlayerEvent event) {
        if (this.entity != null) {
            if (event.getStage() == 0) {
                if (((Boolean) this.remount.getValue()).booleanValue()) {
                    Floppa.getInstance();
                    if (((Module) Objects.requireNonNull(Floppa.moduleManager.getModule("GodModule"))).isToggled()) {
                        this.showEntity(this.entity);
                    }
                }

                this.entity.setPositionAndRotation(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ, Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch);
                this.mc.player.connection.sendPacket(new Rotation(this.mc.player.rotationYaw, this.mc.player.rotationPitch, true));
                this.mc.player.connection.sendPacket(new CPacketInput(this.mc.player.movementInput.moveForward, this.mc.player.movementInput.moveStrafe, false, false));
                this.mc.player.connection.sendPacket(new CPacketVehicleMove(this.entity));
            }

        }
    }
}
