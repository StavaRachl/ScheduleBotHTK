package ru.stavarachi.database;

import org.jetbrains.annotations.Contract;
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
                    chat_id INTEGER PRIMARY KEY,
                    group_name TEXT NOT NULL,
                    dark_theme INTEGER NOT NULL DEFAULT 0
                )
                """;

        try (Connection connection = databaseManager.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
