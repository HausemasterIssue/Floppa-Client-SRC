package me.brownzombie.floppa.modules.hud.elements;

import java.awt.Color;
import java.util.Iterator;
import me.brownzombie.floppa.gui.hudeditor.HudEditor;
import me.brownzombie.floppa.managers.NotificationManager;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import me.brownzombie.floppa.modules.hud.NotificationsModule;
import me.brownzombie.floppa.util.GuiUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Notifications extends HudModuleTemplate {

    private ResourceLocation notifIcon = new ResourceLocation("minecraft:images/notification.png");

    public Notifications() {
        super("Notifications");
    }

    public void onUpdate() {
        if (Notifications.mc.player != null && Notifications.mc.world != null) {
            this.stringVal = "Floppa 1.0";
            this.width = Notifications.mc.fontRenderer.getStringWidth(this.stringVal);
        }

    }

    public void onRenderOverlay(float partialTicks) {
        if (Notifications.mc.currentScreen instanceof HudEditor && NotificationManager.Get().Notifications.isEmpty()) {
            String itr1 = "Notifications";

            this.width = Notifications.mc.fontRenderer.getStringWidth("Notifications");
            this.height = Notifications.mc.fontRenderer.FONT_HEIGHT;
            Notifications.mc.fontRenderer.drawStringWithShadow("Notifications", (float) this.x, (float) this.y, 16777215);
        } else {
            Iterator itr = NotificationManager.Get().Notifications.iterator();
            float notifY = (float) this.y;

            float maxWidth;

            for (maxWidth = 0.0F; itr.hasNext(); notifY -= 13.0F) {
                NotificationManager.Notification notification = (NotificationManager.Notification) itr.next();

                if (((NotificationsModule.NotifMode) NotificationsModule.INSTANCE.notifModeSetting.getValue()).equals(NotificationsModule.NotifMode.COMPACT) && itr.hasNext()) {
                    NotificationManager.Get().Notifications.remove(notification);
                }

                if (notification.IsDecayed()) {
                    NotificationManager.Get().Notifications.remove(notification);
                }

                float width = (float) Notifications.mc.fontRenderer.getStringWidth(notification.GetDescription()) + 1.5F;

                if (((NotificationsModule.NotifRenderMode) NotificationsModule.INSTANCE.notifRenderModeSetting.getValue()).equals(NotificationsModule.NotifRenderMode.SIMPLE)) {
                    GuiUtil.drawRect((float) this.x - 1.5F, notifY, (float) this.x + width + 15.0F, notifY + 13.0F, (new Color(30, 30, 30, 150)).getRGB(), 7, 2.0F);
                } else {
                    GuiUtil.drawRoundedRect((float) this.x - 1.5F, notifY, (float) this.x + width + 15.0F, notifY + 13.0F, 3.0F, (new Color(30, 30, 30, 150)).getRGB());
                }

                Notifications.mc.fontRenderer.drawStringWithShadow(notification.GetDescription(), (float) (this.x + 15), notifY + (float) notification.GetY() + 2.0F, 16777215);
                Notifications.mc.renderEngine.bindTexture(this.notifIcon);
                GlStateManager.color(255.0F, 255.0F, 255.0F);
                Gui.drawScaledCustomSizeModalRect(this.x, this.y + 1, 7.0F, 7.0F, 243, 43, 10, 10, 250.0F, 50.0F);
                if (width >= maxWidth) {
                    maxWidth = width;
                }
            }

            this.height = 10;
            this.width = (int) maxWidth;
        }
    }
}
