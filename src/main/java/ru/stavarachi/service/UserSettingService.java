package ru.stavarachi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.model.User;
import ru.stavarachi.repository.UserGroupRepositoryImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserSettingService {
    private final Map<Long, User> userMap;

    private final UserGroupRepositoryImpl userGroupRepositoryImpl = new UserGroupRepositoryImpl();
    private final Logger log = LoggerFactory.getLogger(UserSettingService.class);

    public UserSettingService() {
        this.userMap = new ConcurrentHashMap<>(userGroupRepositoryImpl.loadUsersFromJson(StorageConfig.userJson()));
    }

    public void toggleTheme(long chatId) {
        User user = userMap.get(chatId);

        user.setDarkTheme(!user.isDarkTheme());

        userGroupRepositoryImpl.saveUsersToJson(userMap, StorageConfig.userJson());
    }

    public void setDefaultGroup(Long chatId, String group) {
        User user = userMap.get(chatId);

        if (user == null) {
            user = new User(chatId, group, false);
        } else {
            user.setGroup(group);
        }

        userMap.put(chatId, user);

        userGroupRepositoryImpl.saveUsersToJson(userMap, StorageConfig.userJson());
    }

    public String getDefaultGroup(Long chatId) {
        User user = userMap.get(chatId);

        if (user == null) {
            return null;
        }

        return user.getGroup();
    }

    public boolean hasDefaultGroup(Long chatId) {
        return userMap.containsKey(chatId) && userMap.get(chatId).getGroup() != null;
    }

    public User getUser(Long chatId) {
        return userMap.get(chatId);
    }
}
