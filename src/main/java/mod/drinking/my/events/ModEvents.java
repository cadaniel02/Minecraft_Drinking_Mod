package mod.drinking.my.events;

import mod.drinking.my.DrinkingMod;
import mod.drinking.my.client.ClientSipData;
import mod.drinking.my.client.ClientWetData;
import mod.drinking.my.client.DrinkHUD;
import mod.drinking.my.networking.ModMessages;
import mod.drinking.my.networking.packet.SipDataSyncS2CPacket;
import mod.drinking.my.networking.packet.WetDataSyncS2CPacket;
import mod.drinking.my.sipcount.PlayerSips;
import mod.drinking.my.sipcount.PlayerSipsProvider;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

import java.util.Objects;


@Mod.EventBusSubscriber(modid = DrinkingMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModEvents {
    
//    @SubscribeEvent
//    public static void onAdvancementAddSip(AdvancementEvent.AdvancementEarnEvent event){
//        Player player = event.getEntity();
//            if(player instanceof ServerPlayer serverPlayer) {
//                serverPlayer.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
//                    if(!Objects.requireNonNull(event.getAdvancement().getDisplay()).isHidden())
//                        {
//                            sips.add_sips(1);
//                            ModMessages.sendToPlayer(new SipDataSyncS2CPacket(sips.get_sips(), sips.get_totalSips()), serverPlayer);
//                        }
//                });
//        }
//    }

    @SubscribeEvent
    public static void onKillAddSip(LivingDeathEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Monster) {
            if (event.getSource().getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                    sips.add_sips(1);
                    ModMessages.sendToPlayer(new SipDataSyncS2CPacket(sips.get_sips(), sips.get_totalSips()), player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onMurderAddSip(LivingDeathEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (event.getSource().getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                    DrinkHUD.murderOpacity = 1.0f;
                });
            }
        }
    }
    @SubscribeEvent
    public static void onCraftAddSip(PlayerEvent.ItemCraftedEvent event){
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                if (sips.get_timer() <= 0) {
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
                if (player.isInWater() && !ClientWetData.isWet()) {
                    player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                        if(sips.get_timer() <= 0) {
                            sips.add_sips(1);
                            ModMessages.sendToPlayer(new SipDataSyncS2CPacket(sips.get_sips(), sips.get_totalSips()), player);
                        }
                    });
                }
                boolean check = event.player.isInWater() || (ClientWetData.isWet() && hasWaterUnderThem(player, player.getLevel()));
                ModMessages.sendToPlayer(new WetDataSyncS2CPacket(check), player);
            }
        }
    }

    @SubscribeEvent
    public static void onTakeDamage(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER) {
            Player player = event.player;
            Level level = player.level;

            if(ClientSipData.getPlayerSips() == 0){
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
                player.removeAllEffects();
            }
            else if (ClientSipData.getPlayerSips() < 7){
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(16);
            }
            else if(ClientSipData.getPlayerSips() < 10){
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300));
            }
            else if (ClientSipData.getPlayerSips() < 15) {
                if(event.player.getRandom().nextFloat() < 0.005f){
                    player.attack(player);
                    level.playSound(null, player.getOnPos(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5f, level.random.nextFloat() * 0.1f + 0.9F);
                }
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300));
            }
            else if(ClientSipData.getPlayerSips() >= 15){
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300));
                if(event.player.getRandom().nextFloat() < 0.005f){
                    player.attack(player);
                    level.playSound(null, player.getOnPos(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5f, level.random.nextFloat() * 0.1f + 0.9F);
                }
            }
        }

    }

    @SubscribeEvent
    public static void decrementSipTimer(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER) {
            if(event.player instanceof ServerPlayer player){
                player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                    if (sips.get_timer() > 0) {
                        sips.dec_timer();
                    }
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
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(oldStore -> {
                if(event.getEntity() instanceof ServerPlayer player){
                        player.getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(newStore -> {
                            newStore.copyFrom(oldStore);
                        });
                }
            });
            event.getOriginal().invalidateCaps();
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

        int blockY = (int) Math.floor(player.getY());

        int blockZ = (int) Math.floor(player.getZ());
        BlockPos blockPosBelow = new BlockPos(blockX, blockY-1, blockZ);
        BlockPos blockPosOn = new BlockPos(blockX, blockY, blockZ);
        return level.getBlockState(blockPosBelow).is(Blocks.WATER) || level.getBlockState(blockPosOn).is(Blocks.WATER);
    }
}







