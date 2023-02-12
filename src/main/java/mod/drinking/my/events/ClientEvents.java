package mod.drinking.my.events;

import mod.drinking.my.DrinkingMod;
import mod.drinking.my.client.ClientSipData;
import mod.drinking.my.networking.ModMessages;
import mod.drinking.my.networking.packet.ResetSipsC2SPacket;
import mod.drinking.my.util.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import mod.drinking.my.client.DrinkHUD;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = DrinkingMod.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.RESET_KEY.consumeClick()) {
                if (ClientSipData.getPlayerSips() > 0 || DrinkHUD.drinking){
                        DrinkHUD.drinkThisMany -= 1;
                    if(DrinkHUD.drinkThisMany <= 0) {
                        DrinkHUD.drinkThisMany = ClientSipData.getPlayerSips();
                        DrinkHUD.drinking = !DrinkHUD.drinking;
                    }
                    if(DrinkHUD.drinking) {
                        ClientSipData.set(0, ClientSipData.getTotalSips());
                        ModMessages.sendToServer(new ResetSipsC2SPacket());
                    }
                }
            }
        }
    }



    @Mod.EventBusSubscriber(modid = DrinkingMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.RESET_KEY);
        }
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("thirst", DrinkHUD.HUD_DRINK);
        }
    }
}