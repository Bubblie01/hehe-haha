package io.github.bnnuycorps.oasisbar.mixin;

import java.util.Optional;

import io.github.bnnuycorps.oasisbar.Main;
import io.github.bnnuycorps.oasisbar.Thirst.inits.ConfigInit;
import io.github.bnnuycorps.oasisbar.Thirst.inits.EffectInit;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstTooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {

    public MilkBucketItemMixin(Settings settings) {
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
            if (thirstQuench == 0) {
                thirstQuench = ConfigInit.CONFIG.milk_thirst_quench;
            }
            ((ThirstManagerInt) player).getThirstManager().add(thirstQuench);

            if (!world.isClient() && world.random.nextFloat() >= ConfigInit.CONFIG.milk_thirst_chance) {
                player.addStatusEffect(new StatusEffectInstance(EffectInit.DEHYDRATION, ConfigInit.CONFIG.potion_bad_thirst_duration / 2, 0, false, false, true));
            }
        }
    }

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
                thirstQuench = ConfigInit.CONFIG.milk_thirst_quench;
            }
            return Optional.of(new ThirstTooltipData(1, thirstQuench));
        } else {
            return super.getTooltipData(stack);
        }
    }

}
