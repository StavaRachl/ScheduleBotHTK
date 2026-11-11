package ru.stavarachi.factory.components;

import ru.stavarachi.application.service.UserInfoService;
import ru.stavarachi.application.service.UserService;
import ru.stavarachi.application.service.UserSettingService;
import ru.stavarachi.infrastructure.telegram.BotCommandService;
import ru.stavarachi.infrastructure.telegram.CommandService;
import ru.stavarachi.infrastructure.telegram.GroupKeyboardService;
import ru.stavarachi.application.service.report.UserExcelGenerator;
import ru.stavarachi.application.service.report.UserReportService;
import ru.stavarachi.application.service.report.UserStatsService;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.application.util.TimeUtil;
import ru.stavarachi.infrastructure.telegram.SettingsKeyboardService;

public record TelegramComponent(
        MessageUtil messageUtil,
        TimeUtil timeUtil,
        UserService userService,
        UserSettingService userSettingService,
        UserInfoService userInfoService,
        GroupKeyboardService groupKeyboardService,
        SettingsKeyboardService settingsKeyboardService,
        BotCommandService botCommandService,
        CommandService commandService,
        UserExcelGenerator userExcelGenerator,
        UserReportService userReportService,
        UserStatsService userStatsService
) {
}
