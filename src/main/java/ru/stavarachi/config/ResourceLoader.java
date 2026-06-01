package ru.stavarachi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class ResourceLoader {
    public static InputStream resourceStream(String resourcePath){
        Logger logger = LoggerFactory.getLogger(ResourceLoader.class);
        InputStream inputStream = ResourceLoader.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            logger.error("Resource not found: {}", resourcePath);
        }
        return inputStream;
    }
}
