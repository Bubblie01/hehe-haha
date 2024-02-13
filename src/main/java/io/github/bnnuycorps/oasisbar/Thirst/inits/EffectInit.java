package io.github.bnnuycorps.oasisbar.Thirst.inits;

import io.github.bnnuycorps.oasisbar.Thirst.event.DehydrationEvent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class EffectInit {

    public final static StatusEffect DEHYDRATION = new DehydrationEvent(StatusEffectCategory.HARMFUL, 3062757);

    public static void init() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier("dehydration", "dehydration_effect"), DEHYDRATION);
    }

}
