package io.github.ashisbored.${MOD_ID};

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ${MOD_CLASS} implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "${MOD_ID}";

    @Override
    public void onInitialize() {
        LOGGER.info("${MOD_NAME} initialising...");
    }
}
