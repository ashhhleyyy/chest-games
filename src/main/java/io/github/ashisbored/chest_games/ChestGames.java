package io.github.ashisbored.chest_games;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChestGames implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "chest_games";

    @Override
    public void onInitialize() {
        LOGGER.info("Chest Games initialising...");
    }
}
