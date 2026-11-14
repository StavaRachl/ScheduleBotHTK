package ru.stavarachi.factory.components;

import ru.stavarachi.application.service.FeedbackService;
import ru.stavarachi.application.service.UserInfoService;
import ru.stavarachi.application.service.UserService;
import ru.stavarachi.application.service.UserSettingService;
import ru.stavarachi.infrastructure.telegram.*;
import ru.stavarachi.application.service.report.UserExcelGenerator;
import ru.stavarachi.application.service.report.UserReportService;
import ru.stavarachi.application.service.report.UserStatsService;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.application.util.TimeUtil;

public record TelegramComponent(
        MessageUtil messageUtil,
        TimeUtil timeUtil,
        UserService userService,
        UserSettingService userSettingService,
        UserInfoService userInfoService,
        GroupKeyboardService groupKeyboardService,
        SettingsKeyboardService settingsKeyboardService,
        FeedbackKeyboardService feedbackKeyboardService,
        BotCommandService botCommandService,
        CommandService commandService,
        UserExcelGenerator userExcelGenerator,
        UserReportService userReportService,
        UserStatsService userStatsService,
        FeedbackService feedbackService
) {
}
