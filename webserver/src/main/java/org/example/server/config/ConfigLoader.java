package org.example.server.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.server.constants.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class ConfigLoader {
    private static volatile ConfigLoader instance;
    private Map<String, String> jsonData;
    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);


    private ConfigLoader() {
        loadJsonFromResources();
    }


    public static ConfigLoader getInstance() {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                if (instance == null) {
                    instance = new ConfigLoader();
                }
            }
        }
        return instance;
    }


    private void loadJsonFromResources() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = getResourceAsStream(Constants.FILE_NAME)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Configuration file not found: " + Constants.FILE_NAME);
            }
            jsonData = mapper.readValue(inputStream, new TypeReference<Map<String, String>>() {
            });
            logger.info("Configuration loaded successfully from {}", Constants.FILE_NAME);
        } catch (IOException e) {
            logger.error("Error reading configuration file {}: {}", Constants.FILE_NAME, e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }


    private InputStream getResourceAsStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }


    public Map<String, String> getJsonData() {
        Objects.requireNonNull(jsonData, "Configuration data has not been loaded");
        return jsonData;
    }
}
