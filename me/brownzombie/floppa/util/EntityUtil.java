package me.brownzombie.floppa.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityUtil {

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isntValid(Entity entity, double range) {
        return entity == null || isDead(entity) || entity.equals(EntityUtil.mc.player) || entity instanceof EntityPlayer || EntityUtil.mc.player.getDistanceSq(entity) > MathUtil.square(range);
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isAlive(Entity entity) {
        return isLiving(entity) && !entity.isDead && ((EntityLivingBase) entity).getHealth() > 0.0F;
    }

    public static boolean isDead(Entity entity) {
        return !isAlive(entity);
    }

    public static float getHealth(Entity entity) {
        if (isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;

            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        } else {
            return 0.0F;
        }
    }

    public static float getHealth(Entity entity, boolean absorption) {
        if (isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;

            return livingBase.getHealth() + (absorption ? livingBase.getAbsorptionAmount() : 0.0F);
        } else {
            return 0.0F;
        }
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, (double) ticks));
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }
}
