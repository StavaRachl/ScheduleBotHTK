package ru.stavarachi.application.service.report;

import ru.stavarachi.application.service.UserService;
import ru.stavarachi.application.service.UserSettingService;
import ru.stavarachi.domain.user.User;
import ru.stavarachi.domain.user.UserSettings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserStatsService {
    private final UserService userService;
    private final UserSettingService userSettingService;

    public UserStatsService(UserService userService, UserSettingService userSettingService) {
        this.userService = userService;
        this.userSettingService = userSettingService;
    }

    public List<String> uniqueGroup() {
        List<User> userList = userService.getAllUser();

        List<String> list = new ArrayList<>();

        for (User user : userList) {
            String group = user.getGroup();

            if (!list.contains(group) && group != null) {
                list.add(group);
            }
        }

        return list;
    }

    public int countGroup(String group) {
        List<User> userList = userService.getAllUser();
        int count = 0;

        for (User user : userList) {
            if (group != null && group.equals(user.getGroup())) {
                count++;
            }
        }

        return count;
    }


    public StringBuilder generateStats() {
        List<String> unique = uniqueGroup();
        List<User> userList = userService.getAllUser();
        List<UserSettings> userSettingsList = userSettingService.getAll();

        StringBuilder message = new StringBuilder();

        message.append("\t\t💠Всего: ")
                .append(userList.size())
                .append("\n")

                .append("\t\t💠Уникальных групп: ")
                .append(unique.size())
                .append("\n")

                .append("\t\t🌙Темная тема:")
                .append(userSettingsList.stream().filter(UserSettings::isDarkTheme).count())
                .append("/")
                .append(userList.size())
                .append("\n")

                .append("\n")
                .append("🏆Топ 5 самых популярных групп")
                .append("\n");

        unique.stream()
                .sorted(Comparator.comparingInt(this::countGroup).reversed())
                .limit(5)
                .forEach(group -> message.append("\t\t📗")
                        .append(group)
                        .append(" - ")
                        .append(countGroup(group))
                        .append("\n")
                );

        return message;
    }
}
