package net.just_s.tmp;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TMPMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("tmp");
	public static final Config CONFIG = new Config();

	@Override
	public void onInitialize() {
		LOGGER.info("TMP initialized successfully!");
		CONFIG.load();
	}
}
