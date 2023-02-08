package mod.drinking.my.networking.packet;

import mod.drinking.my.networking.ModMessages;
import mod.drinking.my.sipcount.PlayerSipsProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetSipsC2SPacket {
    private static final String MESSAGE_RESET_SIPS = "message.drinkingmod.reset_sips";
    public ResetSipsC2SPacket(){

    }
    public ResetSipsC2SPacket(FriendlyByteBuf buf){

    }
    public void toBytes(FriendlyByteBuf buf){

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            // Notify the player that they have crafted an item
            player.sendSystemMessage(Component.translatable(MESSAGE_RESET_SIPS).withStyle(ChatFormatting.GOLD));
            //
            level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS,
                    0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            //
            player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                sips.reset_sips();
                player.sendSystemMessage(Component.literal("Current Sips " + sips.get_sips() + "\nTotal Sips: " + sips.get_totalSips())
                        .withStyle(ChatFormatting.YELLOW));
                ModMessages.sendToPlayer(new SipDataSyncS2CPacket(sips.get_sips()), player);
            });
        });
        return true;
    }
}