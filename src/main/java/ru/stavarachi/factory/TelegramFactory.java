package ru.stavarachi.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.factory.components.DatabaseComponent;
import ru.stavarachi.factory.components.ScheduleComponent;
import ru.stavarachi.factory.components.TelegramComponent;
import ru.stavarachi.infrastructure.telegram.CommandService;
import ru.stavarachi.infrastructure.telegram.GroupKeyboardService;
import ru.stavarachi.service.UserSettingService;
import ru.stavarachi.service.report.UserExcelGenerator;
import ru.stavarachi.service.report.UserReportService;
import ru.stavarachi.service.report.UserStatsService;
import ru.stavarachi.util.MessageUtil;
import ru.stavarachi.util.TimeUtil;

public final class TelegramFactory {
    private TelegramFactory() {}

    @NotNull
    @Contract("_, _ -> new")
    public static TelegramComponent create(@NotNull DatabaseComponent databaseComponent, @NotNull ScheduleComponent scheduleComponent) {
        MessageUtil messageUtil = new MessageUtil();
        TimeUtil timeUtil = new TimeUtil();

        UserSettingService userSettingService = new UserSettingService(databaseComponent.postgresUserRepository());
        GroupKeyboardService groupKeyboardService = new GroupKeyboardService();

        UserExcelGenerator userExcelGenerator = new UserExcelGenerator();
        UserReportService userReportService = new UserReportService(userSettingService, userExcelGenerator);
        UserStatsService userStatsService = new UserStatsService(userSettingService);


        CommandService commandService = new CommandService(
                messageUtil,
                timeUtil,
                userSettingService,
                scheduleComponent.scheduleService(),
                groupKeyboardService,
                userReportService,
                userStatsService
        );

        return new TelegramComponent(
                messageUtil,
                timeUtil,
                userSettingService,
                groupKeyboardService,
                commandService,
                userExcelGenerator,
                userReportService,
                userStatsService
        );
    }
}
