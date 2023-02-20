package mod.drinking.my.events;

import ca.weblite.objc.Client;
import mod.drinking.my.DrinkingMod;
import mod.drinking.my.client.ClientSipData;
import mod.drinking.my.networking.ModMessages;
import mod.drinking.my.networking.packet.SipDataSyncC2SPacket;
import mod.drinking.my.networking.packet.SipDataSyncS2CPacket;
import mod.drinking.my.sipcount.PlayerSips;
import mod.drinking.my.sipcount.PlayerSipsProvider;
import mod.drinking.my.client.DrinkHUD;
import mod.drinking.my.networking.ModMessages;
import mod.drinking.my.networking.packet.MurderS2CPacket;
import mod.drinking.my.networking.packet.SipDataSyncS2CPacket;
import mod.drinking.my.sipcount.PlayerSips;
import mod.drinking.my.sipcount.PlayerSipsProvider;

import mod.drinking.my.wetdata.PlayerWet;
import mod.drinking.my.wetdata.PlayerWetProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;


@Mod.EventBusSubscriber(modid = DrinkingMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModEvents {

    @SubscribeEvent
    public static void onAdvancementAddSip(AdvancementEvent.AdvancementEarnEvent event) {
        Player entity = event.getEntity();

        if(entity instanceof ServerPlayer player){
            player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                sips.add_sips(1);
                ModMessages.sendToPlayer(new SipDataSyncS2CPacket(sips.get_sips(), sips.get_totalSips()), player);
            });
        }
    }
    @SubscribeEvent
    public static void onMurderAddSip(LivingDeathEvent event){
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                ModMessages.sendToPlayer(new MurderS2CPacket(), player);
            });
        }
    }
    @SubscribeEvent
    public static void onCraftAddSip(PlayerEvent.ItemCraftedEvent event){
        Player entity = event.getEntity();

        if(entity instanceof ServerPlayer player){
            player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                if(sips.get_timer() <= 0) {
                    sips.add_sips(1);
                    ModMessages.sendToPlayer(new SipDataSyncS2CPacket(sips.get_sips(), sips.get_totalSips()), player);
                }
            });
        }
    }
    @SubscribeEvent
    public static void onEnterWaterAddSip(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER && event.phase.toString().equals("START")) {
            if(event.player instanceof ServerPlayer player) {
                player.getCapability(PlayerWetProvider.IS_WET).ifPresent(wet -> {
                    if (player.isInWater() && !wet.is_wet()){
                        player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                            if(sips.get_timer() <= 0) {
                                sips.add_sips(1);
                                ModMessages.sendToPlayer(new SipDataSyncS2CPacket(sips.get_sips(), sips.get_totalSips()), player);
                            }
                        });
                    }
                    boolean check = player.isInWater() || (wet.is_wet() && hasWaterUnderThem(player, player.getLevel()));
                    wet.setWet(check);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onTakeDamage(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER) {
            if(event.player instanceof ServerPlayer player) {
                ServerLevel level = player.getLevel();
                player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                    if(sips.get_sips() == 0){
                        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
                        player.removeAllEffects();
                    }
                    else if (sips.get_sips() < 7){
                        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(16);
                    }
                    else if(sips.get_sips() < 10){
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300));
                    }
                    else if (sips.get_sips() < 15){
                        if (event.player.getRandom().nextFloat() < 0.005f) {
                            player.attack(player);
                            level.playSound(null, player.getOnPos(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5f, level.random.nextFloat() * 0.1f + 0.9F);
                        }
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300));
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void decrementSipTimer(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER) {
            if(event.player instanceof ServerPlayer player){
                player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                    sips.dec_timer();
                });
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerSipsProvider.PLAYER_SIPS).isPresent()) {
                event.addCapability(new ResourceLocation(DrinkingMod.MODID, "properties"), new PlayerSipsProvider());
            }
            if(!event.getObject().getCapability(PlayerWetProvider.IS_WET).isPresent()) {
                event.addCapability(new ResourceLocation((DrinkingMod.MODID + '1'), "properties"), new PlayerWetProvider());
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
        event.register(PlayerWet.class);
    }


    private static boolean hasWaterUnderThem(ServerPlayer player, ServerLevel level) {

        int blockX = (int) Math.floor(player.getX());

        int blockY = (int) Math.floor(player.getY()) - 1;

        int blockZ = (int) Math.floor(player.getZ());

        BlockPos blockPosBelow = new BlockPos(blockX, blockY - 1, blockZ);
        BlockPos blockPosOn = new BlockPos(blockX, blockY, blockZ);
        return level.getBlockState(blockPosBelow).getMaterial().isLiquid() || level.getBlockState(blockPosOn).getMaterial().isLiquid();
    }
}





