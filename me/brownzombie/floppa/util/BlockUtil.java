package me.brownzombie.floppa.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BlockUtil {

    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final List blackList = Arrays.asList(new Block[] { Blocks.TALLGRASS, Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR});
    public static final List shulkerList = Arrays.asList(new Block[] { Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX});

    public static BlockPos getPlayerPositionFloored(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static boolean canBreak(BlockPos pos) {
        IBlockState blockState = BlockUtil.mc.world.getBlockState(pos);
        Block block = blockState.getBlock();

        return block.getBlockHardness(blockState, BlockUtil.mc.world, pos) != -1.0F;
    }

    public static boolean placeBlock(BlockPos blockPos, boolean offhand, boolean rotate) {
        if (!checkCanPlace(blockPos)) {
            return false;
        } else {
            EnumFacing placeSide = getPlaceSide(blockPos);
            BlockPos adjacentBlock = blockPos.offset(placeSide);
            EnumFacing opposingSide = placeSide.getOpposite();

            if (!BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(adjacentBlock), false)) {
                return false;
            } else {
                boolean isSneak = false;

                if (BlockUtil.blackList.contains(BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock()) || BlockUtil.shulkerList.contains(BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock())) {
                    BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, Action.START_SNEAKING));
                    isSneak = true;
                }

                Vec3d hitVector = getHitVector(adjacentBlock, opposingSide);

                if (rotate) {
                    float[] actionHand = getLegitRotations(hitVector);

                    BlockUtil.mc.player.connection.sendPacket(new Rotation(actionHand[0], actionHand[1], BlockUtil.mc.player.onGround));
                }

                EnumHand actionHand1 = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

                BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, adjacentBlock, opposingSide, hitVector, actionHand1);
                BlockUtil.mc.player.connection.sendPacket(new CPacketAnimation(actionHand1));
                if (isSneak) {
                    BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, Action.STOP_SNEAKING));
                }

                return true;
            }
        }
    }

    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = getFirstFacing(pos);

        if (side == null) {
            return isSneaking;
        } else {
            BlockPos neighbour = pos.offset(side);
            EnumFacing opposite = side.getOpposite();
            Vec3d hitVec = (new Vec3d(neighbour)).add(new Vec3d(0.5D, 0.5D, 0.5D)).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
            Block neighbourBlock = BlockUtil.mc.world.getBlockState(neighbour).getBlock();

            if (!BlockUtil.mc.player.isSneaking()) {
                BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, Action.START_SNEAKING));
                BlockUtil.mc.player.setSneaking(true);
                sneaking = true;
            }

            if (rotate) {
                faceVector(hitVec, true);
            }

            rightClickBlock(neighbour, hitVec, hand, opposite, packet);
            BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockUtil.mc.rightClickDelayTimer = 4;
            return sneaking || isSneaking;
        }
    }

    public static void placeBlock(BlockPos pos) {
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumFacing = aenumfacing[j];

            if (!BlockUtil.mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos)) {
                Vec3d vec = new Vec3d((double) pos.getX() + 0.5D + (double) enumFacing.getXOffset() * 0.5D, (double) pos.getY() + 0.5D + (double) enumFacing.getYOffset() * 0.5D, (double) pos.getZ() + 0.5D + (double) enumFacing.getZOffset() * 0.5D);
                float[] old = new float[] { BlockUtil.mc.player.rotationYaw, BlockUtil.mc.player.rotationPitch};

                BlockUtil.mc.player.connection.sendPacket(new Rotation((float) Math.toDegrees(Math.atan2(vec.z - BlockUtil.mc.player.posZ, vec.x - BlockUtil.mc.player.posX)) - 90.0F, (float) (-Math.toDegrees(Math.atan2(vec.y - (BlockUtil.mc.player.posY + (double) BlockUtil.mc.player.getEyeHeight()), Math.sqrt((vec.x - BlockUtil.mc.player.posX) * (vec.x - BlockUtil.mc.player.posX) + (vec.z - BlockUtil.mc.player.posZ) * (vec.z - BlockUtil.mc.player.posZ))))), BlockUtil.mc.player.onGround));
                BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, Action.START_SNEAKING));
                BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
                BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, Action.STOP_SNEAKING));
                BlockUtil.mc.player.connection.sendPacket(new Rotation(old[0], old[1], BlockUtil.mc.player.onGround));
                return;
            }
        }

    }

    public static void placeBlock(BlockPos pos, int slot) {
        if (slot != -1) {
            int prev = BlockUtil.mc.player.inventory.currentItem;

            BlockUtil.mc.player.inventory.currentItem = slot;
            placeBlock(pos);
            BlockUtil.mc.player.inventory.currentItem = prev;
        }
    }

    public static List getPossibleSides(BlockPos pos) {
        ArrayList facings = new ArrayList();
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing side = aenumfacing[j];
            BlockPos neighbour = pos.offset(side);

            if (BlockUtil.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(neighbour), false)) {
                IBlockState blockState = BlockUtil.mc.world.getBlockState(neighbour);

                if (!blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
        }

        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator iterator = getPossibleSides(pos).iterator();

        if (iterator.hasNext()) {
            EnumFacing facing = (EnumFacing) iterator.next();

            return facing;
        } else {
            return null;
        }
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + (double) BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        return new float[] { BlockUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - BlockUtil.mc.player.rotationYaw), BlockUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - BlockUtil.mc.player.rotationPitch)};
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = getLegitRotations(vec);

        BlockUtil.mc.player.connection.sendPacket(new Rotation(rotations[0], normalizeAngle ? (float) MathHelper.normalizeAngle((int) rotations[1], 360) : rotations[1], BlockUtil.mc.player.onGround));
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float) (vec.x - (double) pos.getX());
            float f1 = (float) (vec.y - (double) pos.getY());
            float f2 = (float) (vec.z - (double) pos.getZ());

            BlockUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos, direction, vec, hand);
        }

        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockUtil.mc.rightClickDelayTimer = 4;
    }

    public static boolean isIntercepted(BlockPos pos) {
        Iterator iterator = BlockUtil.mc.world.loadedEntityList.iterator();

        Entity entity;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            entity = (Entity) iterator.next();
        } while (!(new AxisAlignedBB(pos)).intersects(entity.getEntityBoundingBox()));

        return true;
    }

    public static boolean placeBlock(BlockPos blockPos, boolean offhand, boolean rotate, boolean doSwitch, boolean silentSwitch, int toSwitch) {
        if (!checkCanPlace(blockPos)) {
            return false;
        } else {
            EnumFacing placeSide = getPlaceSide(blockPos);
            BlockPos adjacentBlock = blockPos.offset(placeSide);
            EnumFacing opposingSide = placeSide.getOpposite();

            if (!BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(adjacentBlock), false)) {
                return false;
            } else {
                if (doSwitch) {
                    if (silentSwitch) {
                        BlockUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(toSwitch));
                    } else if (BlockUtil.mc.player.inventory.currentItem != toSwitch) {
                        BlockUtil.mc.player.inventory.currentItem = toSwitch;
                    }
                }

                boolean isSneak = false;

                if (BlockUtil.blackList.contains(BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock()) || BlockUtil.shulkerList.contains(BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock())) {
                    BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, Action.START_SNEAKING));
                    isSneak = true;
                }

                Vec3d hitVector = getHitVector(adjacentBlock, opposingSide);

                if (rotate) {
                    float[] actionHand = getLegitRotations(hitVector);

                    BlockUtil.mc.player.connection.sendPacket(new Rotation(actionHand[0], actionHand[1], BlockUtil.mc.player.onGround));
                }

                EnumHand actionHand1 = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

                BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, adjacentBlock, opposingSide, hitVector, actionHand1);
                BlockUtil.mc.player.connection.sendPacket(new CPacketAnimation(actionHand1));
                if (isSneak) {
                    BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, Action.STOP_SNEAKING));
                }

                return true;
            }
        }
    }

    private static EnumFacing getPlaceSide(BlockPos blockPos) {
        EnumFacing placeableSide = null;
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing side = aenumfacing[j];
            BlockPos adjacent = blockPos.offset(side);

            if (BlockUtil.mc.world.getBlockState(adjacent).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(adjacent), false) && !BlockUtil.mc.world.getBlockState(adjacent).getMaterial().isReplaceable()) {
                placeableSide = side;
            }
        }

        return placeableSide;
    }

    public static boolean checkCanPlace(BlockPos pos) {
        if (!(BlockUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockAir) && !(BlockUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) {
            return false;
        } else {
            Iterator iterator = BlockUtil.mc.world.getEntitiesWithinAABBExcludingEntity((Entity) null, new AxisAlignedBB(pos)).iterator();

            Entity entity;

            do {
                if (!iterator.hasNext()) {
                    return getPlaceSide(pos) != null;
                }

                entity = (Entity) iterator.next();
            } while (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow);

            return false;
        }
    }

    private static Vec3d getHitVector(BlockPos pos, EnumFacing opposingSide) {
        return (new Vec3d(pos)).add(new Vec3d(0.5D, 0.5D, 0.5D)).add((new Vec3d(opposingSide.getDirectionVec())).scale(0.5D));
    }
}
