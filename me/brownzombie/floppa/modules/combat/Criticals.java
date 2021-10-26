package me.brownzombie.floppa.modules.combat;

import java.util.function.Predicate;
import me.brownzombie.floppa.event.events.PacketEvent;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketUseEntity.Action;

public class Criticals extends Module {

    @EventHandler
    private Listener onPacketPre = new Listener((event) -> {
        MessageManager.sendClientMessage("TROLLED", false);
        if (event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() == Action.ATTACK && Criticals.mc.player.onGround) {
            Criticals.mc.player.connection.sendPacket(new Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.10000000149011612D, Criticals.mc.player.posZ, false));
            Criticals.mc.player.connection.sendPacket(new Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
        }

    }, new Predicate[0]);

    public Criticals() {
        super("Criticals", "", Module.Category.COMBAT);
    }
}
