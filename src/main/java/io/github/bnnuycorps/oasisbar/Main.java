package io.github.bnnuycorps.oasisbar;

import io.github.bnnuycorps.oasisbar.Thirst.event.DehydrationEvent;
import io.github.bnnuycorps.oasisbar.Thirst.event.HydrationTemplate;
import io.github.bnnuycorps.oasisbar.Thirst.inits.*;
import io.github.bnnuycorps.oasisbar.Thirst.packets.ThirstServerPacket;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main implements ModInitializer {

	public static final String MOD_ID = "oasisbar";
	public static final Logger LOGGER = (Logger) LogManager.getLogger(MOD_ID);

	public static final List<HydrationTemplate> HYDRATION_TEMPLATES = new ArrayList<HydrationTemplate>();

	@Override
	public void onInitialize() {

		CommandInit.init();
		CompatInit.init();
		ConfigInit.init();
		EffectInit.init();
		ItemInit.init();
		EventInit.init();
		SoundInit.init();
		TagInit.init();
		ThirstServerPacket.init();
		JsonReaderInit.init();

		FabricLoader.getInstance().getEntrypointContainers("dehydration", DehydrationEvent.class).forEach((entrypoint) -> {
			ModMetadata metadata = entrypoint.getProvider().getMetadata();
			String id = metadata.getId();

			try {
				DehydrationEvent api = entrypoint.getEntrypoint();
				api.registerDrinkEvent();
			} catch (Throwable exception) {
				LOGGER.error( "Mod {} is providing a broken implementation", id, exception);
			}
		});

	}
}