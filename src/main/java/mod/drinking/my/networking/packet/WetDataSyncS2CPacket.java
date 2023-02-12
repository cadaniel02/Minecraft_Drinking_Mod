package mod.drinking.my.networking.packet;

import mod.drinking.my.client.ClientSipData;
import mod.drinking.my.client.ClientWetData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WetDataSyncS2CPacket {
    private final boolean wet;
    private static final String MESSAGE_RESET_SIPS = "message.drinkingmod.reset_sips";
    public WetDataSyncS2CPacket(boolean wet){
        this.wet = wet;
    }
    public WetDataSyncS2CPacket(FriendlyByteBuf buf){
        this.wet = buf.readBoolean();

    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(wet);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
        //HERE WE ARE ON CLIENT
            ClientWetData.setWet(wet);
        });

        return true;
    }
}
