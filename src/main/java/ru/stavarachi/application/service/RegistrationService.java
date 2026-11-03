package ru.stavarachi.application.service;

import org.jetbrains.annotations.NotNull;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.domain.user.User;
import ru.stavarachi.domain.user.UserInfo;
import ru.stavarachi.domain.user.UserSettings;
import ru.stavarachi.port.UserInfoRepository;
import ru.stavarachi.port.UserRepository;
import ru.stavarachi.port.UserSettingsRepository;

import java.util.Objects;

public class RegistrationService {
    private final UserRepository<User, Long> userRepository;
    private final UserSettingsRepository<UserSettings, Long> userSettingsRepository;
    private final UserInfoRepository<UserInfo, Long> userInfoRepository;
    private final DatabaseManager databaseManager;

    public RegistrationService(UserRepository<User, Long> userRepository, UserSettingsRepository<UserSettings, Long> userSettingsRepository, UserInfoRepository<UserInfo, Long> userInfoRepository, DatabaseManager databaseManager) {
        this.userRepository = userRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.userInfoRepository = userInfoRepository;
        this.databaseManager = databaseManager;
    }

    public void registerChat(@NotNull Long id, @NotNull UserInfo userInfo) {
        Objects.requireNonNull(id, "Chat id must to be null");
        Objects.requireNonNull(userInfo, "User info must to be null");

        if (!id.equals(userInfo.getChatId())) {
            throw new IllegalArgumentException("Chat IDs do not match");
        }

        User user = new User(id, null);
        UserSettings userSettings = new UserSettings(id, false, true);

        databaseManager.executeInTransaction(connection -> {
            userRepository.save(user, connection);
            userInfoRepository.save(userInfo, connection);
            userSettingsRepository.save(userSettings, connection);
        });
    }
}
