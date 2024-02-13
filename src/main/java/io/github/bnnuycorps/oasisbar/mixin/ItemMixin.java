package io.github.bnnuycorps.oasisbar.mixin;

import java.util.Optional;

import io.github.bnnuycorps.oasisbar.Main;
import io.github.bnnuycorps.oasisbar.Thirst.inits.ConfigInit;
import io.github.bnnuycorps.oasisbar.Thirst.inits.TagInit;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstTooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    private void finishUsingMixin(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (!stack.isFood() && user instanceof PlayerEntity player) {
            int thirstQuench = 0;
             if (stack.isIn(TagInit.HYDRATING_FOOD)) {
                thirstQuench = ConfigInit.CONFIG.food_thirst_quench;
            } else if (stack.isIn(TagInit.HYDRATING_DRINKS)) {
                thirstQuench = ConfigInit.CONFIG.drinks_thirst_quench;
            } else if (stack.isIn(TagInit.STRONGER_HYDRATING_STEW)) {
                thirstQuench = ConfigInit.CONFIG.stronger_stew_thirst_quench;
            } else if (stack.isIn(TagInit.STRONGER_HYDRATING_FOOD)) {
                thirstQuench = ConfigInit.CONFIG.stronger_food_thirst_quench;
            } else if (stack.isIn(TagInit.STRONGER_HYDRATING_DRINKS)) {
                thirstQuench = ConfigInit.CONFIG.stronger_drinks_thirst_quench;
            }
            for (int i = 0; i < Main.HYDRATION_TEMPLATES.size(); i++) {
                if (Main.HYDRATION_TEMPLATES.get(i).containsItem(stack.getItem())) {
                    thirstQuench = Main.HYDRATION_TEMPLATES.get(i).getHydration();
                    break;
                }
            }
            if (thirstQuench > 0) {
                ((ThirstManagerInt) player).getThirstManager().add(thirstQuench);
            }
        }
    }

    @Inject(method = "getTooltipData", at = @At("HEAD"), cancellable = true)
    private void getTooltipDataMixin(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> info) {
        if (ConfigInit.CONFIG.thirst_preview) {
            int thirstQuench = 0;
            if (stack.isIn(TagInit.HYDRATING_STEW)) {
                thirstQuench = ConfigInit.CONFIG.stew_thirst_quench;
            } else if (stack.isIn(TagInit.HYDRATING_FOOD)) {
                thirstQuench = ConfigInit.CONFIG.food_thirst_quench;
            } else if (stack.isIn(TagInit.HYDRATING_DRINKS)) {
                thirstQuench = ConfigInit.CONFIG.drinks_thirst_quench;
            } else if (stack.isIn(TagInit.STRONGER_HYDRATING_STEW)) {
                thirstQuench = ConfigInit.CONFIG.stronger_stew_thirst_quench;
            } else if (stack.isIn(TagInit.STRONGER_HYDRATING_FOOD)) {
                thirstQuench = ConfigInit.CONFIG.stronger_food_thirst_quench;
            } else if (stack.isIn(TagInit.STRONGER_HYDRATING_DRINKS)) {
                thirstQuench = ConfigInit.CONFIG.stronger_drinks_thirst_quench;
            }
            for (int i = 0; i < Main.HYDRATION_TEMPLATES.size(); i++) {
                if (Main.HYDRATION_TEMPLATES.get(i).containsItem(stack.getItem())) {
                    thirstQuench = Main.HYDRATION_TEMPLATES.get(i).getHydration();
                    break;
                }
            }

            if (thirstQuench > 0) {
                info.setReturnValue(Optional.of(new ThirstTooltipData(0, thirstQuench)));
            }
        }
    }

}
