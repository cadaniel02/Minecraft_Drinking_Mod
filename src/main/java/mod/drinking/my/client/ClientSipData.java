package mod.drinking.my.client;

import ca.weblite.objc.Client;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.drinking.my.DrinkingMod;
import mod.drinking.my.sipcount.PlayerSips;
import mod.drinking.my.sipcount.PlayerSipsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientSipData {
    private static int playerSips;
    private static int totalSips;
    private static boolean takingSip = false;


    public static void set(int sips, int totalSips) {
        ClientSipData.playerSips = sips;
        ClientSipData.totalSips = totalSips;

    }

    public static void triggerSipPrompt(){
        takingSip = true;
    }

    public static boolean getSipStatus(){
        return takingSip;
    }

    public static void add(int add){
        playerSips += add;
        totalSips += add;
        takingSip = true;
        DrinkHUD.opacity = 1;
    }

    public static int getPlayerSips() {
        return playerSips;
    }

    public static int getTotalSips() {
        return totalSips;
    }
}