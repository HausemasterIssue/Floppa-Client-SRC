package me.brownzombie.floppa.modules.movement;

import java.lang.reflect.Field;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.Mappings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;

public class BigJump extends Module {

    public Setting force = new Setting("Force", Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(10.0D));
    private BlockPos oldPos;

    public BigJump() {
        super("BigJump", "Makes you jump really high.", Module.Category.MOVEMENT);
    }

    public void onEnable() {
        this.oldPos = new BlockPos(BigJump.mc.player.posX, BigJump.mc.player.posY, BigJump.mc.player.posZ);
        this.setTimer(50.0F);
        BigJump.mc.player.jump();
    }

    public void onDisable() {
        this.setTimer(1.0F);
    }

    public void onUpdate() {
        if (BigJump.mc.player != null && BigJump.mc.world != null) {
            if (BigJump.mc.player.posY > (double) this.oldPos.getY() + 1.04D) {
                BigJump.mc.player.motionY = ((Double) this.force.getValue()).doubleValue();
                BigJump.mc.player.connection.sendPacket(new Position(BigJump.mc.player.posX, 1337.0D, BigJump.mc.player.posZ, false));
            }

            this.toggle();
        }

    }

    private void setTimer(float value) {
        try {
            Field e = Minecraft.class.getDeclaredField(Mappings.timer);

            e.setAccessible(true);
            Field tickLength = Timer.class.getDeclaredField(Mappings.tickLength);

            tickLength.setAccessible(true);
            tickLength.setFloat(e.get(BigJump.mc), 50.0F / value);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}
