package mod.drinking.my.events;

import mod.drinking.my.DrinkingMod;
import mod.drinking.my.sipcount.PlayerSips;
import mod.drinking.my.sipcount.PlayerSipsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DrinkingMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModEvents {

    @SubscribeEvent
    public static void onCraftAddSip(PlayerEvent.ItemCraftedEvent event){
        event.getEntity().getCapability(PlayerSipsProvider.PLAYER_SIPS).ifPresent(sips -> {
                sips.add_sips(1);
                event.getEntity().sendSystemMessage(Component.literal("Adding Crafting Sip :)"));
        });
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
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSips.class);
    }
}


