package ru.stavarachi.application.service.report;

import ru.stavarachi.application.service.UserInfoService;
import ru.stavarachi.application.service.UserService;
import ru.stavarachi.application.service.UserSettingService;
import ru.stavarachi.domain.user.User;
import ru.stavarachi.domain.user.UserInfo;
import ru.stavarachi.domain.user.UserSettings;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class UserReportService {
    private final UserService userService;
    private final UserSettingService userSettingService;
    private final UserInfoService userInfoService;
    private final UserExcelGenerator userExcelGenerator;

    public UserReportService(UserService userService, UserSettingService userSettingService, UserInfoService userInfoService, UserExcelGenerator userExcelGenerator) {
        this.userService = userService;
        this.userSettingService = userSettingService;
        this.userInfoService = userInfoService;
        this.userExcelGenerator = userExcelGenerator;
    }


    public Path generateUsersReport() throws IOException {
        List<User> userList = userService.getAllUser()
                .stream()
                .sorted(Comparator.comparing(User::getChatId))
                .toList();

        List<UserInfo> userInfoList =  userInfoService.getAll()
                .stream()
                .sorted(Comparator.comparing(UserInfo::getChatId))
                .toList();

        List<UserSettings> userSettingsList = userSettingService.getAll()
                .stream()
                .sorted(Comparator.comparing(UserSettings::getChatId))
                .toList();

        return userExcelGenerator.generateReport(userList, userInfoList, userSettingsList);
    }
}
