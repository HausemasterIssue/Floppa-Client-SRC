package me.brownzombie.floppa.event;

import java.util.Iterator;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.event.events.DisconnectEvent;
import me.brownzombie.floppa.event.events.PacketEvent;
import me.brownzombie.floppa.event.events.RenderWorldEvent;
import me.brownzombie.floppa.event.events.TickEvent;
import me.brownzombie.floppa.event.events.UpdateWalkingPlayerEvent;
import me.brownzombie.floppa.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class Adapter {

    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!event.isCanceled()) {
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.shadeModel(7425);
            GlStateManager.disableDepth();
            GlStateManager.glLineWidth(1.0F);
            Floppa.EVENT_BUS.post(new RenderWorldEvent());
            GlStateManager.glLineWidth(1.0F);
            GlStateManager.shadeModel(7424);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.enableCull();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        Floppa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Iterator iterator = Floppa.moduleManager.getModules().iterator();

        while (iterator.hasNext()) {
            Module m = (Module) iterator.next();

            if (m.isToggled()) {
                m.onRender(event.getPartialTicks());
            }
        }

    }

    @SubscribeEvent
    public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
        Floppa.EVENT_BUS.post(new DisconnectEvent());
    }

    @SubscribeEvent
    public void onUpdate(ClientTickEvent event) {
        if (this.mc.player != null && this.mc.world != null) {
            Floppa.EVENT_BUS.post(new TickEvent());
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0) {
            Floppa.serverManager.onPacketReceived();
            if (event.getPacket() instanceof SPacketTimeUpdate) {
                Floppa.serverManager.update();
            }

        }
    }

    @SubscribeEvent(
        priority = EventPriority.HIGHEST
    )
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.mc.player == null || this.mc.world == null) {
            if (event.getStage() == 0) {
                Floppa.rotationUtil.updateRotations();
            }

            if (event.getStage() == 1) {
                Floppa.rotationUtil.restoreRotations();
            }

        }
    }
}
