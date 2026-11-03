package ru.stavarachi.database;

import ru.stavarachi.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class DatabaseManager {
    private final DatabaseConfig config;

    public DatabaseManager(DatabaseConfig config) {
        this.config = config;
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.jdbcUrl(),
                config.username(),
                config.password()
        );
    }

    public void executeInTransaction(Consumer<Connection> operation) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            try {
                operation.accept(connection);
                connection.commit();

            } catch (RuntimeException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                throw e;

            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                throw new RuntimeException("Transaction failed", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute transaction", e);
        }
    }
}
