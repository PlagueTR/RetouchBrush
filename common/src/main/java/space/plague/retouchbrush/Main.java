package space.plague.retouchbrush;

import space.plague.retouchbrush.config.ModConfig;
import space.plague.retouchbrush.config.ModConfigManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.plague.retouchbrush.registry.DispenserBehavior;

public final class Main {

    public static final String MOD_ID = "plaguesretouchbrush";
    public static final String MOD_NAME = "Plague's Retouch Brush";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("[" + MOD_NAME + "] Loading...");

        ModConfigManager.initializeConfig();

        DispenserBehavior.register();

        LOGGER.info("[" + MOD_NAME + "] All done!");
    }

    public static ModConfig getConfig() {
        return ModConfigManager.getConfig();
    }

    public static void saveConfig() {
        ModConfigManager.save();
    }

}