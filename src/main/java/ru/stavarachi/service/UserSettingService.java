package ru.stavarachi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.model.User;
import ru.stavarachi.repository.UserGroupRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserSettingService {
    private final Map<Long, User> userMap;

    private final UserGroupRepository userGroupRepository = new UserGroupRepository();
    private final Logger log = LoggerFactory.getLogger(UserSettingService.class);

    private final PathConfig pathConfig = new PathConfig();

    public UserSettingService() {
        this.userMap = new ConcurrentHashMap<>(userGroupRepository.loadUsersFromJson(pathConfig.getJsonPath()));
    }

    public void toggleTheme(long chatId) {
        User user = userMap.get(chatId);

        user.setDarkTheme(!user.isDarkTheme());

        userGroupRepository.saveUsersToJson(userMap, pathConfig.getPathToSave());
    }

    public void setDefaultGroup(Long chatId, String group) {
        User user = userMap.get(chatId);

        if (user == null) {
            user = new User(chatId, group, false);
        } else {
            user.setGroup(group);
        }

        userMap.put(chatId, user);

        userGroupRepository.saveUsersToJson(userMap, pathConfig.getJsonPath());
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
