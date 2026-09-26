package ru.stavarachi.service;

import ru.stavarachi.model.User;
import ru.stavarachi.repository.PostgresUserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserSettingService {
    private final Map<Long, User> userMap;
    private final PostgresUserRepository postgresUserRepository;

    public UserSettingService(PostgresUserRepository postgresUserRepository) {
        this.userMap = new ConcurrentHashMap<>();
        this.postgresUserRepository = postgresUserRepository;

        postgresUserRepository.findAll().forEach(user -> userMap.put(user.getId(), user));
    }

    public List<User> getAllUsers() {
        return postgresUserRepository.findAll();
    }

    public void toggleTheme(Long chatId) {
        User user = userMap.get(chatId);

        if (user == null) {
            user = new User(chatId, null, true);
            userMap.put(chatId, user);
            postgresUserRepository.save(user);
            return;
        }

        user.setDarkTheme(!user.isDarkTheme());

        postgresUserRepository.update(user);
    }

    public void setDefaultGroup(long chatId, String group) {

        User user = userMap.get(chatId);

        if (user == null) {
            user = new User(chatId, group, false);

            userMap.put(chatId, user);
            postgresUserRepository.save(user);

            return;
        }

        user.setGroup(group);

        postgresUserRepository.update(user);
    }

    public String getDefaultGroup(long chatId) {

        User user = userMap.get(chatId);

        if (user == null) {
            return null;
        }

        return user.getGroup();
    }

    public boolean hasDefaultGroup(long chatId) {

        User user = userMap.get(chatId);

        return user != null && user.getGroup() != null;
    }

    public User getUser(long chatId) {
        return userMap.get(chatId);
    }

    public Map<Long, User> getUsers() {
        return userMap;
    }
}
