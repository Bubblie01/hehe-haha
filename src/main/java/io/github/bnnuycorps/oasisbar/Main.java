package io.github.bnnuycorps.oasisbar;

import io.github.bnnuycorps.oasisbar.thirst.registry.*;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;



public class Main implements ModInitializer {

	public static final String MOD_ID = "oasisbar";
	public static final Logger LOGGER = (Logger) LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ConfigRegistry.register();
		ServerEventRegistry.register();
		SoundRegistry.register();
		EffectRegistry.register();
	}

}