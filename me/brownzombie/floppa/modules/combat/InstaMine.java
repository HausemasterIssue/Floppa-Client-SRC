package me.brownzombie.floppa.modules.combat;

import java.util.function.Predicate;
import me.brownzombie.floppa.event.events.BlockEvent;
import me.brownzombie.floppa.event.events.PacketEvent;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.Timer;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InstaMine extends Module {

    public static InstaMine INSTANCE;
    private BlockPos renderBlock;
    private BlockPos lastBlock;
    private boolean packetCancel = false;
    private Timer breaktimer = new Timer();
    private EnumFacing direction;
    public Setting autoBreak = new Setting("Instant", Boolean.valueOf(false));
    public Setting pickOnly = new Setting("PickOnly", Boolean.valueOf(false));
    public Setting delay = new Setting("Delay", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(500));
    @EventHandler
    public Listener packetSendListener = new Listener((event) -> {
        Packet packet = event.getPacket();

        if (packet instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging digPacket = (CPacketPlayerDigging) packet;

            if (((CPacketPlayerDigging) packet).getAction() == Action.START_DESTROY_BLOCK && this.packetCancel) {
                event.isCanceled();
            }
        }

    }, new Predicate[0]);

    public InstaMine() {
        super("InstaMine", "", Module.Category.COMBAT);
        InstaMine.INSTANCE = this;
    }

    public void onUpdate() {
        if (InstaMine.mc.player != null) {
            if (this.renderBlock != null && ((Boolean) this.autoBreak.getValue()).booleanValue() && this.breaktimer.passedMs((long) ((Integer) this.delay.getValue()).intValue())) {
                if (((Boolean) this.pickOnly.getValue()).booleanValue() && InstaMine.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.DIAMOND_PICKAXE) {
                    return;
                }

                InstaMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.renderBlock, this.direction));
                this.breaktimer.reset();
            }

            try {
                InstaMine.mc.playerController.blockHitDelay = 0;
            } catch (Exception exception) {
                ;
            }
        }

    }

    @SubscribeEvent
    public void OnDamageBlock(BlockEvent event) {
        if (this.isToggled() && this.canBreak(event.pos)) {
            if (this.lastBlock != null && event.pos == this.lastBlock) {
                this.packetCancel = true;
            } else {
                this.packetCancel = false;
                InstaMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                InstaMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, event.pos, event.facing));
                this.packetCancel = true;
            }

            InstaMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
            this.renderBlock = event.pos;
            this.lastBlock = event.pos;
            this.direction = event.facing;
            event.isCanceled();
        }

    }

    private boolean canBreak(BlockPos pos) {
        IBlockState blockState = InstaMine.mc.world.getBlockState(pos);
        Block block = blockState.getBlock();

        return block.getBlockHardness(blockState, InstaMine.mc.world, pos) != -1.0F;
    }

    public BlockPos getTarget() {
        return this.renderBlock;
    }
}
