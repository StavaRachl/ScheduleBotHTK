package ru.stavarachi.application.service.report;

import ru.stavarachi.application.service.UserService;
import ru.stavarachi.domain.user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserStatsService {
    private final UserService userService;

    public UserStatsService(UserService userService) {
        this.userService = userService;
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

        StringBuilder message = new StringBuilder();

        message.append("💠Всего:")
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
                        .append("\n")
                );

        return message;
    }
}
