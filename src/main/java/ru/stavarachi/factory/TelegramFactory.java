package ru.stavarachi.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.application.service.RegistrationService;
import ru.stavarachi.application.service.UserInfoService;
import ru.stavarachi.application.service.UserService;
import ru.stavarachi.application.service.UserSettingService;
import ru.stavarachi.factory.components.DatabaseComponent;
import ru.stavarachi.factory.components.ScheduleComponent;
import ru.stavarachi.factory.components.TelegramComponent;
import ru.stavarachi.infrastructure.telegram.CommandService;
import ru.stavarachi.infrastructure.telegram.GroupKeyboardService;
import ru.stavarachi.application.service.report.UserExcelGenerator;
import ru.stavarachi.application.service.report.UserReportService;
import ru.stavarachi.application.service.report.UserStatsService;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.application.util.TimeUtil;

public final class TelegramFactory {
    private TelegramFactory() {}

    @NotNull
    @Contract("_, _ -> new")
    public static TelegramComponent create(@NotNull DatabaseComponent databaseComponent, @NotNull ScheduleComponent scheduleComponent) {
        MessageUtil messageUtil = new MessageUtil();
        TimeUtil timeUtil = new TimeUtil();

        RegistrationService registrationService = new RegistrationService(
                databaseComponent.postgresUserRepository(),
                databaseComponent.postgresUserSettingRepository(),
                databaseComponent.postgresUserInfoRepository(),
                databaseComponent.databaseManager()
        );

        UserService userService = new UserService(databaseComponent.postgresUserRepository());
        UserSettingService userSettingService = new UserSettingService(databaseComponent.postgresUserSettingRepository());
        UserInfoService userInfoService = new UserInfoService(databaseComponent.postgresUserInfoRepository());

        GroupKeyboardService groupKeyboardService = new GroupKeyboardService();

        UserExcelGenerator userExcelGenerator = new UserExcelGenerator();

        UserReportService userReportService = new UserReportService(
                userService,
                userSettingService,
                userInfoService,
                userExcelGenerator
        );

        UserStatsService userStatsService = new UserStatsService(userService);


        CommandService commandService = new CommandService(
                messageUtil,
                timeUtil,
                registrationService,
                scheduleComponent.scheduleService(),
                groupKeyboardService,
                userReportService,
                userStatsService,
                userService,
                userSettingService
        );

        return new TelegramComponent(
                messageUtil,
                timeUtil,
                userService,
                userSettingService,
                userInfoService,
                groupKeyboardService,
                commandService,
                userExcelGenerator,
                userReportService,
                userStatsService
        );
    }
}
