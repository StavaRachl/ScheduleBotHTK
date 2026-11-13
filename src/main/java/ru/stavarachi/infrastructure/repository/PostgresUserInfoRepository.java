package ru.stavarachi.infrastructure.repository;

import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.domain.user.UserInfo;
import ru.stavarachi.port.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PostgresUserInfoRepository implements Repository<UserInfo, Long> {

    private final DatabaseManager databaseManager;

    public PostgresUserInfoRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public List<UserInfo> findAll() {
        @Language("SQL")
        String sql = "SELECT chat_id, username, firstname, lastname, sponsor FROM user_info";

        List<UserInfo> users = new ArrayList<>();

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUserInfo(resultSet));
            }

            return users;

        } catch (SQLException e) {
            log.error("Failed to retrieve users", e);
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    @Override
    public Optional<UserInfo> findById(Long chatId) {
        @Language("SQL")
        String sql = """
                SELECT chat_id, username, firstname, lastname, sponsor
                FROM user_info
                WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, chatId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUserInfo(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            log.error("Failed to find user info by chatId: {}", chatId, e);
            throw new RuntimeException("Failed to find user info", e);
        }
    }

    @Override
    public void save(@NotNull UserInfo userInfo) {
        try (Connection connection = databaseManager.getConnection()) {
            save(userInfo, connection);
        } catch (SQLException e) {
            log.error("Failed to save user info: {}", userInfo.getChatId(), e);
            throw new RuntimeException("Failed to save user info", e);
        }
    }

    @Override
    public void save(@NotNull UserInfo userInfo, @NotNull Connection connection) {
        @Language("SQL")
        String sql = """
                INSERT INTO user_info
                (chat_id, username, firstname, lastname, sponsor)
                VALUES (?, ?, ?, ?, ?) ON CONFLICT (chat_id) DO NOTHING;
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userInfo.getChatId());
            statement.setString(2, userInfo.getUsername());
            statement.setString(3, userInfo.getFirstName());
            statement.setString(4, userInfo.getLastName());
            statement.setBoolean(5, userInfo.isSponsor());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to save user info: {}", userInfo.getChatId(), e);
            throw new RuntimeException("Failed to save user info", e);
        }
    }

    @Override
    public void update(@NotNull UserInfo userInfo) {
        @Language("SQL")
        String sql = """
                UPDATE user_info
                SET username = ?, firstname = ?, lastname = ?, sponsor = ?
                WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userInfo.getUsername());
            statement.setString(2, userInfo.getFirstName());
            statement.setString(3, userInfo.getLastName());
            statement.setBoolean(4, userInfo.isSponsor());
            statement.setLong(5, userInfo.getChatId());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to update user info: {}", userInfo.getChatId(), e);
            throw new RuntimeException("Failed to update user info", e);
        }
    }

    @Override
    public void delete(Long chatId) {
        @Language("SQL")
        String sql = "DELETE FROM user_info WHERE chat_id = ?";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, chatId);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to delete user info: {}", chatId, e);
            throw new RuntimeException("Failed to delete user info", e);
        }
    }

    @NotNull
    @Contract("_ -> new")
    private UserInfo mapUserInfo(@NotNull ResultSet resultSet) throws SQLException {
        return new UserInfo(
                resultSet.getLong("chat_id"),
                resultSet.getString("username"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                resultSet.getBoolean("sponsor")
        );
    }
}
