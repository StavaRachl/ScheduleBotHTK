package ru.stavarachi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:runtime/data/schedule.db";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

}
