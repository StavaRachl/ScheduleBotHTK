package ru.stavarachi.database;

import org.intellij.lang.annotations.Language;
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
        @Language("SQL")
        String sql = """
                CREATE TABLE IF NOT EXISTS groups (
                    chat_id BIGINT PRIMARY KEY,
                    group_name VARCHAR(8)
                );
                
                CREATE TABLE IF NOT EXISTS user_info (
                    chat_id   BIGINT PRIMARY KEY,
                    username  VARCHAR(255),
                    firstname VARCHAR(255),
                    lastname  VARCHAR(255),
                    sponsor   BOOLEAN NOT NULL DEFAULT FALSE,
                
                    CONSTRAINT fk_user_info_chat
                        FOREIGN KEY (chat_id)
                            REFERENCES groups (chat_id)
                            ON DELETE CASCADE
                );
                
                CREATE TABLE IF NOT EXISTS settings (
                    chat_id      BIGINT PRIMARY KEY,
                    dark_theme   BOOLEAN NOT NULL DEFAULT FALSE,
                    notification BOOLEAN NOT NULL DEFAULT TRUE,
                
                    CONSTRAINT fk_settings_chat
                        FOREIGN KEY (chat_id)
                            REFERENCES groups (chat_id)
                            ON DELETE CASCADE
                );
                
                CREATE TABLE IF NOT EXISTS requests (
                    id BIGSERIAL PRIMARY KEY,
                    chat_id BIGINT NOT NULL,
                    message text NOT NULL,
                    feedback_status request_status NOT NULL DEFAULT 'PENDING',
                
                    CONSTRAINT fk_request_chat
                        FOREIGN KEY (chat_id)
                                      REFERENCES groups(chat_id)
                                      ON DELETE CASCADE
                );
                """;

        try (Connection connection = databaseManager.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);

            statement.execute("""
                DO $$
                BEGIN
                    CREATE TYPE request_status AS ENUM (
                        'PENDING',
                        'REJECTED',
                        'APPROVED',
                        'IN_PROCESS'
                    );
                EXCEPTION
                    WHEN duplicate_object THEN NULL;
                END
                $$;
                """);
            logger.info("database initialize successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
