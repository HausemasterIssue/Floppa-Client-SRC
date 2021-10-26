package me.brownzombie.floppa.modules.render;

import java.awt.Color;
import me.brownzombie.floppa.event.events.RenderEntityModelEvent;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import me.brownzombie.floppa.util.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.lwjgl.opengl.GL11;

public class CrystalChams extends Module {

    public Setting rainbow = new Setting("Rainbow", Boolean.valueOf(false));
    public Setting renderModel = new Setting("Crystal Model", Boolean.valueOf(false), test<invokedynamic>(this));
    public Setting speed = new Setting("Speed", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(10), test<invokedynamic>(this));
    public Setting saturation = new Setting("Saturation", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting brightness = new Setting("Brightness", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting red = new Setting("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting green = new Setting("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting blue = new Setting("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), test<invokedynamic>(this));
    public Setting alpha = new Setting("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255));
    public Setting lineWidth = new Setting("LineWidth", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(3.0F));
    public Setting crystalScale = new Setting("Size", Float.valueOf(1.0F), Float.valueOf(0.1F), Float.valueOf(5.0F));
    public Setting renderMode;
    public ColorUtil colorUtil;
    public static CrystalChams INSTANCE;

    public CrystalChams() {
        super("CrystalChams", "Crystal and Player CrystalChams.", Module.Category.RENDER);
        this.renderMode = new Setting("Mode", CrystalChams.Mode.FULL);
        this.colorUtil = new ColorUtil();
        CrystalChams.INSTANCE = this;
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (CrystalChams.INSTANCE.isToggled()) {
            if (event.getStage() != 0 || !(event.entity instanceof EntityEnderCrystal)) {
                return;
            }

            if (this.renderMode.getValue() == CrystalChams.Mode.WIREFRAME || this.renderMode.getValue() == CrystalChams.Mode.FULL) {
                Color color = ((Boolean) this.rainbow.getValue()).booleanValue() ? new Color(this.colorUtil.getRainbow(((Integer) this.speed.getValue()).intValue(), ((Integer) this.saturation.getValue()).intValue(), ((Integer) this.brightness.getValue()).intValue()).getRGB()) : new Color(((Integer) this.red.getValue()).intValue(), ((Integer) this.green.getValue()).intValue(), ((Integer) this.blue.getValue()).intValue(), ((Integer) this.alpha.getValue()).intValue());

                CrystalChams.mc.gameSettings.fancyGraphics = false;
                CrystalChams.mc.gameSettings.gammaSetting = 10000.0F;
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GlStateManager.blendFunc(770, 771);
                GlStateManager.color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) ((Integer) this.alpha.getValue()).intValue() / 255.0F);
                GlStateManager.glLineWidth(((Float) this.lineWidth.getValue()).floatValue());
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
        }

    }

    private boolean lambda$new$6(Integer v) {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$5(Integer v) {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$4(Integer v) {
        return !((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$3(Integer v) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$2(Integer v) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$1(Integer v) {
        return ((Boolean) this.rainbow.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Boolean v) {
        return this.renderMode.getValue() == CrystalChams.Mode.WIREFRAME;
    }

    public static enum Mode {

        FULL, WIREFRAME, SOLID;
    }
}
