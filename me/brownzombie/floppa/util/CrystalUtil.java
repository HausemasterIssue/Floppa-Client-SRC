package me.brownzombie.floppa.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class CrystalUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static List possiblePlacePositions(float placeRange, EntityPlayer player) {
        NonNullList positions = NonNullList.create();

        positions.addAll((Collection) getSphere(getPlayerPos(player), placeRange, (int) placeRange, false, true, 0).stream().filter(CrystalUtil::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static List getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList circleblocks = new ArrayList();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();

        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x) {
            for (int z = cz - (int) r; (float) z <= (float) cz + r; ++z) {
                for (int y = sphere ? cy - (int) r : cy; (float) y < (sphere ? (float) cy + r : (float) (cy + h)); ++y) {
                    double dist = (double) ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));

                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);

                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        Block block = CrystalUtil.mc.world.getBlockState(pos).getBlock();

        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            Block floor = CrystalUtil.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            Block ceil = CrystalUtil.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();

            if (floor == Blocks.AIR && ceil == Blocks.AIR) {
                return CrystalUtil.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty() && CrystalUtil.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(pos.add(0, 2, 0))).isEmpty();
            }
        }

        return false;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        if (entity == CrystalUtil.mc.player && CrystalUtil.mc.player.capabilities.isCreativeMode) {
            return 0.0F;
        } else {
            float doubleExplosionSize = 12.0F;
            double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0D;
            Vec3d vec3d = new Vec3d(posX, posY, posZ);
            double blockDensity = 0.0D;

            try {
                blockDensity = (double) entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
            } catch (Exception exception) {
                ;
            }

            double v = (1.0D - distancedsize) * blockDensity;
            float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D));
            double finald = 1.0D;

            if (entity instanceof EntityLivingBase) {
                finald = (double) getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(CrystalUtil.mc.world, (Entity) null, posX, posY, posZ, 6.0F, false, true));
            }

            return (float) finald;
        }
    }

    public static float calculateDamage(BlockPos pos, Entity entity) {
        return calculateDamage((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), entity);
    }

    public static float getDamageMultiplied(float damage) {
        int diff = CrystalUtil.mc.world.getDifficulty().hashCode();

        return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);

            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp((float) k, 0.0F, 20.0F);

            damage *= 1.0F - f / 25.0F;
            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage -= damage / 4.0F;
            }

            return damage;
        } else {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            return damage;
        }
    }

    private List getPlayersSorted(float range) {
        List list = CrystalUtil.mc.world.playerEntities;

        synchronized (CrystalUtil.mc.world.playerEntities) {
            ArrayList playerList = new ArrayList();
            Iterator iterator = CrystalUtil.mc.world.playerEntities.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                if (CrystalUtil.mc.player != player && CrystalUtil.mc.player.getDistance(player) <= range) {
                    playerList.add(player);
                }
            }

            playerList.sort(Comparator.comparing((eP) -> {
                return Float.valueOf(CrystalUtil.mc.player.getDistance(eP));
            }));
            return playerList;
        }
    }
}
