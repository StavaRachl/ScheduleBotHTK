package ru.stavarachi.infrastructure.repository;

import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.domain.user.UserInfo;
import ru.stavarachi.domain.user.UserSettings;
import ru.stavarachi.port.UserSettingsRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PostgresUserSettingRepository
        implements UserSettingsRepository<UserSettings, Long> {

    private final DatabaseManager databaseManager;

    public PostgresUserSettingRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public List<UserSettings> findAll() {
        @Language("SQL")
        String sql = "SELECT chat_id, dark_theme, notification FROM settings";

        List<UserSettings> users = new ArrayList<>();

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUserSettings(resultSet));
            }

            return users;

        } catch (SQLException e) {
            log.error("Failed to retrieve users", e);
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    @Override
    public Optional<UserSettings> findById(Long chatId) {
        @Language("SQL")
        String sql = """
                SELECT chat_id, dark_theme, notification
                FROM settings
                WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, chatId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUserSettings(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            log.error("Failed to find user settings: {}", chatId, e);
            throw new RuntimeException("Failed to find user settings", e);
        }
    }

    @Override
    public void save(@NotNull UserSettings settings) {
        try (Connection connection = databaseManager.getConnection()) {
            save(settings, connection);
        } catch (SQLException e) {
            log.error("Failed to save user settings: {}", settings.getChatId(), e);
            throw new RuntimeException("Failed to save user settings", e);
        }
    }

    @Override
    public void save(@NotNull UserSettings settings, @NotNull Connection connection) {
        @Language("SQL")
        String sql = """
                INSERT INTO settings (chat_id, dark_theme, notification)
                VALUES (?, ?, ?) ON CONFLICT (chat_id) DO NOTHING;
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, settings.getChatId());
            statement.setBoolean(2, settings.isDarkTheme());
            statement.setBoolean(3, settings.isNotification());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to save user settings: {}", settings.getChatId(), e);
            throw new RuntimeException("Failed to save user settings", e);
        }
    }

    @Override
    public void update(@NotNull UserSettings settings) {
        @Language("SQL")
        String sql = """
                UPDATE settings
                SET dark_theme = ?, notification = ?
                WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, settings.isDarkTheme());
            statement.setBoolean(2, settings.isNotification());
            statement.setLong(3, settings.getChatId());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to update user settings: {}", settings.getChatId(), e);
            throw new RuntimeException("Failed to update user settings", e);
        }
    }

    @Override
    public void delete(Long chatId) {
        @Language("SQL")
        String sql = "DELETE FROM settings WHERE chat_id = ?";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, chatId);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to delete user settings: {}", chatId, e);
            throw new RuntimeException("Failed to delete user settings", e);
        }
    }

    @NotNull
    @Contract("_ -> new")
    private UserSettings mapUserSettings(@NotNull ResultSet resultSet) throws SQLException {
        return new UserSettings(
                resultSet.getLong("chat_id"),
                resultSet.getBoolean("dark_theme"),
                resultSet.getBoolean("notification")
        );
    }
}
