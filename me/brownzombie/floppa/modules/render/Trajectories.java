package me.brownzombie.floppa.modules.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

public class Trajectories extends Module {

    public Setting sliceCount = new Setting("Slices", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20));
    public Setting lineWidthSetting = new Setting("Line Width", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F));
    public Setting topRadiusSetting = new Setting("Top Radius", Float.valueOf(0.3F), Float.valueOf(0.1F), Float.valueOf(1.0F));
    public Setting baseRadiusSetting = new Setting("Base Radius", Float.valueOf(0.6F), Float.valueOf(0.1F), Float.valueOf(1.0F));
    public Setting customColorSetting = new Setting("Custom Colors", Boolean.valueOf(false));
    public Setting red = new Setting("Red", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.customColorSetting.getValue()).booleanValue();
    });
    public Setting green = new Setting("Green", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.customColorSetting.getValue()).booleanValue();
    });
    public Setting blue = new Setting("Blue", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.customColorSetting.getValue()).booleanValue();
    });
    public Setting entityRed = new Setting("EntityRed", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.customColorSetting.getValue()).booleanValue();
    });
    public Setting entityGreen = new Setting("EntityGreen", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.customColorSetting.getValue()).booleanValue();
    });
    public Setting entityBlue = new Setting("EntityBlue", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(255), (c) -> {
        return ((Boolean) this.customColorSetting.getValue()).booleanValue();
    });

    public Trajectories() {
        super("Trajectories", "Shows where throwables will land.", Module.Category.RENDER);
    }

    public void onRender(float partialTicks) {
        if (Trajectories.mc.world != null && Trajectories.mc.player != null && Trajectories.mc.getRenderManager() != null) {
            double renderPosX = Trajectories.mc.player.lastTickPosX + (Trajectories.mc.player.posX - Trajectories.mc.player.lastTickPosX) * (double) partialTicks;
            double renderPosY = Trajectories.mc.player.lastTickPosY + (Trajectories.mc.player.posY - Trajectories.mc.player.lastTickPosY) * (double) partialTicks;
            double renderPosZ = Trajectories.mc.player.lastTickPosZ + (Trajectories.mc.player.posZ - Trajectories.mc.player.lastTickPosZ) * (double) partialTicks;

            Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND);
            if (Trajectories.mc.gameSettings.thirdPersonView == 0 && (Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFishingRod || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnderPearl || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEgg || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSnowball || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle)) {
                GL11.glPushMatrix();
                Item item = Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem();
                double posX = renderPosX - (double) (MathHelper.cos(Trajectories.mc.player.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
                double posY = renderPosY + (double) Trajectories.mc.player.getEyeHeight() - 0.1000000014901161D;
                double posZ = renderPosZ - (double) (MathHelper.sin(Trajectories.mc.player.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
                double motionX = (double) (-MathHelper.sin(Trajectories.mc.player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(Trajectories.mc.player.rotationPitch / 180.0F * 3.1415927F)) * (item instanceof ItemBow ? 1.0D : 0.4D);
                double motionY = (double) (-MathHelper.sin(Trajectories.mc.player.rotationPitch / 180.0F * 3.1415927F)) * (item instanceof ItemBow ? 1.0D : 0.4D);
                double motionZ = (double) (MathHelper.cos(Trajectories.mc.player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(Trajectories.mc.player.rotationPitch / 180.0F * 3.1415927F)) * (item instanceof ItemBow ? 1.0D : 0.4D);
                int i = 72000 - Trajectories.mc.player.getItemInUseCount();
                float power = (float) i / 20.0F;

                power = (power * power + power * 2.0F) / 3.0F;
                if (power > 1.0F) {
                    power = 1.0F;
                }

                float distance = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);

                motionX /= (double) distance;
                motionY /= (double) distance;
                motionZ /= (double) distance;
                float pow = item instanceof ItemBow ? power * 2.0F : (item instanceof ItemFishingRod ? 1.25F : (Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE ? 0.9F : 1.0F));

                motionX *= (double) (pow * (item instanceof ItemFishingRod ? 0.75F : (Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE ? 0.75F : 1.5F)));
                motionY *= (double) (pow * (item instanceof ItemFishingRod ? 0.75F : (Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE ? 0.75F : 1.5F)));
                motionZ *= (double) (pow * (item instanceof ItemFishingRod ? 0.75F : (Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE ? 0.75F : 1.5F)));
                this.enableGL3D(((Float) this.lineWidthSetting.getValue()).floatValue());
                GlStateManager.color(((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.red.getValue()).intValue() / 255.0F : 0.0F, ((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.green.getValue()).intValue() / 255.0F : 1.0F, ((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.blue.getValue()).intValue() / 255.0F : 0.0F, 1.0F);
                GL11.glEnable(2848);
                float size = (float) (item instanceof ItemBow ? 0.3D : 0.25D);
                boolean hasLanded = false;
                Entity landingOnEntity = null;
                RayTraceResult landingPosition = null;

                while (!hasLanded && posY > 0.0D) {
                    Vec3d side = new Vec3d(posX, posY, posZ);
                    Vec3d c = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
                    RayTraceResult possibleLandingStrip = Trajectories.mc.world.rayTraceBlocks(side, c, false, true, false);

                    if (possibleLandingStrip != null && possibleLandingStrip.typeOfHit != Type.MISS) {
                        landingPosition = possibleLandingStrip;
                        hasLanded = true;
                    }

                    AxisAlignedBB arrowBox = new AxisAlignedBB(posX - (double) size, posY - (double) size, posZ - (double) size, posX + (double) size, posY + (double) size, posZ + (double) size);
                    List entities = this.getEntitiesWithinAABB(arrowBox.offset(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
                    Iterator motionAdjustment = entities.iterator();

                    while (motionAdjustment.hasNext()) {
                        Object entity = motionAdjustment.next();
                        Entity boundingBox = (Entity) entity;

                        if (boundingBox.canBeCollidedWith() && boundingBox != Trajectories.mc.player) {
                            float f = 0.3F;
                            AxisAlignedBB axisalignedbb = boundingBox.getEntityBoundingBox().expand(0.30000001192092896D, 0.30000001192092896D, 0.30000001192092896D);
                            RayTraceResult possibleEntityLanding = axisalignedbb.calculateIntercept(side, c);

                            if (possibleEntityLanding != null) {
                                hasLanded = true;
                                landingOnEntity = boundingBox;
                                landingPosition = possibleEntityLanding;
                            }
                        }
                    }

                    if (landingOnEntity != null) {
                        GlStateManager.color(((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.entityRed.getValue()).intValue() / 255.0F : 1.0F, ((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.entityGreen.getValue()).intValue() / 255.0F : 0.0F, ((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.entityBlue.getValue()).intValue() / 255.0F : 0.0F, 1.0F);
                    }

                    posX += motionX;
                    posY += motionY;
                    posZ += motionZ;
                    float motionAdjustment1 = 0.99F;

                    motionX *= 0.9900000095367432D;
                    motionY *= 0.9900000095367432D;
                    motionZ *= 0.9900000095367432D;
                    motionY -= item instanceof ItemBow ? 0.05D : 0.03D;
                    this.drawLine3D(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
                }

                if (landingPosition != null && landingPosition.typeOfHit == Type.BLOCK) {
                    GlStateManager.translate(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
                    int side1 = landingPosition.sideHit.getIndex();

                    if (side1 == 2) {
                        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    } else if (side1 == 3) {
                        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    } else if (side1 == 4) {
                        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                    } else if (side1 == 5) {
                        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                    }

                    Cylinder c1 = new Cylinder();

                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                    c1.setDrawStyle(100011);
                    if (landingOnEntity != null) {
                        GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
                        GL11.glLineWidth(2.5F);
                        c1.draw(((Float) this.baseRadiusSetting.getValue()).floatValue(), ((Float) this.topRadiusSetting.getValue()).floatValue(), 0.0F, ((Integer) this.sliceCount.getValue()).intValue(), 1);
                        GL11.glLineWidth(0.1F);
                        GlStateManager.color(((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.entityRed.getValue()).intValue() / 255.0F : 1.0F, ((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.entityGreen.getValue()).intValue() / 255.0F : 0.0F, ((Boolean) this.customColorSetting.getValue()).booleanValue() ? (float) ((Integer) this.entityBlue.getValue()).intValue() / 255.0F : 0.0F, 1.0F);
                    }

                    c1.draw(((Float) this.baseRadiusSetting.getValue()).floatValue(), ((Float) this.topRadiusSetting.getValue()).floatValue(), 0.0F, ((Integer) this.sliceCount.getValue()).intValue(), 1);
                }

                this.disableGL3D();
                GL11.glPopMatrix();
            }
        }

    }

    public void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Trajectories.mc.entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public void drawLine3D(double d0, double d1, double d2) {
        GL11.glVertex3d(d0, d1, d2);
    }

    private List getEntitiesWithinAABB(AxisAlignedBB bb) {
        ArrayList list = new ArrayList();
        int chunkMinX = MathHelper.floor((bb.minX - 2.0D) / 16.0D);
        int chunkMaxX = MathHelper.floor((bb.maxX + 2.0D) / 16.0D);
        int chunkMinZ = MathHelper.floor((bb.minZ - 2.0D) / 16.0D);
        int chunkMaxZ = MathHelper.floor((bb.maxZ + 2.0D) / 16.0D);

        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (Trajectories.mc.world.getChunkProvider().getLoadedChunk(x, z) != null) {
                    Trajectories.mc.world.getChunk(x, z).getEntitiesWithinAABBForEntity(Trajectories.mc.player, bb, list, (com.google.common.base.Predicate) null);
                }
            }
        }

        return list;
    }
}
