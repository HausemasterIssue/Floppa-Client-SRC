package me.brownzombie.floppa.gui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import me.brownzombie.floppa.gui.clickgui.part.Frame;
import me.brownzombie.floppa.gui.clickgui.part.PartTemplate;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.modules.client.ClickGuiModule;
import me.brownzombie.floppa.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class ClickGui extends GuiScreen {

    public static ClickGui instance;
    public static ArrayList frames;
    private boolean open;
    private ResourceLocation shader;
    public ColorUtil colorUtil = new ColorUtil();
    public ClickGuiModule cgModule = new ClickGuiModule();
    public Color color;

    public ClickGui() {
        this.color = new Color(((Integer) this.cgModule.red.getValue()).intValue(), ((Integer) this.cgModule.green.getValue()).intValue(), ((Integer) this.cgModule.blue.getValue()).intValue(), ((Integer) this.cgModule.alpha.getValue()).intValue());
        ClickGui.instance = this;
        ClickGui.frames = new ArrayList();
        int frameX = 5;
        Module.Category[] amodule_category = Module.Category.values();
        int i = amodule_category.length;

        for (int j = 0; j < i; ++j) {
            Module.Category category = amodule_category[j];
            Frame frame = new Frame(category);

            frame.setX(frameX);
            ClickGui.frames.add(frame);
            frameX += frame.getWidth() + 1;
        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.open = true;
        this.drawDefaultBackground();
        Iterator iterator = ClickGui.frames.iterator();

        while (iterator.hasNext()) {
            Frame frame = (Frame) iterator.next();

            frame.renderFrame(this.fontRenderer);
            frame.updatePosition(mouseX, mouseY);
            Iterator iterator1 = frame.getComponents().iterator();

            while (iterator1.hasNext()) {
                PartTemplate comp = (PartTemplate) iterator1.next();

                comp.updateComponent(mouseX, mouseY);
            }
        }

        this.shader = new ResourceLocation("minecraft", "shaders/post/blur.json");
        if (!this.mc.entityRenderer.isShaderActive() && ClickGuiModule.blur) {
            this.mc.entityRenderer.loadShader(this.shader);
        }

        if (!ClickGuiModule.blur) {
            this.mc.entityRenderer.stopUseShader();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Iterator iterator = ClickGui.frames.iterator();

        while (iterator.hasNext()) {
            Frame frame = (Frame) iterator.next();

            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
            }

            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 0.1F));
            }

            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                Iterator iterator1 = frame.getComponents().iterator();

                while (iterator1.hasNext()) {
                    PartTemplate component = (PartTemplate) iterator1.next();

                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }

    }

    protected void keyTyped(char typedChar, int keyCode) {
        Iterator iterator = ClickGui.frames.iterator();

        while (iterator.hasNext()) {
            Frame frame = (Frame) iterator.next();

            if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
                Iterator iterator1 = frame.getComponents().iterator();

                while (iterator1.hasNext()) {
                    PartTemplate component = (PartTemplate) iterator1.next();

                    component.keyTyped(typedChar, keyCode);
                }
            }
        }

        if (keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen) null);
        }

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        Iterator iterator = ClickGui.frames.iterator();

        Frame frame;

        while (iterator.hasNext()) {
            frame = (Frame) iterator.next();
            frame.setDrag(false);
        }

        iterator = ClickGui.frames.iterator();

        while (iterator.hasNext()) {
            frame = (Frame) iterator.next();
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                Iterator iterator1 = frame.getComponents().iterator();

                while (iterator1.hasNext()) {
                    PartTemplate component = (PartTemplate) iterator1.next();

                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }

    }

    public void initGui() {}

    public void onGuiClosed() {
        this.open = false;
        this.mc.entityRenderer.stopUseShader();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public static ClickGui getInstance() {
        return ClickGui.instance;
    }

    public void applyBlur() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (this.mc.entityRenderer.shaderGroup != null && !this.open) {
                this.mc.entityRenderer.shaderGroup.deleteShaderGroup();
            }

            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }

    }
}
