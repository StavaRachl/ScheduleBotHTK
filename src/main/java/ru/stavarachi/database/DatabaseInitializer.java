package ru.stavarachi.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private final DatabaseManager databaseManager;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public DatabaseInitializer(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    public void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    chat_id BIGINT PRIMARY KEY,
                    group_name VARCHAR(255) NOT NULL,
                    dark_theme BOOLEAN NOT NULL DEFAULT FALSE
                )
                """;

        try (Connection connection = databaseManager.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
            logger.info("database initialize successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
