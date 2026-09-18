package ru.stavarachi.repository;

import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteUserRepository implements UserRepository<User, Long> {
    private final DatabaseManager databaseManager;

    public SQLiteUserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public List<User> findAll() {
        String sql = """
                SELECT chat_id, group_name, dark_theme
                FROM users
                """;

        List<User> userList = new ArrayList<>();

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                userList.add(mapUser(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        String sql = """
                SELECT chat_id, group_name, dark_theme
                FROM users
                WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(User user) {
        String sql = """
                INSERT INTO users (chat_id, group_name, dark_theme)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getGroup());
            preparedStatement.setBoolean(3, user.isDarkTheme());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        String sql = """
                UPDATE users
                SET group_name = ?,
                    dark_theme = ?
                WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getGroup());
            preparedStatement.setBoolean(2, user.isDarkTheme());
            preparedStatement.setLong(3, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long aLong) {
        String sql = """
                DELETE FROM users
                WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("chat_id"),
                resultSet.getString("group_name"),
                resultSet.getBoolean("dark_theme")
        );
    }
}
