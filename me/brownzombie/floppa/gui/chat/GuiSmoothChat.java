package me.brownzombie.floppa.gui.chat;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import me.brownzombie.floppa.managers.MessageManager;
import me.brownzombie.floppa.modules.client.Chat;
import me.brownzombie.floppa.util.AnimationUtil;
import me.brownzombie.floppa.util.ColorUtil;
import me.brownzombie.floppa.util.RenderUtil;
import me.brownzombie.floppa.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class GuiSmoothChat extends GuiNewChat {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft mc;
    private final Timer messageTimer = new Timer();
    private final ColorUtil colorUtil = new ColorUtil();
    private final List sentMessages = Lists.newArrayList();
    private final List chatLines = Lists.newArrayList();
    private final List drawnChatLines = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;
    public static float percentComplete = 0.0F;
    public static int newLines;
    public static long prevMillis = -1L;
    public static int messageAdd;
    public boolean configuring;

    public GuiSmoothChat(Minecraft mcIn) {
        super(mcIn);
        this.mc = mcIn;
    }

    public static void updatePercentage(long diff) {
        if (GuiSmoothChat.percentComplete < 1.0F) {
            GuiSmoothChat.percentComplete += 0.004F * (float) diff;
        }

        GuiSmoothChat.percentComplete = AnimationUtil.clamp(GuiSmoothChat.percentComplete, 0.0F, 1.0F);
    }

    public void drawChat(int updateCounter) {
        if (!this.configuring) {
            if (GuiSmoothChat.prevMillis == -1L) {
                GuiSmoothChat.prevMillis = System.currentTimeMillis();
            } else {
                long current = System.currentTimeMillis();
                long diff = current - GuiSmoothChat.prevMillis;

                GuiSmoothChat.prevMillis = current;
                updatePercentage(diff);
                float t = GuiSmoothChat.percentComplete;
                float percent = 1.0F - --t * t * t * t;

                percent = AnimationUtil.clamp(percent, 0.0F, 1.0F);
                if (this.mc.gameSettings.chatVisibility != EnumChatVisibility.HIDDEN) {
                    int i = this.getLineCount();
                    int j = this.drawnChatLines.size();
                    float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

                    if (j > 0) {
                        boolean flag = false;

                        if (this.getChatOpen()) {
                            flag = true;
                        }

                        float f1 = this.getChatScale();
                        int k = MathHelper.ceil((float) this.getChatWidth() / f1);

                        GlStateManager.pushMatrix();
                        if (((Boolean) Chat.INSTANCE.smoothChat.getValue()).booleanValue() && ((Chat.RenderMode) Chat.INSTANCE.renderMode.getValue()).equals(Chat.RenderMode.HORIZONTAL) && !this.isScrolled) {
                            GlStateManager.translate(2.0F + ((Float) Chat.INSTANCE.xOffset.getValue()).floatValue(), 8.0F + ((Float) Chat.INSTANCE.yOffset.getValue()).floatValue() + (9.0F - 9.0F * percent) * f1, 0.0F);
                        } else {
                            GlStateManager.translate(2.0F + ((Float) Chat.INSTANCE.xOffset.getValue()).floatValue(), 8.0F + ((Float) Chat.INSTANCE.yOffset.getValue()).floatValue(), 0.0F);
                        }

                        GlStateManager.scale(f1, f1, 1.0F);
                        int l = 0;

                        int k2;
                        int i3;
                        int k3;

                        for (k2 = 0; k2 + this.scrollPos < this.drawnChatLines.size() && k2 < i; ++k2) {
                            ChatLine l2 = (ChatLine) this.drawnChatLines.get(k2 + this.scrollPos);

                            if (l2 != null) {
                                i3 = updateCounter - l2.getUpdatedCounter();
                                if (i3 < 200 || flag) {
                                    double j3 = (double) i3 / 200.0D;

                                    j3 = 1.0D - j3;
                                    j3 *= 10.0D;
                                    j3 = MathHelper.clamp(j3, 0.0D, 1.0D);
                                    j3 *= j3;
                                    k3 = (int) (255.0D * j3);
                                    if (flag) {
                                        k3 = 255;
                                    }

                                    k3 = (int) ((float) k3 * f);
                                    ++l;
                                    if (k3 > 3) {
                                        byte l3 = 0;
                                        int j2 = -k2 * 9;

                                        if (!((Boolean) Chat.INSTANCE.clearChat.getValue()).booleanValue()) {
                                            drawRect(-2, j2 - 9, l3 + k + 4, j2, k3 / 2 << 24);
                                        }

                                        String s = l2.getChatComponent().getFormattedText();

                                        GlStateManager.enableBlend();
                                        if (((Boolean) Chat.INSTANCE.smoothChat.getValue()).booleanValue() && k2 <= GuiSmoothChat.newLines) {
                                            if (this.messageTimer.passedMs((long) ((Integer) Chat.INSTANCE.vSpeed.getValue()).intValue()) && GuiSmoothChat.messageAdd < 0) {
                                                GuiSmoothChat.messageAdd += ((Integer) Chat.INSTANCE.vIncrements.getValue()).intValue();
                                                if (GuiSmoothChat.messageAdd > 0) {
                                                    GuiSmoothChat.messageAdd = 0;
                                                }

                                                this.messageTimer.reset();
                                            }

                                            if (((Boolean) Chat.INSTANCE.rainbowSuffix.getValue()).booleanValue() && s.contains(MessageManager.prefix)) {
                                                RenderUtil.drawRainbowString(s, 0.0F + (float) (((Chat.RenderMode) Chat.INSTANCE.renderMode.getValue()).equals(Chat.RenderMode.VERTICAL) ? GuiSmoothChat.messageAdd : 0), (float) (j2 - 8), this.colorUtil.getRainbow(((Integer) Chat.INSTANCE.suffixSpeed.getValue()).intValue(), ((Integer) Chat.INSTANCE.suffixSat.getValue()).intValue(), ((Integer) Chat.INSTANCE.suffixBright.getValue()).intValue()).getRGB(), 100.0F, true);
                                            } else {
                                                this.mc.fontRenderer.drawStringWithShadow(s, 0.0F + (float) (((Chat.RenderMode) Chat.INSTANCE.renderMode.getValue()).equals(Chat.RenderMode.VERTICAL) ? GuiSmoothChat.messageAdd : 0), (float) (j2 - 8), 16777215 + ((int) ((float) k3 * (((Chat.RenderMode) Chat.INSTANCE.renderMode.getValue()).equals(Chat.RenderMode.VERTICAL) ? percent : 1.0F)) << 24));
                                            }
                                        } else if (((Boolean) Chat.INSTANCE.rainbowSuffix.getValue()).booleanValue() && s.contains(MessageManager.prefix)) {
                                            RenderUtil.drawRainbowString(s, (float) l3, (float) (j2 - 8), this.colorUtil.getRainbow(((Integer) Chat.INSTANCE.suffixSpeed.getValue()).intValue(), ((Integer) Chat.INSTANCE.suffixSat.getValue()).intValue(), ((Integer) Chat.INSTANCE.suffixBright.getValue()).intValue()).getRGB(), 100.0F, true);
                                        } else {
                                            this.mc.fontRenderer.drawStringWithShadow(s, (float) l3, (float) (j2 - 8), 16777215 + (k3 << 24));
                                        }

                                        GlStateManager.disableAlpha();
                                        GlStateManager.disableBlend();
                                    }
                                }
                            }
                        }

                        if (flag) {
                            k2 = this.mc.fontRenderer.FONT_HEIGHT;
                            GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                            int i = j * k2 + j;

                            i3 = l * k2 + l;
                            int j = this.scrollPos * i3 / j;
                            int k1 = i3 * i3 / i;

                            if (i != i3) {
                                k3 = j > 0 ? 170 : 96;
                                int k = this.isScrolled ? 13382451 : 3355562;

                                drawRect(0, -j, 2, -j - k1, k + (k3 << 24));
                                drawRect(2, -j, 1, -j - k1, 13421772 + (k3 << 24));
                            }
                        }

                        GlStateManager.popMatrix();
                    }
                }

            }
        }
    }

    public void clearChatMessages(boolean p_146231_1_) {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        if (p_146231_1_) {
            this.sentMessages.clear();
        }

    }

    public void printChatMessage(ITextComponent chatComponent) {
        this.printChatMessageWithOptionalDeletion(chatComponent, 0);
    }

    public void printChatMessageWithOptionalDeletion(ITextComponent chatComponent, int chatLineId) {
        GuiSmoothChat.percentComplete = 0.0F;
        this.setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
        GuiSmoothChat.LOGGER.info("[CHAT] {}", chatComponent.getUnformattedText().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
    }

    private void setChatLine(ITextComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        GuiSmoothChat.messageAdd = -((Integer) Chat.INSTANCE.vLength.getValue()).intValue();
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }

        int i = MathHelper.floor((float) this.getChatWidth() / this.getChatScale());
        List list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRenderer, false, false);
        boolean flag = this.getChatOpen();

        GuiSmoothChat.newLines = list.size() - 1;

        ITextComponent itextcomponent;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); this.drawnChatLines.add(0, new ChatLine(updateCounter, itextcomponent, chatLineId))) {
            itextcomponent = (ITextComponent) iterator.next();
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
        }

        while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }

        if (!displayOnly) {
            this.chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));

            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }

    }

    public void refreshChat() {
        this.drawnChatLines.clear();
        this.resetScroll();

        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            ChatLine chatline = (ChatLine) this.chatLines.get(i);

            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }

    }

    public List getSentMessages() {
        return this.sentMessages;
    }

    public void addToSentMessages(String message) {
        if (this.sentMessages.isEmpty() || !((String) this.sentMessages.get(this.sentMessages.size() - 1)).equals(message)) {
            this.sentMessages.add(message);
        }

    }

    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    public void scroll(int amount) {
        this.scrollPos += amount;
        int i = this.drawnChatLines.size();

        if (this.scrollPos > i - this.getLineCount()) {
            this.scrollPos = i - this.getLineCount();
        }

        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }

    }

    @Nullable
    public ITextComponent getChatComponent(int mouseX, int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        } else {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i = scaledresolution.getScaleFactor();
            float f = this.getChatScale();
            int j = mouseX / i - 2 - ((Float) Chat.INSTANCE.xOffset.getValue()).intValue();
            int k = mouseY / i - 40 + ((Float) Chat.INSTANCE.yOffset.getValue()).intValue();

            j = MathHelper.floor((float) j / f);
            k = MathHelper.floor((float) k / f);
            if (j >= 0 && k >= 0) {
                int l = Math.min(this.getLineCount(), this.drawnChatLines.size());

                if (j <= MathHelper.floor((float) this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRenderer.FONT_HEIGHT * l + l) {
                    int i1 = k / this.mc.fontRenderer.FONT_HEIGHT + this.scrollPos;

                    if (i1 >= 0 && i1 < this.drawnChatLines.size()) {
                        ChatLine chatline = (ChatLine) this.drawnChatLines.get(i1);
                        int j1 = 0;
                        Iterator iterator = chatline.getChatComponent().iterator();

                        while (iterator.hasNext()) {
                            ITextComponent itextcomponent = (ITextComponent) iterator.next();

                            if (itextcomponent instanceof TextComponentString) {
                                j1 += this.mc.fontRenderer.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString) itextcomponent).getText(), false));
                                if (j1 > j) {
                                    return itextcomponent;
                                }
                            }
                        }
                    }

                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }

    public void deleteChatLine(int id) {
        Iterator iterator = this.drawnChatLines.iterator();

        ChatLine chatline1;

        while (iterator.hasNext()) {
            chatline1 = (ChatLine) iterator.next();
            if (chatline1.getChatLineID() == id) {
                iterator.remove();
            }
        }

        iterator = this.chatLines.iterator();

        while (iterator.hasNext()) {
            chatline1 = (ChatLine) iterator.next();
            if (chatline1.getChatLineID() == id) {
                iterator.remove();
                break;
            }
        }

    }

    public int getChatWidth() {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight() {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float scale) {
        boolean i = true;
        boolean j = true;

        return MathHelper.floor(scale * 280.0F + 40.0F);
    }

    public static int calculateChatboxHeight(float scale) {
        boolean i = true;
        boolean j = true;

        return MathHelper.floor(scale * 160.0F + 20.0F);
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}
