package io.github.bnnuycorps.oasisbar.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.bnnuycorps.oasisbar.thirst.ThirstManager;
import io.github.bnnuycorps.oasisbar.thirst.access.ThirstManagerAccess;
import io.github.bnnuycorps.oasisbar.thirst.identifier.HudTexturesIdentifiers;
import io.github.bnnuycorps.oasisbar.thirst.registry.ConfigRegistry;
import io.github.bnnuycorps.oasisbar.thirst.registry.EffectRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;


@Environment(EnvType.CLIENT)
public class ThirstHud {
    public static void renderThirstHud(MatrixStack matrixStack, MinecraftClient client, PlayerEntity playerEntity, int ticks) {

        // Get the client width and height
        if (playerEntity != null && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
            int width = client.getWindow().getScaledWidth() / 2;
            int height = client.getWindow().getScaledHeight();
            int bounceFactor = 0;

            // Defining the texture
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, HudTexturesIdentifiers.THIRST_ICONS);

            // Get the ThirstManager for the playerEntity
            ThirstManager thirstManager = ((ThirstManagerAccess) playerEntity).getThirstManager();

            // Get the thirstValue
            int thirstValue = thirstManager.getThirstLevel();

            // If the player currently is in a hot biome for enough time or in The Nether
            int hotXFactor = 0;
            int hotYFactor = 0;
            if (playerEntity.getWorld().getRegistryKey() == World.NETHER && ConfigRegistry.CONFIG.nether_drains_more_thirst || thirstManager.getIsInHotBiomeForEnoughtTime()) {
                if (ConfigRegistry.CONFIG.hot_thirst_bar_texture == 1) {
                    hotYFactor = 9;
                } else if (ConfigRegistry.CONFIG.hot_thirst_bar_texture == 2) {
                    hotXFactor = 36;
                }
            }

            // If the player currently has the thirst effect
            int thirstEffectFactor = 0;
            if (playerEntity.hasStatusEffect(EffectRegistry.THIRST)) {
                thirstEffectFactor = 18;
                hotXFactor = 0;
            }


            // Create the Thirst Bar
            // Empty Thirst
            for (int i = 0; i < 10; i++) {
                bounceFactor = getBounceFactor(playerEntity, ticks, thirstManager);
                DrawContext.drawTexture(matrixStack,
                        (width + 82 - (i * 9) + i) + ConfigRegistry.CONFIG.hud_x,
                        (height - 49 + bounceFactor) + ConfigRegistry.CONFIG.hud_y,
                        0 + hotYFactor,
                        0,
                        9,
                        9,
                        256,
                        256);
            }

            // Half Thirst
            for (int i = 0; i < 20; i++) {
                if (thirstValue != 0) {
                    if (((thirstValue + 1) / 2) > i) {
                        bounceFactor = getBounceFactor(playerEntity, ticks, thirstManager);
                        DrawContext.drawTexture(matrixStack,
                                (width + 82 - (i * 9) + i) + ConfigRegistry.CONFIG.hud_x,
                                (height - 49 + bounceFactor) + ConfigRegistry.CONFIG.hud_y,
                                9 + thirstEffectFactor + hotXFactor,
                                9,
                                9,
                                9,
                                256,
                                256);
                    } else {
                        break;
                    }
                }
            }

            // Full Thirst
            for (int i = 0; i < 20; i++) {
                if (thirstValue != 0) {
                    if ((thirstValue / 2) > i) {
                        bounceFactor = getBounceFactor(playerEntity, ticks, thirstManager);
                        DrawContext.drawTexture(matrixStack,
                                (width + 82 - (i * 9) + i) + ConfigRegistry.CONFIG.hud_x,
                                (height - 49 + bounceFactor) + ConfigRegistry.CONFIG.hud_y,
                                0 + thirstEffectFactor + hotXFactor,
                                9,
                                9,
                                9,
                                256,
                                256);
                    } else {
                        break;
                    }
                }
            }
            RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
        }
    }

    // Bounce factor
    private static int getBounceFactor(PlayerEntity playerEntity, int ticks, ThirstManager thirstManager) {
        if (thirstManager.dehydrationLevel >= 4.0F && ticks % (thirstManager.thirstLevel * 3 + 1) == 0) {
            return playerEntity.getWorld().random.nextInt(3) - 1;
        } else if (ticks % (thirstManager.thirstLevel * 8 + 3) == 0) {
            return playerEntity.getWorld().random.nextInt(5) - 1;
        } else {
            return 0;
        }
    }
}
