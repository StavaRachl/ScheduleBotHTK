package ru.stavarachi.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.NotNull;

public class ConfigLoader {
    private final Dotenv dotenv;

    public ConfigLoader() {
        this.dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }

    private @NotNull String get(String key) {
        String value = System.getenv(key);

        if (value != null && !value.isBlank()) {
            return value;
        }

        return dotenv.get(key);
    }

    public DatabaseConfig database() {
        return new DatabaseConfig(
                get("POSTGRES_HOST"),
                Integer.parseInt(get("POSTGRES_PORT")),
                get("POSTGRES_DB"),
                get("POSTGRES_USER"),
                get("POSTGRES_PASSWORD")
        );
    }
}
