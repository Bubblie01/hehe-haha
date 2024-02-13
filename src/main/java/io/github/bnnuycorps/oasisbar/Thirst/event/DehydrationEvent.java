package io.github.bnnuycorps.oasisbar.Thirst.event;

import io.github.bnnuycorps.oasisbar.Thirst.ThirstManager;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;


public interface DehydrationEvent {

    default void registerDrinkEvent() {
        DrinkEvent.EVENT.register(this::onDrink);
    }

    default void onDrink(ItemStack stack, PlayerEntity playerEntity) {
        // Get the ThirstManager using a convenient utility called ThirstManagerAccess
        ThirstManager thirstManager = ((ThirstManagerInt) playerEntity).getThirstManager();

        // Calculate the thirst using calculateDrinkThirst
        int thirst = calculateDrinkThirst(stack, playerEntity);
        thirstManager.add(thirst);
    }

    int calculateDrinkThirst(ItemStack stack, PlayerEntity playerEntity);
}
