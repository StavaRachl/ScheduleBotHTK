package ru.stavarachi.application.service;

import ru.stavarachi.domain.user.UserSettings;
import ru.stavarachi.port.UserSettingsRepository;

import java.util.List;
import java.util.Optional;

public class UserSettingService {
    private final UserSettingsRepository<UserSettings, Long> repository;

    public UserSettingService(UserSettingsRepository<UserSettings, Long> repository) {
        this.repository = repository;
    }

    public List<UserSettings> getAll() {
        return repository.findAll();
    }

    public Optional<UserSettings> getSetting(Long id) {
        return repository.findById(id);
    };

    public void toggleTheme(Long id) {
        repository.findById(id)
                .ifPresent(userSettings -> {
                    userSettings.setDarkTheme(!userSettings.isDarkTheme());
                    repository.update(userSettings);
                });
    };

    public void toggleNotification(Long id) {
        repository.findById(id)
                .ifPresent(userSettings -> {
                    userSettings.setNotification(!userSettings.isNotification());
                    repository.update(userSettings);
                });
    }
}
