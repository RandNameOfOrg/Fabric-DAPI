package ru.daniil10295.DAPI;

import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class DAPI implements ModInitializer {
    public Logger logger = Logger.getLogger("D-API");

    @Override
    public void onInitialize() {
        logger.info("Loaded D-API DEV");
        try {
            new TestCommand();
        } catch (Exception e) {
            logger.severe("Failed to initialize TestCommand: " + e);
        }
    }
}
