package me.brownzombie.floppa.util;

import java.text.DecimalFormat;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;

public class ServerManager {

    private float TPS = 20.0F;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private long lastUpdate = -1L;
    private final float[] tpsCounts = new float[10];
    private final DecimalFormat format = new DecimalFormat("##.00#");
    private String serverBrand = "";
    private final Timer timer = new Timer();

    public void onPacketReceived() {
        this.timer.reset();
    }

    public long serverRespondingTime() {
        return this.timer.getPassedTimeMs();
    }

    public void update() {
        long currentTime = System.currentTimeMillis();

        if (this.lastUpdate == -1L) {
            this.lastUpdate = currentTime;
        } else {
            long timeDiff = currentTime - this.lastUpdate;
            float tickTime = (float) timeDiff / 20.0F;

            if (tickTime == 0.0F) {
                tickTime = 50.0F;
            }

            float tps;

            if ((tps = 1000.0F / tickTime) > 20.0F) {
                tps = 20.0F;
            }

            System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
            this.tpsCounts[0] = tps;
            double total = 0.0D;
            float[] afloat = this.tpsCounts;
            int i = afloat.length;

            for (int j = 0; j < i; ++j) {
                float f = afloat[j];

                total += (double) f;
            }

            if ((total /= (double) this.tpsCounts.length) > 20.0D) {
                total = 20.0D;
            }

            this.TPS = Float.parseFloat(this.format.format(total));
            this.lastUpdate = currentTime;
        }
    }

    public float getTpsFactor() {
        return 20.0F / this.TPS;
    }

    public float getTPS() {
        return this.TPS;
    }

    public String getServerBrand() {
        return this.serverBrand;
    }

    public void setServerBrand(String brand) {
        this.serverBrand = brand;
    }

    public int getPing() {
        if (ServerManager.mc.player != null && ServerManager.mc.world != null) {
            try {
                return ((NetHandlerPlayClient) Objects.requireNonNull(ServerManager.mc.getConnection())).getPlayerInfo(ServerManager.mc.getConnection().getGameProfile().getId()).getResponseTime();
            } catch (Exception exception) {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
