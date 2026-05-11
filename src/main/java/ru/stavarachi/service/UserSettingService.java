package ru.stavarachi.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserSettingService {
    private final Map<Long, String> currentGroup = new ConcurrentHashMap<>();

    public void setDefaultGroup(Long chatId, String group) {
        currentGroup.put(chatId, group);
    }

    public String getDefaultGroup(Long chatId) {
        return currentGroup.get(chatId);
    }

    public boolean hasDefaultGroup(Long chatId) {
        return currentGroup.containsKey(chatId);
    }
}
