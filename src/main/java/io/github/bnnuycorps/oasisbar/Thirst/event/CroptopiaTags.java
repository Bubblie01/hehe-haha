package io.github.bnnuycorps.oasisbar.Thirst.event;

import io.github.bnnuycorps.oasisbar.Main;
import io.github.bnnuycorps.oasisbar.Thirst.ThirstManager;
import io.github.bnnuycorps.oasisbar.Thirst.inits.ConfigInit;
import io.github.bnnuycorps.oasisbar.Thirst.inits.TagInit;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;

public class CroptopiaTags {

    public static void init() {
        DrinkEvent.EVENT.register((stack, player) -> {
            ThirstManager thirstManager = ((ThirstManagerInt) player).getThirstManager();
            int thirst = 0;
            if (stack.isIn(TagInit.HYDRATING_STEW))
                thirst = ConfigInit.CONFIG.stew_thirst_quench;
            if (stack.isIn(TagInit.HYDRATING_FOOD))
                thirst = ConfigInit.CONFIG.food_thirst_quench;
            if (stack.isIn(TagInit.HYDRATING_DRINKS))
                thirst = ConfigInit.CONFIG.drinks_thirst_quench;
            if (stack.isIn(TagInit.STRONGER_HYDRATING_STEW))
                thirst = ConfigInit.CONFIG.stronger_stew_thirst_quench;
            if (stack.isIn(TagInit.STRONGER_HYDRATING_FOOD))
                thirst = ConfigInit.CONFIG.stronger_food_thirst_quench;
            if (stack.isIn(TagInit.STRONGER_HYDRATING_DRINKS))
                thirst = ConfigInit.CONFIG.stronger_drinks_thirst_quench;

            for (int i = 0; i < Main.HYDRATION_TEMPLATES.size(); i++) {
                if (Main.HYDRATION_TEMPLATES.get(i).containsItem(stack.getItem())) {
                    thirst = Main.HYDRATION_TEMPLATES.get(i).getHydration();
                    break;
                }
            }
            if (thirst > 0)
                thirstManager.add(thirst);
        });
    }

}
