package org.example.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.server.constants.Constants;
import java.util.Map;

public class ConfigParseUtils {

    private static final Logger logger = LogManager.getLogger(ConfigParseUtils.class);


    public static int parsePort(Map<String, String> configs) {
        String portConfig = configs.get(Constants.PORT);
        if (portConfig == null) {
            throw new IllegalArgumentException("Port configuration is missing.");
        }
        try {
            return Integer.parseInt(portConfig);
        } catch (NumberFormatException e) {
            logger.error("Invalid port format: {}", portConfig);
            throw new IllegalArgumentException("Invalid port format.", e);
        }
    }


    public static int parseMaxThreads(Map<String, String> configs) {
        String threadsConfig = configs.get(Constants.MAX_THREADS);
        if (threadsConfig == null) {
            throw new IllegalArgumentException("Max threads configuration is missing.");
        }
        try {
            return Integer.parseInt(threadsConfig);
        } catch (NumberFormatException e) {
            logger.error("Invalid max threads format: {}", threadsConfig);
            throw new IllegalArgumentException("Invalid max threads format.", e);
        }
    }
}
