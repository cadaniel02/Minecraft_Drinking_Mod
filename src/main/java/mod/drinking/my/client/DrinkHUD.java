package mod.drinking.my.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class DrinkHUD {
    public static final IGuiOverlay HUD_DRINK = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    });
}
