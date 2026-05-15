package ru.stavarachi.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.model.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserGroupRepository {
    private final Logger log = LoggerFactory.getLogger(UserGroupRepository.class);

    public Map<Long, User> saveUsersToJson(Map<Long, User> userMap, String pathToSave) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(pathToSave), userMap);
            log.info("User data save");
            return userMap;
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return userMap;
    }

    public Map<Long, User> loadUsersFromJson(String pathToSave) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(pathToSave);

            if (!file.exists() || file.length() == 0) {
                return new HashMap<>();
            }

            log.info("User data load");
            return mapper.readValue(new File(pathToSave), new TypeReference<Map<Long, User>>() {});
        } catch (Exception e) {
            log.error("Error: ", e);
            return null;
        }
    }

    public User getCurrentUser(Map<Long, User> userMap, Long chatId) {
        try {
            return userMap.get(chatId);
        } catch (Exception e) {
            log.error("Error: ", e);
            return null;
        }
    }
}
