package ru.stavarachi.service.report;

import ru.stavarachi.domain.user.User;
import ru.stavarachi.service.UserSettingService;

import java.util.ArrayList;
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

        String string;

        StringBuilder message = new StringBuilder();

        int sum = userList.size();
        string = "💠Всего: " + sum + "\n";
        message.append(string);

        for (String group : unique) {
            string = "📗" + group + " - " + countGroup(group) + "\n";

            message.append(string);
        }

        return message;
    }
}
