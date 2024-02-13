package io.github.bnnuycorps.oasisbar.thirst.registry;

import io.github.bnnuycorps.oasisbar.thirst.config.VanillaThirstConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;


public class ConfigRegistry {
    public static VanillaThirstConfig CONFIG = new VanillaThirstConfig();

    public static void register() {
        AutoConfig.register(VanillaThirstConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(VanillaThirstConfig.class).getConfig();
    }
}
