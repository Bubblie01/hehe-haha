package io.github.bnnuycorps.oasisbar.mixin;

import io.github.bnnuycorps.oasisbar.Main;
import io.github.bnnuycorps.oasisbar.Thirst.inits.ConfigInit;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstTooltipData;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HoneyBottleItem.class)
public abstract class HoneyBottleItemMixin extends Item {

    public HoneyBottleItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    public void finishUsingMixin(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (user instanceof PlayerEntity player) {
            int thirstQuench = 0;
            for (int i = 0; i < Main.HYDRATION_TEMPLATES.size(); i++) {
                if (Main.HYDRATION_TEMPLATES.get(i).containsItem(stack.getItem())) {
                    thirstQuench = Main.HYDRATION_TEMPLATES.get(i).getHydration();
                    break;
                }
            }
            if (thirstQuench == 0)
                thirstQuench = ConfigInit.CONFIG.honey_quench;
            ((ThirstManagerInt) player).getThirstManager().add(thirstQuench);
        }
    }

    // check here and for milk and potion
    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        if (ConfigInit.CONFIG.thirst_preview) {
            int thirstQuench = 0;
            for (int i = 0; i < Main.HYDRATION_TEMPLATES.size(); i++) {
                if (Main.HYDRATION_TEMPLATES.get(i).containsItem(stack.getItem())) {
                    thirstQuench = Main.HYDRATION_TEMPLATES.get(i).getHydration();
                    break;
                }
            }
            if (thirstQuench == 0) {
                thirstQuench = ConfigInit.CONFIG.honey_quench;
            }
            return Optional.of(new ThirstTooltipData(0, thirstQuench));
        }
        return super.getTooltipData(stack);
    }
}
