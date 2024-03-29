package io.github.bnnuycorps.oasisbar.mixin;

import io.github.bnnuycorps.oasisbar.thirst.access.ThirstManagerAccess;
import io.github.bnnuycorps.oasisbar.thirst.registry.ConfigRegistry;
import io.github.bnnuycorps.oasisbar.thirst.registry.TagRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {

    // Add thirstLevel after drinking a water bottle
    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    public void vanillaThirst$hydratingWaterBottle(ItemStack stack, World world, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (livingEntity instanceof PlayerEntity player && !world.isClient()) {
            int thirst_value = 0;

            if (stack.isIn(TagRegistry.HYDRATING_DRINK))
                thirst_value = ConfigRegistry.CONFIG.hydrating_drink_value;

            if (thirst_value > 0) {
                ((ThirstManagerAccess) player).getThirstManager().add(thirst_value);
            }
        }
    }
}
