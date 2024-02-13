package io.github.bnnuycorps.oasisbar.Thirst.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Arrays;


public interface DrinkEvent {
    void onDrink(ItemStack stack, PlayerEntity playerEntity);

    Event<DrinkEvent> EVENT = EventFactory.createArrayBacked(DrinkEvent.class, (listeners) ->
        (stack, playerEntity) -> {
            Arrays.stream(listeners).forEach((listener) -> listener.onDrink(stack, playerEntity));
        });
}
