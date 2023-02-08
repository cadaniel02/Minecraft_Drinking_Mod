package mod.drinking.my.client;

import ca.weblite.objc.Client;
import mod.drinking.my.sipcount.PlayerSips;
import mod.drinking.my.sipcount.PlayerSipsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientBlockData {
    private static Block block;

    public static void set(Block block) {
        ClientBlockData.block = block;
    }

    public static Block getBlock() {
        return block;
    }
}
