package me.brownzombie.floppa.modules.misc;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class FakePlayer extends Module {

    public static FakePlayer INSTANCE;
    Setting fakePlayerName = new Setting("FakePlayerName", "Trol");
    Entity fakePlayer;

    public FakePlayer() {
        super("FakePlayer", "", Module.Category.PLAYER);
        FakePlayer.INSTANCE = this;
    }

    public void onEnable() {
        if (FakePlayer.mc.world != null) {
            this.fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.randomUUID(), (String) this.fakePlayerName.getValue()));
            FakePlayer.mc.world.addEntityToWorld(this.fakePlayer.getEntityId(), this.fakePlayer);
            this.fakePlayer.copyLocationAndAnglesFrom(FakePlayer.mc.player);
            ((EntityPlayer) this.fakePlayer).inventory.copyInventory(FakePlayer.mc.player.inventory);
        }

    }

    public void onDisable() {
        if (this.fakePlayer != null) {
            FakePlayer.mc.world.removeEntity(this.fakePlayer);
        }

    }

    @SubscribeEvent
    public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
        FakePlayer.mc.world.removeEntity(this.fakePlayer);
        this.toggle();
    }
}
