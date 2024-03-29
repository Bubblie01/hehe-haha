package io.github.bnnuycorps.oasisbar.thirst.registry;

import io.github.bnnuycorps.oasisbar.thirst.identifier.TagIdentifiers;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;


public class TagRegistry {
    public static final TagKey<Item> HYDRATING_FOOD = TagKey.of(RegistryKeys.ITEM, TagIdentifiers.HYDRATING_FOOD);
    public static final TagKey<Item> HYDRATING_DRINK = TagKey.of(RegistryKeys.ITEM, TagIdentifiers.HYDRATING_DRINK);
    public static final TagKey<Item> HYDRATING_STEW = TagKey.of(RegistryKeys.ITEM, TagIdentifiers.HYDRATING_STEW);
    public static final TagKey<Biome> HOT_BIOME = TagKey.of(RegistryKeys.BIOME, TagIdentifiers.HOT_BIOME);
}
