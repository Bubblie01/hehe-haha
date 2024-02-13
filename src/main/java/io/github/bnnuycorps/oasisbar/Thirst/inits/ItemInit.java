package io.github.bnnuycorps.oasisbar.Thirst.inits;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.dehydration.item.HandbookItem;
import net.dehydration.item.LeatherFlask;
import net.dehydration.item.PurifiedBucket;
import net.dehydration.item.WaterBowlItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemInit {
    // Item Group
    public static final RegistryKey<ItemGroup> DEHYDRATION_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("dehydration", "item_group"));
    // Map and List
    private static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

    // Flasks
    public static final List<Item> FLASK_ITEM_LIST = new ArrayList<Item>();
    public static final Item LEATHER_FLASK = register("leather_flask", new LeatherFlask(0, new Item.Settings().maxCount(1)));

    // Handbook

    private static Item register(String name, Item item) {
        ItemGroupEvents.modifyEntriesEvent(DEHYDRATION_ITEM_GROUP).register(entries -> entries.add(item));
        ITEMS.put(new Identifier("dehydration", name), item);
        if (name.contains("flask")) {
            FLASK_ITEM_LIST.add(item);
        }
        return item;
    }

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, DEHYDRATION_ITEM_GROUP,
                FabricItemGroup.builder().icon(() -> new ItemStack(ItemInit.LEATHER_FLASK)).displayName(Text.translatable("item.dehydration.item_group")).build());
        for (Identifier id : ITEMS.keySet()) {
            Registry.register(Registries.ITEM, id, ITEMS.get(id));
        }

    }

}
