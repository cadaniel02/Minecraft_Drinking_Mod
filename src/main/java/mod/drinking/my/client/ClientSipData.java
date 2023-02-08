package mod.drinking.my.client;

import mod.drinking.my.sipcount.PlayerSips;
import mod.drinking.my.sipcount.PlayerSipsProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientSipData {
    private static int playerSips;

    public static void set(int sips) {
        ClientSipData.playerSips = sips;
    }

    public static int getPlayerSips() {
        return playerSips;
    }
}