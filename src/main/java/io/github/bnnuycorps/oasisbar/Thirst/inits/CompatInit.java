package io.github.bnnuycorps.oasisbar.Thirst.inits;

import io.github.bnnuycorps.oasisbar.Thirst.event.CroptopiaTags;
import net.fabricmc.loader.api.FabricLoader;

public class CompatInit {

    public static void init() {
        ifLoaded("croptopia", CroptopiaTags::init);
    }

    private static void ifLoaded(String mod, Action action) {
        if (FabricLoader.getInstance().isModLoaded(mod)) action.act();
    }

    @FunctionalInterface
    private interface Action {
        void act();
    }
}
