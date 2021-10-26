package me.brownzombie.floppa.modules.misc;

import java.awt.Color;
import java.util.ArrayList;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.event.events.BlockEvent;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.BlockUtil;
import me.brownzombie.floppa.util.RenderUtil;
import me.brownzombie.floppa.util.Timer;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PacketMine extends Module {

    public Setting tweaks = new Setting("Tweaks", Boolean.valueOf(true));
    public Setting render = new Setting("Render", Boolean.valueOf(true));
    public Setting overrideRender = new Setting("CustomRenderColors", Boolean.valueOf(false));
    public Setting renderMode;
    public Setting colorMode;
    public Setting red;
    public Setting green;
    public Setting blue;
    public Setting alpha;
    public Setting array;
    public Setting reset;
    public Setting doubleRender;
    public Setting red2;
    public Setting green2;
    public Setting blue2;
    public Setting alpha2;
    public static PacketMine INSTANCE;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private final Timer colorTimer;
    private final Timer fadeTimer;
    public ArrayList positions;
    public int idkNigga;

    public PacketMine() {
        super("PacketMine", "", Module.Category.MISC);
        this.renderMode = new Setting("RenderMode", PacketMine.RenderMode.FULL);
        this.colorMode = new Setting("ColorMode", PacketMine.ColorMode.WHITE, test<invokedynamic>(this));
        this.red = new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
        this.green = new Setting("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
        this.blue = new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
        this.alpha = new Setting("Alpha", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
        this.array = new Setting("Array", Boolean.valueOf(false));
        this.reset = new Setting("Reset", Boolean.valueOf(false));
        this.doubleRender = new Setting("DoubleRender", Boolean.valueOf(false), test<invokedynamic>(this));
        this.red2 = new Setting("Double Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
        this.green2 = new Setting("Double Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
        this.blue2 = new Setting("Double Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
        this.alpha2 = new Setting("Double Alpha", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
        this.colorTimer = new Timer();
        this.fadeTimer = new Timer();
        this.positions = new ArrayList();
        this.idkNigga = 0;
        PacketMine.INSTANCE = this;
    }

    public void onDisable() {
        this.idkNigga = 0;
        this.positions.clear();
        this.currentPos = null;
        this.colorTimer.reset();
        this.fadeTimer.reset();
        this.currentBlockState = null;
    }

    public void onUpdate() {
        if (PacketMine.mc.player != null && PacketMine.mc.world != null) {
            if (((Boolean) this.array.getValue()).booleanValue() && !this.positions.isEmpty()) {
                if (BlockUtil.canBreak((BlockPos) this.positions.get(0))) {
                    if (PacketMine.mc.player.getDistanceSq((BlockPos) this.positions.get(0)) >= 10.0D) {
                        this.idkNigga = 0;
                        this.positions.clear();
                        return;
                    }

                    if (PacketMine.mc.world.getBlockState((BlockPos) this.positions.get(0)).getBlock() != Blocks.AIR && this.idkNigga == 0) {
                        PacketMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, (BlockPos) this.positions.get(0), EnumFacing.DOWN));
                        PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, (BlockPos) this.positions.get(0), EnumFacing.DOWN));
                        this.idkNigga = 1;
                    }

                    if (PacketMine.mc.world.getBlockState((BlockPos) this.positions.get(0)).getBlock() == Blocks.AIR) {
                        this.positions.remove(0);
                        this.colorTimer.reset();
                        this.fadeTimer.reset();
                        this.idkNigga = 0;
                    }
                } else {
                    this.positions.remove(0);
                }
            }

        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (this.isToggled()) {
            if (!this.positions.contains(event.pos)) {
                this.positions.add(event.pos);
            }

            if (PacketMine.mc.player == null || PacketMine.mc.world == null) {
                return;
            }

            if (event.getStage() == 3 && PacketMine.mc.world.getBlockState(event.pos).getBlock() instanceof BlockEndPortalFrame) {
                PacketMine.mc.world.getBlockState(event.pos).getBlock().setHardness(50.0F);
            }

            if (event.getStage() == 4 && ((Boolean) this.tweaks.getValue()).booleanValue() && BlockUtil.canBreak(event.pos)) {
                if (((Boolean) this.reset.getValue()).booleanValue()) {
                    PacketMine.mc.playerController.isHittingBlock = false;
                }

                if (this.currentPos == null) {
                    this.currentPos = event.pos;
                    this.currentBlockState = PacketMine.mc.world.getBlockState(this.currentPos);
                    this.colorTimer.reset();
                    this.fadeTimer.reset();
                }

                if (!((Boolean) this.array.getValue()).booleanValue()) {
                    PacketMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, event.pos, event.facing));
                    PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                }

                event.setCanceled(true);
            }
        }

    }

    public void onRender(float partialTicks) {
        if (((Boolean) this.render.getValue()).booleanValue() && this.currentPos != null) {
            if (PacketMine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR && !((Boolean) this.array.getValue()).booleanValue()) {
                this.currentPos = null;
                this.currentBlockState = null;
                this.colorTimer.reset();
                this.fadeTimer.reset();
            } else {
                Color color;

                if (!((Boolean) this.overrideRender.value).booleanValue()) {
                    if (((PacketMine.ColorMode) this.colorMode.getValue()).equals(PacketMine.ColorMode.WHITE)) {
                        color = new Color(255, 255, 255, 255);
                    } else {
                        color = new Color(0, 0, 0, 255);
                    }
                } else {
                    color = new Color(((Integer) this.red.value).intValue(), ((Integer) this.green.value).intValue(), ((Integer) this.blue.value).intValue(), ((Integer) this.alpha.value).intValue());
                }

                double addValueX = 1.0D;
                double addValueY = 1.0D;
                double addValueZ = 1.0D;
                double addValueX2 = 0.0D;
                double addValueZ2 = 0.0D;

                if (this.currentBlockState.getBlock().fullBlock) {
                    addValueX = 1.0D;
                    addValueY = 1.0D;
                    addValueZ = 1.0D;
                } else if (this.currentBlockState.getBlock() != Blocks.ENDER_CHEST && this.currentBlockState.getBlock() != Blocks.CHEST && this.currentBlockState.getBlock() != Blocks.TRAPPED_CHEST) {
                    if (this.currentBlockState.getBlock() == Blocks.STONE_SLAB || this.currentBlockState.getBlock() == Blocks.WOODEN_SLAB) {
                        addValueY = 0.5D;
                    }
                } else {
                    addValueX = 0.94D;
                    addValueY = 0.87D;
                    addValueZ = 0.94D;
                    addValueX2 = 0.06D;
                    addValueZ2 = 0.06D;
                }

                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glLineWidth(4.0F);
                AxisAlignedBB bb = new AxisAlignedBB((double) this.currentPos.getX() + addValueX2 - PacketMine.mc.getRenderManager().viewerPosX, (double) this.currentPos.getY() - PacketMine.mc.getRenderManager().viewerPosY, (double) this.currentPos.getZ() + addValueZ2 - PacketMine.mc.getRenderManager().viewerPosZ, (double) this.currentPos.getX() + addValueX - PacketMine.mc.getRenderManager().viewerPosX, (double) this.currentPos.getY() + addValueY - PacketMine.mc.getRenderManager().viewerPosY, (double) this.currentPos.getZ() + addValueZ - PacketMine.mc.getRenderManager().viewerPosZ);

                if (((Boolean) this.overrideRender.getValue()).booleanValue()) {
                    if (((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.FULL) || ((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.SOLID)) {
                        RenderUtil.drawBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRGB());
                    }

                    if (((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.FULL) || ((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.OUTLINE)) {
                        RenderUtil.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRGB());
                    }

                    if (((Boolean) this.doubleRender.getValue()).booleanValue()) {
                        GL11.glLineWidth(2.0F);
                        if (((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.FULL) || ((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.OUTLINE)) {
                            RenderUtil.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (new Color(((Integer) this.red2.getValue()).intValue(), ((Integer) this.green2.getValue()).intValue(), ((Integer) this.blue2.getValue()).intValue(), ((Integer) this.alpha2.getValue()).intValue())).getRGB());
                        }
                    }
                } else if (!((Boolean) this.overrideRender.getValue()).booleanValue() && this.colorMode.getValue() == PacketMine.ColorMode.TIMED) {
                    if (((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.FULL) || ((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.SOLID)) {
                        RenderUtil.drawBox(bb, (new Color(this.colorTimer.passedMs((long) (2000.0F * Floppa.serverManager.getTpsFactor())) ? 0 : 255, this.colorTimer.passedMs((long) (2000.0F * Floppa.serverManager.getTpsFactor())) ? 255 : 0, 0, 70)).getRGB());
                    }

                    if (((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.FULL) || ((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.OUTLINE)) {
                        RenderUtil.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (new Color(this.colorTimer.passedMs((long) (2000.0F * Floppa.serverManager.getTpsFactor())) ? 0 : 255, this.colorTimer.passedMs((long) (2000.0F * Floppa.serverManager.getTpsFactor())) ? 255 : 0, 0, 255)).getRGB());
                    }
                } else if (!((Boolean) this.overrideRender.getValue()).booleanValue()) {
                    if (((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.FULL) || ((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.SOLID)) {
                        RenderUtil.drawBox(bb, (new Color(color.getRed(), color.getGreen(), color.getBlue(), 75)).getRGB());
                    }

                    if (((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.FULL) || ((PacketMine.RenderMode) this.renderMode.getValue()).equals(PacketMine.RenderMode.OUTLINE)) {
                        RenderUtil.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRGB());
                    }
                }

                GL11.glDisable(2848);
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }

    }

    private boolean lambda$new$9(Integer s) {
        return ((Boolean) this.overrideRender.value).booleanValue() && ((Boolean) this.doubleRender.getValue()).booleanValue();
    }

    private boolean lambda$new$8(Integer s) {
        return ((Boolean) this.overrideRender.value).booleanValue() && ((Boolean) this.doubleRender.getValue()).booleanValue();
    }

    private boolean lambda$new$7(Integer s) {
        return ((Boolean) this.overrideRender.value).booleanValue() && ((Boolean) this.doubleRender.getValue()).booleanValue();
    }

    private boolean lambda$new$6(Integer s) {
        return ((Boolean) this.overrideRender.value).booleanValue() && ((Boolean) this.doubleRender.getValue()).booleanValue();
    }

    private boolean lambda$new$5(Boolean s) {
        return ((Boolean) this.overrideRender.getValue()).booleanValue();
    }

    private boolean lambda$new$4(Integer s) {
        return ((Boolean) this.overrideRender.value).booleanValue();
    }

    private boolean lambda$new$3(Integer s) {
        return ((Boolean) this.overrideRender.value).booleanValue();
    }

    private boolean lambda$new$2(Integer s) {
        return ((Boolean) this.overrideRender.value).booleanValue();
    }

    private boolean lambda$new$1(Integer s) {
        return ((Boolean) this.overrideRender.value).booleanValue();
    }

    private boolean lambda$new$0(PacketMine.ColorMode s) {
        return !((Boolean) this.overrideRender.value).booleanValue();
    }

    static enum RenderMode {

        FULL, OUTLINE, SOLID;
    }

    static enum ColorMode {

        WHITE, BLACK, TIMED;
    }
}
