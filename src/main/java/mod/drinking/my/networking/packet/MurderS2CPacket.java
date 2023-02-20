package mod.drinking.my.networking.packet;

import mod.drinking.my.client.ClientSipData;
import mod.drinking.my.client.DrinkHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MurderS2CPacket {
    private static final String MESSAGE_RESET_SIPS = "message.drinkingmod.reset_sips";
    public MurderS2CPacket(){

    }
    public MurderS2CPacket(FriendlyByteBuf buf){

    }
    public void toBytes(FriendlyByteBuf buf){

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
        //HERE WE ARE ON CLIENT
            DrinkHUD.murderOpacity = 1.0f;
        });

        return true;
    }
}
