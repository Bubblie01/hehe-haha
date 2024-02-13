package io.github.bnnuycorps.oasisbar.Thirst;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.bnnuycorps.oasisbar.Thirst.inits.ConfigInit;
import io.github.bnnuycorps.oasisbar.Thirst.inits.EffectInit;
import io.github.bnnuycorps.oasisbar.Thirst.inits.RenderInit;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.GUI;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstTooltipData;
import io.github.bnnuycorps.oasisbar.Thirst.items.Canteen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ThirstHudRender {

    // Could implement HudRenderCallback
    public static void renderThirstHud(DrawContext context, MinecraftClient client, PlayerEntity playerEntity, int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount, float flashAlpha,
            float otherFlashAlpha) {
        if (playerEntity != null && !playerEntity.isInvulnerable()) {
            ThirstManager thirstManager = ((ThirstManagerInt) playerEntity).getThirstManager();
            if (thirstManager.hasThirst()) {
                int thirst = thirstManager.getThirstLevel();
                int variable_one;
                int variable_two;
                int variable_three;
                int height = scaledHeight - 49;
                int width = scaledWidth / 2 + 91;
                if (vehicleHeartCount == 0) {

                    ItemStack itemStack = null;
                    if (ConfigInit.CONFIG.thirst_preview && thirst < 20) {
                        if (!playerEntity.getMainHandStack().isEmpty() && !playerEntity.getMainHandStack().getTooltipData().isEmpty()
                                && playerEntity.getMainHandStack().getTooltipData().get() instanceof ThirstTooltipData) {
                            itemStack = playerEntity.getMainHandStack();
                        } else if (!playerEntity.getOffHandStack().isEmpty() && !playerEntity.getOffHandStack().getTooltipData().isEmpty()
                                && playerEntity.getOffHandStack().getTooltipData().get() instanceof ThirstTooltipData) {
                            itemStack = playerEntity.getOffHandStack();
                        }
                    }
                    if (itemStack != null) {
                        ((GUI) client.inGameHud).setOtherFlashAlpha(otherFlashAlpha += MathHelper.PI / (48F));
                        ((GUI) client.inGameHud).setFlashAlpha((MathHelper.cos(otherFlashAlpha + MathHelper.PI) + 1f) / 2f);
                    } else if (otherFlashAlpha > 0.01F) {
                        ((GUI) client.inGameHud).setOtherFlashAlpha(0.0F);
                        ((GUI) client.inGameHud).setFlashAlpha(0.0F);
                    }

                    // Render ui droplets
                    for (variable_one = 0; variable_one < 10; ++variable_one) {
                        variable_three = height;
                        if (thirstManager.dehydration >= 4.0F && ticks % (thirst * 3 + 1) == 0) {
                            variable_three = height + (playerEntity.getWorld().getRandom().nextInt(3) - 1); // bouncy
                            thirstManager.dehydration -= 4.0F;
                        } else if (ticks % (thirst * 8 + 3) == 0) {
                            variable_three = height + (playerEntity.getWorld().getRandom().nextInt(3) - 1); // bouncy
                        }
                        int uppderCoord = 9;
                        if (ConfigInit.CONFIG.other_droplet_texture) {
                            uppderCoord = uppderCoord + 9;
                        }

                        int beneathCoord = 0;
                        if (playerEntity.hasStatusEffect(EffectInit.DEHYDRATION)) {
                            beneathCoord = 36;
                        }

                        variable_two = width - variable_one * 8 - 9;
                        variable_two = variable_two + ConfigInit.CONFIG.hud_x;
                        variable_three = variable_three + ConfigInit.CONFIG.hud_y;

                        context.drawTexture(RenderInit.DEHYDRATION_ICON, variable_two, variable_three, 0, 0, 9, 9, 256, 256); // Background
                        if (variable_one * 2 + 1 < thirst) {
                            context.drawTexture(RenderInit.DEHYDRATION_ICON, variable_two, variable_three, beneathCoord, uppderCoord, 9, 9, 256, 256); // Big icon
                        }
                        if (variable_one * 2 + 1 == thirst) {
                            context.drawTexture(RenderInit.DEHYDRATION_ICON, variable_two, variable_three, beneathCoord + 9, uppderCoord, 9, 9, 256, 256); // Small icon
                        }
                        // Show item thirst quench
                        if (variable_one >= thirst / 2) {
                            if (itemStack != null) {
                                RenderSystem.enableBlend();
                                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, flashAlpha);
                                int thirstQuench = ((ThirstTooltipData) itemStack.getTooltipData().get()).getThirstQuench();
                                if (itemStack.getItem() instanceof Canteen)
                                    thirstQuench = ConfigInit.CONFIG.flask_thirst_quench;
                                int quality = ((ThirstTooltipData) itemStack.getTooltipData().get()).getDrinkQuality();
                                if (variable_one < (thirst + thirstQuench) / 2) {
                                    context.drawTexture(RenderInit.DEHYDRATION_ICON, variable_two, variable_three, quality * 18, 9, 9, 9, 256, 256);
                                } else if ((thirst + thirstQuench) % 2 != 0 && variable_one < (thirst + thirstQuench) / 2 + 1) {
                                    context.drawTexture(RenderInit.DEHYDRATION_ICON, variable_two, variable_three, quality * 18 + 9, 9, 9, 9, 256, 256);
                                }
                                RenderSystem.disableBlend();
                                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                            }
                        }
                        // Freezing
                        if (playerEntity.getFrozenTicks() > 0) {
                            RenderSystem.enableBlend();
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, playerEntity.getFreezingScale());
                            if (variable_one * 2 + 1 < thirst) {
                                context.drawTexture(RenderInit.DEHYDRATION_ICON, variable_two, variable_three, 54, uppderCoord, 9, 9, 256, 256);
                            }
                            if (variable_one * 2 + 1 == thirst) {
                                context.drawTexture(RenderInit.DEHYDRATION_ICON, variable_two, variable_three, 54 + 9, uppderCoord, 9, 9, 256, 256);
                            }
                            RenderSystem.disableBlend();
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                        }
                    }
                }
            }
        }
    }

}