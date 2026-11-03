package ru.stavarachi.infrastructure.repository;

import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.domain.user.User;
import ru.stavarachi.port.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PostgresUserRepository implements UserRepository<User, Long> {

    private final DatabaseManager databaseManager;

    public PostgresUserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public List<User> findAll() {
        @Language("SQL")
        String sql = "SELECT chat_id, group_name FROM groups";

        List<User> users = new ArrayList<>();

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }

            return users;

        } catch (SQLException e) {
            log.error("Failed to retrieve users", e);
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    @Override
    public Optional<User> findById(Long chatId) {
        @Language("SQL")
        String sql = "SELECT chat_id, group_name FROM groups WHERE chat_id = ?";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, chatId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            log.error("Failed to find user: {}", chatId, e);
            throw new RuntimeException("Failed to find user", e);
        }
    }

    @Override
    public void save(@NotNull User user) {
        try (Connection connection = databaseManager.getConnection()) {
            save(user, connection);
        } catch (SQLException e) {
            log.error("Failed to save user: {}", user.getChatId(), e);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public void save(@NotNull User user, @NotNull Connection connection) {
        @Language("SQL")
        String sql = """
                INSERT INTO groups (chat_id, group_name)
                VALUES (?, ?)
                ON CONFLICT (chat_id) DO NOTHING;
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, user.getChatId());
            statement.setString(2, user.getGroup());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to save user: {}", user.getChatId(), e);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public void update(@NotNull User user) {
        @Language("SQL")
        String sql = """
                UPDATE groups
                SET group_name = ?
                WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getGroup());
            statement.setLong(2, user.getChatId());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to update user: {}", user.getChatId(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void delete(Long chatId) {
        @Language("SQL")
        String sql = "DELETE FROM groups WHERE chat_id = ?";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, chatId);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to delete user: {}", chatId, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @NotNull
    @Contract("_ -> new")
    private User mapUser(@NotNull ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("chat_id"),
                resultSet.getString("group_name")
        );
    }
}
