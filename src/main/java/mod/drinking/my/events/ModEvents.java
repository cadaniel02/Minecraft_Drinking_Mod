package mod.drinking.my.events;

import ca.weblite.objc.Client;
import mod.drinking.my.DrinkingMod;
import mod.drinking.my.client.ClientSipData;
import mod.drinking.my.client.ClientWetData;
import mod.drinking.my.networking.ModMessages;
import mod.drinking.my.networking.packet.SipDataSyncC2SPacket;
import mod.drinking.my.networking.packet.SipDataSyncS2CPacket;
import mod.drinking.my.sipcount.PlayerSips;
import mod.drinking.my.sipcount.PlayerSipsProvider;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;





@Mod.EventBusSubscriber(modid = DrinkingMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModEvents {

    @SubscribeEvent
    public static void onAdvancementAddSip(AdvancementEvent.AdvancementEarnEvent event){
        Player player = event.getEntity();
        Level level = player.level;

        if (level.isClientSide()){
            ClientSipData.add(1);
            ModMessages.sendToServer(new SipDataSyncC2SPacket(ClientSipData.getPlayerSips(), ClientSipData.getTotalSips()));
        }
    }
    @SubscribeEvent
    public static void onCraftAddSip(PlayerEvent.ItemCraftedEvent event){
            Player player = event.getEntity();
            Level level = player.level;

            if (level.isClientSide()){
                ClientSipData.add(1);
                ModMessages.sendToServer(new SipDataSyncC2SPacket(ClientSipData.getPlayerSips(), ClientSipData.getTotalSips()));
            }
    }
    @SubscribeEvent
    public static void onEnterWaterAddSip(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER) {
            Player player = event.player;
            Level level = player.level;

            if (player.isInWater() && !ClientWetData.isWet()) {
                player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                    ClientSipData.add(1);
                    ModMessages.sendToServer(new SipDataSyncC2SPacket(ClientSipData.getPlayerSips(), ClientSipData.getTotalSips()));
                });
            }
            ClientWetData.setWet(event.player.isInWater() || (ClientWetData.isWet() && hasWaterUnderThem(player, level)));
        }
    }

    @SubscribeEvent
    public static void onTakeDamage(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER) {
            Player player = event.player;
            Level level = player.level;

            if (ClientSipData.getPlayerSips() > 0 && event.player.getRandom().nextFloat() < 0.005f) {
                    player.attack(player);
                    level.playSound(null, player.getOnPos(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5f, level.random.nextFloat() * 0.1f + 0.9F);
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerSipsProvider.PLAYER_SIPS).isPresent()) {
                event.addCapability(new ResourceLocation(DrinkingMod.MODID, "properties"), new PlayerSipsProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                    ModMessages.sendToPlayer(new SipDataSyncS2CPacket(sips.get_sips(), sips.get_totalSips()), player);
                });
            }
        }
    }
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSips.class);
    }

    private static boolean hasWaterUnderThem(Player player, Level level) {
        int blockX = (int) Math.floor(player.getX());

        int blockY = (int) Math.floor(player.getY()) - 1;

        int blockZ = (int) Math.floor(player.getZ());
        BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
        return level.getBlockState(blockPos).is(Blocks.WATER);
    }
}





