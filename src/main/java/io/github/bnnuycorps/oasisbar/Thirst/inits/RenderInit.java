package io.github.bnnuycorps.oasisbar.Thirst.inits;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderInit {
    public static final Identifier DEHYDRATION_ICON = new Identifier("dehydration:textures/gui/dehydration.png");

}
