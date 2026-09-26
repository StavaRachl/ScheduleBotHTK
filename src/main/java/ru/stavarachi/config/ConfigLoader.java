package ru.stavarachi.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigLoader {
    private final Dotenv dotenv;

    public ConfigLoader() {
        this.dotenv = Dotenv.load();
    }

    public DatabaseConfig database() {
        return new DatabaseConfig(
                dotenv.get("POSTGRES_HOST"),
                Integer.parseInt(dotenv.get("POSTGRES_PORT")),
                dotenv.get("POSTGRES_DB"),
                dotenv.get("POSTGRES_USER"),
                dotenv.get("POSTGRES_PASSWORD")
        );
    }
}
