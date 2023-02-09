package mod.drinking.my.networking.packet;

import mod.drinking.my.sipcount.PlayerSipsProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SipDataSyncC2SPacket {
    private final int sips;
    private final int totalsips;
    private static final String MESSAGE_RESET_SIPS = "message.drinkingmod.reset_sips";
    public SipDataSyncC2SPacket(int sips, int totalsips){
        this.sips = sips;
        this.totalsips = totalsips;
    }
    public SipDataSyncC2SPacket(FriendlyByteBuf buf){
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
// HERE WE ARE ON THE SERVER!

            ServerPlayer player = context.getSender();

            player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                sips.set_sips(this.sips, this.totalsips);

                player.sendSystemMessage(Component.literal("Current Sips " + sips.get_sips() + "\nTotal Sips: " + sips.get_totalSips())
                        .withStyle(ChatFormatting.BLUE));
            });
        });
        return true;
    }
}
