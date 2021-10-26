package me.brownzombie.floppa.modules.render;

import java.util.function.Predicate;
import me.brownzombie.floppa.event.events.EventRenderArmorLayer;
import me.brownzombie.floppa.event.events.EventRenderHurtCameraEffect;
import me.brownzombie.floppa.event.events.PacketEvent;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;

public class NoRender extends Module {

    public static NoRender INSTANCE;
    public Setting armor = new Setting("Armor", Boolean.valueOf(true));
    public Setting xporbs = new Setting("ExpOrbs", Boolean.valueOf(false));
    public Setting fire = new Setting("Fire", Boolean.valueOf(true));
    public Setting water = new Setting("Water", Boolean.valueOf(true));
    public Setting gentity = new Setting("Global Entities", Boolean.valueOf(true));
    public Setting totems = new Setting("Totem Pops", Boolean.valueOf(true));
    public Setting hurtCam = new Setting("Hurtcam", Boolean.valueOf(true));
    @EventHandler
    Listener onRender = new Listener((e) -> {
        if (e.getPacket() instanceof SPacketSpawnExperienceOrb && ((Boolean) this.xporbs.value).booleanValue()) {
            e.isCanceled();
        }

        if (e.getPacket() instanceof SPacketSpawnGlobalEntity && ((Boolean) this.gentity.value).booleanValue()) {
            e.isCanceled();
        }

        if (e.getPacket() instanceof SPacketEntityStatus) {
            if (NoRender.mc.world == null || NoRender.mc.player == null) {
                return;
            }

            SPacketEntityStatus packet = (SPacketEntityStatus) e.getPacket();

            if (packet.getOpCode() == 35 && ((Boolean) this.totems.getValue()).booleanValue()) {
                e.isCanceled();
            }
        }

    }, new Predicate[0]);
    @EventHandler
    private Listener onRenderArmorLayer = new Listener((e) -> {
        if (((Boolean) this.armor.getValue()).booleanValue() && e.Entity instanceof EntityPlayer) {
            e.isCanceled();
        }

    }, new Predicate[0]);
    @EventHandler
    private Listener onBlockOverlayEvent = new Listener((e) -> {
        if (((Boolean) this.fire.getValue()).booleanValue() && e.getOverlayType() == OverlayType.FIRE) {
            e.setCanceled(true);
        }

        if (((Boolean) this.water.getValue()).booleanValue() && e.getOverlayType() == OverlayType.WATER) {
            e.setCanceled(true);
        }

    }, new Predicate[0]);
    @EventHandler
    private Listener onHurt = new Listener((e) -> {
        if (((Boolean) this.hurtCam.getValue()).booleanValue()) {
            e.isCanceled();
        }

    }, new Predicate[0]);

    public NoRender() {
        super("NoRender", "", Module.Category.RENDER);
    }
}
