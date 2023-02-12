package mod.drinking.my.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_TUTORIAL = "key.category.drinkingmod.tutorial";
    public static final String KEY_RESET_SIPS = "key.drinkingmod.reset_sips";

    public static final KeyMapping RESET_KEY = new KeyMapping(KEY_RESET_SIPS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, KEY_CATEGORY_TUTORIAL);
}