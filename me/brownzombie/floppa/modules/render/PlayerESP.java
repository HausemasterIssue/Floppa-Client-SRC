package me.brownzombie.floppa.modules.render;

import java.util.Iterator;
import java.util.List;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.CombatUtil;
import me.brownzombie.floppa.util.EntityUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class PlayerESP extends Module {

    private Setting red = new Setting("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    private Setting green = new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    private Setting blue = new Setting("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    private Setting alpha = new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    private Setting linewidth = new Setting("Red", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(10));
    private Setting mode;
    List playerList;

    public PlayerESP() {
        super("PlayerESP", "", Module.Category.RENDER);
        this.mode = new Setting("Mode", PlayerESP.Mode.ARROW);
        this.playerList = CombatUtil.getPlayersSorted((float) (PlayerESP.mc.gameSettings.renderDistanceChunks * 16));
    }

    public void onRender(float partialTicks) {
        if (PlayerESP.mc.player != null && PlayerESP.mc.world != null) {
            if (PlayerESP.mc.getRenderManager().options == null) {
                return;
            }

            boolean isThirdPersonFrontal = PlayerESP.mc.getRenderManager().options.thirdPersonView == 2;
            float viewerYaw = PlayerESP.mc.getRenderManager().playerViewY;
            Iterator iterator = this.playerList.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                switch ((PlayerESP.Mode) this.mode.value) {
                case BOX:
                    GlStateManager.pushMatrix();
                    Vec3d pos = EntityUtil.getInterpolatedPos(player, partialTicks);

                    GlStateManager.translate(pos.x - PlayerESP.mc.getRenderManager().renderPosX, pos.y - PlayerESP.mc.getRenderManager().renderPosY, pos.z - PlayerESP.mc.getRenderManager().renderPosZ);
                    GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1), 1.0F, 0.0F, 0.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.disableDepth();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
                    if (player instanceof EntityPlayer) {
                        GL11.glColor4f((float) ((Integer) this.red.value).intValue(), (float) ((Integer) this.green.value).intValue(), (float) ((Integer) this.blue.value).intValue(), (float) ((Integer) this.alpha.value).intValue());
                    }

                    GlStateManager.disableTexture2D();
                    GL11.glLineWidth((float) ((Integer) this.linewidth.value).intValue());
                    GL11.glEnable(2848);
                    GL11.glBegin(2);
                    GL11.glVertex2d((double) (-player.width / 2.0F), 0.0D);
                    GL11.glVertex2d((double) (-player.width / 2.0F), (double) player.height);
                    GL11.glVertex2d((double) (player.width / 2.0F), (double) player.height);
                    GL11.glVertex2d((double) (player.width / 2.0F), 0.0D);
                    GL11.glEnd();
                    GlStateManager.popMatrix();
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.disableTexture2D();
                    GlStateManager.enableBlend();
                    GlStateManager.disableAlpha();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    GlStateManager.shadeModel(7425);
                    GlStateManager.disableDepth();
                    GlStateManager.enableCull();
                    GlStateManager.glLineWidth(1.0F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case GLOW:
                    player.setGlowing(true);
                    break;

                case ARROW:
                    GlStateManager.pushMatrix();
                    Vec3d arrowPos = EntityUtil.getInterpolatedPos(player, partialTicks);

                    GlStateManager.translate(arrowPos.x - PlayerESP.mc.getRenderManager().renderPosX, arrowPos.y - PlayerESP.mc.getRenderManager().renderPosY, arrowPos.z - PlayerESP.mc.getRenderManager().renderPosZ);
                    GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1), 1.0F, 0.0F, 0.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.disableDepth();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
                    if (player instanceof EntityPlayer) {
                        GL11.glColor4f((float) ((Integer) this.red.value).intValue(), (float) ((Integer) this.green.value).intValue(), (float) ((Integer) this.blue.value).intValue(), (float) ((Integer) this.alpha.value).intValue());
                    }

                    GlStateManager.disableTexture2D();
                    GL11.glLineWidth((float) ((Integer) this.linewidth.value).intValue());
                    GL11.glEnable(2848);
                    GL11.glBegin(2);
                    GL11.glVertex2d((double) (-player.width / 2.0F), 3.0D);
                    GL11.glVertex2d((double) (-player.width), (double) player.height);
                    GL11.glVertex2d((double) (player.width / 2.0F), 3.0D);
                    GL11.glVertex2d((double) player.width, (double) player.height);
                    GL11.glEnd();
                    GlStateManager.popMatrix();
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.disableTexture2D();
                    GlStateManager.enableBlend();
                    GlStateManager.disableAlpha();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    GlStateManager.shadeModel(7425);
                    GlStateManager.disableDepth();
                    GlStateManager.enableCull();
                    GlStateManager.glLineWidth(1.0F);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }

    }

    static enum Mode {

        ARROW, BOX, GLOW, OUTLINE;
    }
}
