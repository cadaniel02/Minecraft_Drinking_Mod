package mod.drinking.my.networking.packet;

import mod.drinking.my.client.ClientSipData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SipDataSyncS2CPacket {
    private final int sips;
    private static final String MESSAGE_RESET_SIPS = "message.drinkingmod.reset_sips";
    public SipDataSyncS2CPacket(int sips){
        this.sips = sips;
    }
    public SipDataSyncS2CPacket(FriendlyByteBuf buf){
        this.sips = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(sips);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
        //HERE WE ARE ON CLIENT
            ClientSipData.set(sips);
        });
        return true;
    }
}
