package mod.drinking.my.client;

import net.minecraft.world.level.block.Block;

public class ClientWetData {
    private static boolean wet;

    public static void setWet(boolean wet) {
        ClientWetData.wet = wet;
    }

    public static boolean isWet() {
        return wet;
    }
}
