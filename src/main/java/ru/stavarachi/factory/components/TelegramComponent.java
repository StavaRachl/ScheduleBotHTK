package ru.stavarachi.factory.components;

import ru.stavarachi.infrastructure.telegram.CommandService;
import ru.stavarachi.infrastructure.telegram.GroupKeyboardService;
import ru.stavarachi.service.UserSettingService;
import ru.stavarachi.service.report.UserExcelGenerator;
import ru.stavarachi.service.report.UserReportService;
import ru.stavarachi.service.report.UserStatsService;
import ru.stavarachi.util.MessageUtil;
import ru.stavarachi.util.TimeUtil;

public record TelegramComponent(
        MessageUtil messageUtil,
        TimeUtil timeUtil,
        UserSettingService userSettingService,
        GroupKeyboardService groupKeyboardService,
        CommandService commandService,
        UserExcelGenerator userExcelGenerator,
        UserReportService userReportService,
        UserStatsService userStatsService
) {
}
