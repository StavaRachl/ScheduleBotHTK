package ru.stavarachi.service.report;

import org.jetbrains.annotations.NotNull;
import ru.stavarachi.domain.user.User;
import ru.stavarachi.service.UserSettingService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserStatsService {
    private final UserSettingService userSettingService;

    public UserStatsService(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    public List<String> uniqueGroup() {
        List<User> userList = userSettingService.getAllUsers();

        List<String> list = new ArrayList<>();

        for (User user : userList) {
            String group = user.getGroup();

            if (!list.contains(group)) {
                list.add(group);
            }
        }

        return list;
    }

    public int countGroup(String group) {
        List<User> userList = userSettingService.getAllUsers();
        int count = 0;

        for (User user : userList) {
            if (group.equals(user.getGroup())) {
                count++;
            }
        }

        return count;
    }


    public StringBuilder generateStats() {
        List<String> unique = uniqueGroup();
        List<User> userList = userSettingService.getAllUsers();

        StringBuilder message = new StringBuilder();

        message.append("💠Всего: ")
                .append(userList.size())
                .append("\n")
                .append("\n")
                .append("🏆Топ 5 самых популярных групп")
                .append("\n");

        unique.stream()
                .sorted(Comparator.comparingInt(this::countGroup).reversed())
                .limit(5)
                .forEach(group -> message.append("📗")
                        .append(group)
                        .append(" - ")
                        .append(countGroup(group))
                        .append("\n"));

        return message;
    }
}
