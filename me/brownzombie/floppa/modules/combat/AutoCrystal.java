package me.brownzombie.floppa.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;

public class AutoCrystal extends Module {

    private Setting range = new Setting("Range", Double.valueOf(4.5D), Double.valueOf(0.0D), Double.valueOf(10.0D));
    private Setting breakdelay = new Setting("BreakDelay", Double.valueOf(5.0D), Double.valueOf(0.0D), Double.valueOf(1000.0D));
    private Setting placedelay = new Setting("BreakDelay", Double.valueOf(5.0D), Double.valueOf(0.0D), Double.valueOf(1000.0D));
    private Setting minDmg = new Setting("MinDmg", Float.valueOf(7.0F), Float.valueOf(0.1F), Float.valueOf(36.0F));
    private Setting extraSafe = new Setting("ExtraSafe", Boolean.valueOf(false));
    private Setting strict = new Setting("StrictAnims", Boolean.valueOf(false));
    private Timer breakTimer = new Timer();
    private BlockPos bestPos = null;
    private Entity bestCrystal = null;

    public AutoCrystal() {
        super("AutoCrystal", "", Module.Category.COMBAT);
    }

    public void onEnable() {}

    public void onUpdate() {
        if (AutoCrystal.mc.player != null && AutoCrystal.mc.world != null) {
            List playerListSorted = this.getPlayersSorted();

            if (playerListSorted.isEmpty()) {
                return;
            }

            this.breakAllCrystals();
            Iterator iterator = playerListSorted.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                this.placeCrystals(player);
            }
        }

    }

    private boolean checkCrystalAgainstPlayer(EntityEnderCrystal crystal, EntityPlayer avoidPos) {
        return (double) AutoCrystal.mc.player.getDistance(crystal) > ((Double) this.range.value).doubleValue() ? (((Boolean) this.extraSafe.value).booleanValue() && this.rayCheckEntity(crystal, avoidPos) ? false : false) : true;
    }

    private void placeCrystal(BlockPos blockPos) {
        AutoCrystal.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
        if (((Boolean) this.strict.value).booleanValue()) {
            AutoCrystal.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }

        double x = (double) blockPos.getX();
        double y = (double) blockPos.getY() + 1.0D;
        double z = (double) blockPos.getZ();
        AxisAlignedBB scanArea = new AxisAlignedBB(x, y, z, x, y + 0.1D, z);

        synchronized (AutoCrystal.mc.world.getLoadedEntityList()) {
            List crystals = AutoCrystal.mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, scanArea);

            crystals.forEach(this::breakCrystal);
        }
    }

    private void placeCrystals(Entity player) {}

    private void breakCrystal(EntityEnderCrystal crystal) {
        if (this.breakTimer.passedMs(((Double) this.breakdelay.value).longValue())) {
            this.breakTimer.reset();
            if (((Boolean) this.extraSafe.value).booleanValue() && this.checkCrystalAgainstPlayer(crystal, AutoCrystal.mc.player)) {
                return;
            }

            CPacketUseEntity attackPacket = new CPacketUseEntity();

            attackPacket.entityId = crystal.entityId;
            attackPacket.action = Action.ATTACK;
            AutoCrystal.mc.player.connection.sendPacket(attackPacket);
            if (((Boolean) this.strict.value).booleanValue()) {
                AutoCrystal.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            }
        }

    }

    private boolean rayCheckEntity(Entity to, Entity from) {
        RayTraceResult ray = AutoCrystal.mc.world.rayTraceBlocks(from.getPositionVector(), to.getPositionVector());

        return ray == null ? true : ray.typeOfHit.equals(Type.ENTITY) && ray.entityHit.equals(to);
    }

    private boolean rayCheckBlockPosToEntity(BlockPos from, Entity to) {
        RayTraceResult ray = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d((double) from.x, (double) from.y, (double) from.z), new Vec3d(to.posX, to.posY, to.posZ));

        return ray == null ? true : ray.typeOfHit.equals(Type.ENTITY) && ray.entityHit.equals(to);
    }

    private void breakAllCrystals() {
        Iterator iterator = AutoCrystal.mc.world.getLoadedEntityList().iterator();

        while (iterator.hasNext()) {
            Entity enderCrystal = (Entity) iterator.next();

            if ((double) AutoCrystal.mc.player.getDistance(enderCrystal) <= ((Double) this.range.value).doubleValue() && enderCrystal instanceof EntityEnderCrystal) {
                this.breakCrystal((EntityEnderCrystal) enderCrystal);
            }
        }

    }

    private List getPlayersSorted() {
        List list = AutoCrystal.mc.world.playerEntities;

        synchronized (AutoCrystal.mc.world.playerEntities) {
            ArrayList playerList = new ArrayList();
            Iterator iterator = AutoCrystal.mc.world.playerEntities.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                if (AutoCrystal.mc.player != player && (double) AutoCrystal.mc.player.getDistance(player) <= ((Double) this.range.value).doubleValue()) {
                    playerList.add(player);
                }
            }

            playerList.sort(Comparator.comparing((eP) -> {
                return Float.valueOf(AutoCrystal.mc.player.getDistance(eP));
            }));
            return playerList;
        }
    }
}
