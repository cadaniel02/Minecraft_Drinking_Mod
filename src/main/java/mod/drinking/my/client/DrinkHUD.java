package mod.drinking.my.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;


import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraft.resources.ResourceLocation;
import mod.drinking.my.DrinkingMod;
import net.minecraft.client.gui.GuiComponent;

public class DrinkHUD {

    public static int drinkThisMany;
    public static boolean drinking = false;
    public static float opacity = 1.0f;
    private static final ResourceLocation FILLED_THIRST = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/filled_thirst.png");

    private static final ResourceLocation NO_SIPS = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/has_sips2.png");
    private static final ResourceLocation BOTTLE = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/has_sips1.png");
    private static final ResourceLocation PRESS_L = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/press_l.png");

    private static final ResourceLocation LIFETIME = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/total.png");
    private static final ResourceLocation TAKE = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/take.png");

    private static final ResourceLocation SIPS = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/sips.png");
    private static final ResourceLocation EMPTY_THIRST = new ResourceLocation(DrinkingMod.MODID,
            "textures/drinks/empty_thirst.png");


    public static final IGuiOverlay HUD_DRINK = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);


        sipTriggerText(poseStack, width);


        int totalX = width - width/5 + 11;
        int totalY = height/4 + 12;
        int size = 7;
        int number = ClientSipData.getTotalSips();


        if(drinking) {
            totalX = width - width/2 - 25;
            totalY = 84;
            size = 52;
            number = drinkThisMany;
            drawTotalSipAmount(poseStack, size, totalX, totalY, number);
            drawTakeText(poseStack, width, height);
        }
        else {
            drawTotalSipAmount(poseStack, size, totalX, totalY, number);
            drawLifetimeText(poseStack, width, height);

            drawSipBox(poseStack, width, height);
            drawCurSipAmount(poseStack, width, height);
        }


        });

    private static void drawTakeText(PoseStack poseStack, int width, int height){
        int promptSize = 200;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TAKE);
        GuiComponent.blit(poseStack,width/4 - 100 , 10,0,0,promptSize,promptSize,
                promptSize,promptSize);
        RenderSystem.setShaderTexture(0, SIPS);
        GuiComponent.blit(poseStack,width/4 + 100 , 10,0,0,promptSize,promptSize,
                promptSize,promptSize);
    }

    private static void drawLifetimeText(PoseStack poseStack, int width, int height) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, LIFETIME);
        GuiComponent.blit(poseStack,width - width/5 - 8 , height/7 + 16,0,0,80,60,
                80,60);
    }
    private static void drawTotalSipAmount(PoseStack poseStack, int size, int width, int height, int totalSips){
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if(totalSips< 10){
            int textSize = size;
            //files has number one higher than glyph
            int numName = totalSips+1;
            ResourceLocation number = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/whitenums" + numName +  ".png");
            RenderSystem.setShaderTexture(0, number);
            GuiComponent.blit(poseStack,width , height,0,0,textSize,textSize,
                    textSize,textSize);
        }
        else if(totalSips < 100){
            int textSize = size;
            //files has number one higher than glyph

            int numName1 = (totalSips / 10) + 1;
            int numName2 = (totalSips % 10) + 1;

            //digit 1 render
            ResourceLocation number = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/whitenums" + numName1 +  ".png");
            RenderSystem.setShaderTexture(0, number);
            GuiComponent.blit(poseStack,width - 4*size/7 , height,0,0,textSize,textSize,
                    textSize,textSize);

            //digit 2 render
            ResourceLocation number2 = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/whitenums" + numName2 +  ".png");
            RenderSystem.setShaderTexture(0, number2);
            GuiComponent.blit(poseStack,width , height,0,0,textSize,textSize,
                    textSize,textSize);
        }
        else{
            int textSize = size;
            //files has number one higher than glyph
            int numName0 = (totalSips / 100) + 1;
            int numName1 = ((totalSips - (100 *(totalSips/100))) / 10) + 1;
            int numName2 = (totalSips % 10) + 1;


            //digit 0 render
            ResourceLocation number = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/whitenums" + numName0 +  ".png");
            RenderSystem.setShaderTexture(0, number);
            GuiComponent.blit(poseStack,width + 7 , height,0,0,textSize,textSize,
                    textSize,textSize);

            //digit 1 render
            ResourceLocation number1 = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/whitenums" + numName1 +  ".png");
            RenderSystem.setShaderTexture(0, number1);
            GuiComponent.blit(poseStack,width + 2 , height,0,0,textSize,textSize,
                    textSize,textSize);

            //digit 2 render
            ResourceLocation number2 = new ResourceLocation(DrinkingMod.MODID,
                    "textures/nums/whitenums" + numName2 +  ".png");
            RenderSystem.setShaderTexture(0, number2);
            GuiComponent.blit(poseStack,width + 7 , height,0,0,textSize,textSize,
                    textSize,textSize);


        }
    }
    private static void drawSipBox(PoseStack poseStack, int width, int height){
        if(ClientSipData.getPlayerSips() == 0){
            RenderSystem.setShaderTexture(0, NO_SIPS);
            GuiComponent.blit(poseStack,width - width/5 -25 , height/4,0,0,100,75,
                    100,75);

        }
        else{
            RenderSystem.setShaderTexture(0, BOTTLE);
            GuiComponent.blit(poseStack,width - width/5 -25 , height/4,0,0,100,75,
                    100,75);
            RenderSystem.setShaderTexture(0, PRESS_L);
            GuiComponent.blit(poseStack,width - width/5 -5 , height/3 + 15,0,0,60,45,
                    60,45);
        }
    }

    private static void drawCurSipAmount(PoseStack poseStack, int width, int height){
        int curSips = ClientSipData.getPlayerSips();
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



    }
    private static void sipTriggerText(PoseStack poseStack, int width){
        if(ClientSipData.getSipStatus()){
            int promptSize = 200;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
            ResourceLocation takeSip = new ResourceLocation(DrinkingMod.MODID,
                    "textures/drinks/takesip.png");
            RenderSystem.setShaderTexture(0, takeSip);
            GuiComponent.blit(poseStack,width/4 + 10 , 30,0,0,promptSize,promptSize,
                    promptSize,promptSize);
            opacity -= 0.01f;
        }
    }
    }

