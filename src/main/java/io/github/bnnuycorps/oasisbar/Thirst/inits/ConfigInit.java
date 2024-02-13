package io.github.bnnuycorps.oasisbar.Thirst.inits;


import io.github.bnnuycorps.oasisbar.Thirst.interfaces.DehydrationConfig;

public class ConfigInit {
    public static DehydrationConfig CONFIG = new DehydrationConfig();

    public static void init() {
        AutoConfig.register(DehydrationConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(DehydrationConfig.class).getConfig();
    }

}
