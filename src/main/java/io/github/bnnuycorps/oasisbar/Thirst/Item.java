package io.github.bnnuycorps.oasisbar.Thirst;

import io.github.bnnuycorps.oasisbar.Thirst.DrinkComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class Item {
    public final DrinkComponent drinkComponent;

    public Item(Settings settings) {
        this.drinkComponent = settings.drinkComponent;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.isDrink()) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (user.canConsume(this.getDrinkComponent().isAlwaysEdible())) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            } else {
                return TypedActionResult.fail(itemStack);
            }
        } else {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return this.isDrink() ? user.eatFood(world, stack) : stack;
    }

    public UseAction getUseAction(ItemStack stack) {
        return stack.getItem().isFood() ? UseAction.EAT : UseAction.NONE;
    }

    public int getMaxUseTime(ItemStack stack) {
        if (stack.getItem().isFood()) {
            return this.getDrinkComponent().isAlwaysEdible() ? 16 : 32;
        } else {
            return 0;
        }
    }

    public boolean isDrink() {
        return this.drinkComponent != null;
    }

    public DrinkComponent getDrinkComponent() {return this.drinkComponent;}

    public static class Settings {
        DrinkComponent drinkComponent;
        public Settings drink(DrinkComponent drinkComponent) {
            this.drinkComponent = drinkComponent;
            return this;
        }
    }
}
