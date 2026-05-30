package ru.stavarachi.config;

import java.io.InputStream;

public class ResourceLoader {
    public static InputStream resourceStream(String resourcePath) throws IllegalAccessException {
        InputStream inputStream = ResourceLoader.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IllegalAccessException("Resource not found: " + resourcePath);
        }
        return inputStream;
    }
}
