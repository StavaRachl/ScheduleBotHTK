package ru.stavarachi.repository;

import ru.stavarachi.model.User;

import java.nio.file.Path;
import java.util.Map;

public interface UserGroupRepository {
    Map<Long, User> saveUsersToJson(Map<Long, User> userMap, Path path);

    Map<Long, User> loadUsersFromJson(Path path);

    User getCurrentUser(Map<Long, User> userMap, Long chatId);
}
