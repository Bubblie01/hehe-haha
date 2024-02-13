package io.github.bnnuycorps.oasisbar.thirst.registry;


import io.github.bnnuycorps.oasisbar.thirst.effect.DehydrationEffect;
import io.github.bnnuycorps.oasisbar.thirst.identifier.EffectIdentifiers;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EffectRegistry {
    public static StatusEffect THIRST;

    public static void register() {
        THIRST = Registry.register(Registries.STATUS_EFFECT, EffectIdentifiers.THIRST, new DehydrationEffect(StatusEffectCategory.HARMFUL, 11983920));
    }
}
