package io.github.ashisbored.chest_games;

import io.github.ashisbored.chest_games.games.ChestGameRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChestGames implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "chest_games";

    @Override
    public void onInitialize() {
        LOGGER.info("Chest Games initialising...");
        ChestGameRegistry.register();
        CGBlocks.register();
        CGItems.register();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
