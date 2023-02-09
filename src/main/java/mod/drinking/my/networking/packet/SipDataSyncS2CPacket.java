package mod.drinking.my.networking.packet;

import mod.drinking.my.client.ClientSipData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SipDataSyncS2CPacket {
    private final int sips;
    private final int totalsips;
    private static final String MESSAGE_RESET_SIPS = "message.drinkingmod.reset_sips";
    public SipDataSyncS2CPacket(int sips, int totalsips){
        this.sips = sips;
        this.totalsips = totalsips;
    }
    public SipDataSyncS2CPacket(FriendlyByteBuf buf){
        this.sips = buf.readInt();
        this.totalsips = buf.readInt();

    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(sips);
        buf.writeInt(totalsips);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
        //HERE WE ARE ON CLIENT
            ClientSipData.set(sips, totalsips);

            LocalPlayer player = Minecraft.getInstance().player;
            player.sendSystemMessage(Component.literal("Current Sips " + sips + "\nTotal Sips: " + totalsips)
                    .withStyle(ChatFormatting.YELLOW));

        });

        return true;
    }
}
