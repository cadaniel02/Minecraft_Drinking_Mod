package mod.drinking.my.networking;

import mod.drinking.my.DrinkingMod;

import mod.drinking.my.networking.packet.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id(){
        return packetId++;
    }

    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(DrinkingMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;
        net.messageBuilder(ResetSipsC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ResetSipsC2SPacket::new)
                .encoder(ResetSipsC2SPacket::toBytes)
                .consumerMainThread(ResetSipsC2SPacket::handle)
                .add();

        net.messageBuilder(SipDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SipDataSyncS2CPacket::new)
                .encoder(SipDataSyncS2CPacket::toBytes)
                .consumerMainThread(SipDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(WetDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(WetDataSyncS2CPacket::new)
                .encoder(WetDataSyncS2CPacket::toBytes)
                .consumerMainThread(WetDataSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
