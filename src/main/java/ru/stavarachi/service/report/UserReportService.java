package ru.stavarachi.service.report;

import ru.stavarachi.model.User;
import ru.stavarachi.service.UserSettingService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class UserReportService {
    private final UserSettingService userSettingService;
    private final UserExcelGenerator userExcelGenerator;

    public UserReportService(UserSettingService userSettingService, UserExcelGenerator userExcelGenerator) {
        this.userSettingService = userSettingService;
        this.userExcelGenerator = userExcelGenerator;
    }

    public Path generateUsersReport() throws IOException {
        List<User> userList = userSettingService.getAllUsers();

        return userExcelGenerator.generateReport(userList);
    }
}
