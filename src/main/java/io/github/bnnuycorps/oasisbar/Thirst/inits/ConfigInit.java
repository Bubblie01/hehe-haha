package io.github.bnnuycorps.oasisbar.Thirst.inits;


import io.github.bnnuycorps.oasisbar.Thirst.interfaces.DehydrationConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ConfigInit {
    public static DehydrationConfig CONFIG = new DehydrationConfig();

    public static void init() {
        AutoConfig.register(DehydrationConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(DehydrationConfig.class).getConfig();
    }

}
