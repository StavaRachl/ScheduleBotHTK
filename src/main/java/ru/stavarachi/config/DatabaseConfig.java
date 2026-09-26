package ru.stavarachi.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record DatabaseConfig(
        String host,
        int port,
        String database,
        String username,
        String password
) {
    @Contract(pure = true)
    public @NotNull String jdbcUrl() {
        return "jdbc:postgresql://%s:%s/%s".formatted(host, port, database);
    }
}
