package mod.drinking.my.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.drinking.my.networking.ModMessages;
import mod.drinking.my.networking.packet.SipDataSyncC2SPacket;
import mod.drinking.my.sipcount.PlayerSipsProvider;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;


import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraft.resources.ResourceLocation;
import mod.drinking.my.DrinkingMod;
import net.minecraft.client.gui.GuiComponent;

public class DrinkHUD {

    private static int sipCount;
    private static final ResourceLocation FILLED_THIRST = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/filled_thirst.png");

    private static final ResourceLocation NO_SIPS = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/has_sips2.png");
    private static final ResourceLocation BOTTLE = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/has_sips1.png");

    private static final ResourceLocation EMPTY_THIRST = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/empty_thirst.png");

    public static final IGuiOverlay HUD_DRINK = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;




        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);



            if(ClientSipData.getPlayerSips() == 0){
                RenderSystem.setShaderTexture(0, NO_SIPS);
                GuiComponent.blit(poseStack,width - width/5 -25 , height/4,0,0,100,75,
                        100,75);


            }
            else{
                RenderSystem.setShaderTexture(0, BOTTLE);
                GuiComponent.blit(poseStack,width - width/5 -25 , height/4,0,0,100,75,
                        100,75);
            }

        int curSips = ClientSipData.getPlayerSips();

        //Single digits
        if(curSips< 10){
            int textSize = 14;
            //files has number one higher than glyph
            int numName = curSips+1;
            ResourceLocation number = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/num" + numName +  ".png");
            RenderSystem.setShaderTexture(0, number);
            GuiComponent.blit(poseStack,width - width/5 + 7 , height/4 + 29,0,0,textSize,textSize,
                    textSize,textSize);
        }
        else if(curSips < 100){
            int textSize = 14;
            //files has number one higher than glyph

            int numName1 = (curSips / 10) + 1;
            int numName2 = (curSips % 10) + 1;


            //digit 1 render
            ResourceLocation number = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/num" + numName1 +  ".png");
            RenderSystem.setShaderTexture(0, number);
            GuiComponent.blit(poseStack,width - width/5 + 3 , height/4 + 29,0,0,textSize,textSize,
                    textSize,textSize);

            //digit 2 render
            ResourceLocation number2 = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/num" + numName2 +  ".png");
            RenderSystem.setShaderTexture(0, number2);
            GuiComponent.blit(poseStack,width - width/5 + 12 , height/4 + 29,0,0,textSize,textSize,
                    textSize,textSize);
        }

//        Font font1 = Minecraft.getInstance().font;
//
//        String curSipsStr = Integer.toString(ClientSipData.getPlayerSips());
//
//        font1.draw(poseStack, curSipsStr, width - width/6, height/3 +15, 0);

        });
    }

