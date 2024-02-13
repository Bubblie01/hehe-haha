package io.github.bnnuycorps.oasisbar.thirst.registry;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static io.github.bnnuycorps.oasisbar.Main.MOD_ID;

public class ResourcesRegistry {
    public static Identifier id(@NotNull String path)
    {
        return new Identifier(MOD_ID, path);
    }
}
