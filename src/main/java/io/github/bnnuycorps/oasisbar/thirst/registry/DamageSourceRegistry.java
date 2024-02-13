package io.github.bnnuycorps.oasisbar.thirst.registry;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;


public class DamageSourceRegistry {

    public static final RegistryKey<DamageType> DEHYDRATION = register("dehydration");

    private static RegistryKey<DamageType> register(String id) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ResourcesRegistry.id(id));
    }

}
