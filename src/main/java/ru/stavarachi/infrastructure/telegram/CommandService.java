package ru.stavarachi.infrastructure.telegram;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.stavarachi.application.service.RegistrationService;
import ru.stavarachi.application.service.UserService;
import ru.stavarachi.application.service.UserSettingService;
import ru.stavarachi.config.*;
import ru.stavarachi.application.service.report.UserReportService;
import ru.stavarachi.application.service.report.UserStatsService;
import ru.stavarachi.application.service.schedule.ScheduleService;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.application.util.TimeUtil;
import ru.stavarachi.domain.user.UserInfo;
import ru.stavarachi.domain.user.UserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CommandService {
    private final MessageUtil messageUtil;
    private final TimeUtil timeUtil;
    private final RegistrationService registrationService;
    private final ScheduleService scheduleService;
    private final GroupKeyboardService groupKeyboardService;
    private final SettingsKeyboardService settingsKeyboardService;
    private final UserReportService userReportService;
    private final UserStatsService userStatsService;
    private final UserService userService;
    private final UserSettingService userSettingService;

    private final ScheduleConfig scheduleConfig = new ScheduleConfig();
    private final BotConfig botConfig = new BotConfig();
    private static final AppConfig appConfig = new AppConfig();

    public CommandService(MessageUtil messageUtil, TimeUtil timeUtil, RegistrationService registrationService, ScheduleService scheduleService, GroupKeyboardService groupKeyboardService, SettingsKeyboardService settingsKeyboardService, UserReportService userReportService, UserStatsService userStatsService, UserService userService, UserSettingService userSettingService) {
        this.messageUtil = messageUtil;
        this.timeUtil = timeUtil;
        this.registrationService = registrationService;
        this.scheduleService = scheduleService;
        this.groupKeyboardService = groupKeyboardService;
        this.settingsKeyboardService = settingsKeyboardService;
        this.userReportService = userReportService;
        this.userStatsService = userStatsService;
        this.userService = userService;
        this.userSettingService = userSettingService;
    }

    private static final Long ADMIN_ID = appConfig.getAdminId();

    private static final String SCHEDULE_ALERT_MESSAGE = "⚠️Сначала выберите группу через /setdefaultgroup";
    private static final String INFO_MESSAGE = """
                📋Информация о боте
                🧑‍💻Разработчик: @StavaRaChiii
                🛎️Версия: 7.2.0
                ❗Проект не является официальным продуктом КГБ ПОУ "Хабаровский Технический Колледж"
                ⚙️Информация о настройках:
                ```
                - Тема: отвечает за цветовую гамму изображения расписания
                - Уведомления: отвечает за рассылку сообщений от администратора касаемо информирования о неточностях, обновлениях и т.д.
                ```
                🪲Если вы нашли баг или какую то неточность напишите об этом разработчику @StavaRaChiii
                📃[Политика конфиденциальности](https://telegra.ph/Politika-konfidencialnosti-Telegram-bota-10-01)
                """;

    public void startCommand(TelegramLongPollingBot bot, @NotNull Update update, Long chatId) {
        var from = update.getMessage().getFrom();

        UserInfo userInfo;

        if (update.getMessage().getChat().isGroupChat() || update.getMessage().getChat().isSuperGroupChat()) {
            userInfo = createGroupUserInfo(chatId);
        } else if (update.getMessage().getChat().isUserChat()){
            userInfo = createPrivateUserInfo(chatId, from);
        } else {
            return;
        }

        registrationService.registerChat(chatId, userInfo);

        messageUtil.sendMessage(bot, chatId, "✅Бот запущен.");
        messageUtil.sendMessage(bot, chatId, "⚠️ВНИМАНИЕ! Перед использованием бота просим вас установить необходимую вам группу командой /setdefaultgroup.");
    }

    public void raspCommand(TelegramLongPollingBot bot, Long chatId, UserSettings settings) throws Exception {
        if (!userService.hasDefaultGroup(chatId)) {
            messageUtil.sendMessage(bot, chatId, SCHEDULE_ALERT_MESSAGE);
            return;
        }

        messageUtil.sendMessage(bot, chatId, "Идёт получение расписания, пожалуйста подождите⌛");

        String group = userService.getDefaultGroup(chatId).orElseThrow();
        Path path = scheduleService.generateScheduleImage(StorageConfig.scheduleExcel(), group, timeUtil.getDayOfWeek(), settings, timeUtil.getMonth());

        messageUtil.sendPhoto(bot, chatId, "🗓️Расписание для " + group + "\n" + timeUtil.getDayOfWeek() + ": " + timeUtil.getNumeratorOrDenominator() + "\n[Изменения в расписании](https://1drv.ms/x/c/31511f965faf70b6/IQDyk8xKWRQKRbGz_6v0skl8AVb0i5u5S50vRRH7SabQ38g)", path);
    }

    public void nextRaspCommand(TelegramLongPollingBot bot, Long chatId, UserSettings settings) throws Exception {
        if (!userService.hasDefaultGroup(chatId)) {
            messageUtil.sendMessage(bot, chatId, "⚠️Сначала выберите группу через /setdefaultgroup");
            return;
        }

        messageUtil.sendMessage(bot, chatId, "Идёт получение расписания, пожалуйста подождите⌛");


        String groupForNextDay = userService.getDefaultGroup(chatId).orElseThrow();
        Path pathForNextDay = scheduleService.generateScheduleImage(StorageConfig.scheduleExcel(), groupForNextDay, timeUtil.getDayOfWeekPlusDay(), settings, timeUtil.getMonth());

        messageUtil.sendPhoto(bot, chatId, "🗓️Расписание для " + groupForNextDay + "\n" + timeUtil.getDayOfWeekPlusDay() + ": " + timeUtil.getNumeratorOrDenominator(), pathForNextDay);
    }

    public void zvonkiCommand(TelegramLongPollingBot bot, Long chatId, String path) {
        try (InputStream inputStream = ResourceLoader.resourceStream(path)) {
            messageUtil.sendPhoto(bot, chatId, new InputFile(inputStream, "calls.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDefaultGroupCommand(TelegramLongPollingBot bot, Long chatId) {
        InlineKeyboardMarkup keyboardMarkup = groupKeyboardService.buildKeyboardForGroup(scheduleConfig.getGROUP_NAMES(), 0);
        messageUtil.sendKeyboard(bot, chatId, botConfig.getKeyboardMessage(), keyboardMarkup);
    }

    public void settings(TelegramLongPollingBot bot, Long chatId) {
        UserSettings settings = userSettingService.getSetting(chatId).get();

        String theme = "🖼️Тема: " + (settings.isDarkTheme() ? "🌙Темная" : "☀️Светлая");
        String notification = "🔔Уведомления: " + (settings.isNotification() ? "✅Включены" : "❌Отключены");

        InlineKeyboardMarkup keyboardMarkup = settingsKeyboardService.buildKeyboard();
        messageUtil.sendKeyboard(bot, chatId, "⚙️Меню настроек\nтекущие значения\n" + theme + "\n" + notification, keyboardMarkup);
    }

    public void infoCommand(TelegramLongPollingBot bot, Long chatId) {
        messageUtil.sendMessage(bot, chatId, INFO_MESSAGE);
    }

    public void sendMessage(TelegramLongPollingBot bot, @NotNull Long chatId, List<UserSettings> userSettings, String text) {
        if (chatId.equals(ADMIN_ID)) {
            for (UserSettings settings : userSettings) {
                if (settings.isNotification()) {
                    messageUtil.sendMessage(bot, settings.getChatId(), text);
                }
            }
        } else {
            messageUtil.sendMessage(bot, chatId, "❌У вас недостаточно прав!");
        }
    }

    public void logCommand(TelegramLongPollingBot bot, @NotNull Long chatId) {
        if (chatId.equals(ADMIN_ID)) {
            messageUtil.sendDocument(bot, chatId, "🗒️Отсчет о работе бота:", StorageConfig.logger());
        } else {
            messageUtil.sendMessage(bot, chatId, "❌У вас недостаточно прав!");
        }
    }

    public void usersCommand(TelegramLongPollingBot bot, @NotNull Long chatId) {
        if (chatId.equals(ADMIN_ID)) {
            try {
                Path report = userReportService.generateUsersReport();
                messageUtil.sendDocument(bot, chatId, "📃 Отчёт о пользователях\n👥Используемые группы\n" + userStatsService.generateStats(), report);

                Files.deleteIfExists(report);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            messageUtil.sendMessage(bot, chatId, "❌У вас недостаточно прав!");
        }
    }

    @NotNull
    @Contract("_ -> new")
    private UserInfo createGroupUserInfo(Long chatId) {
        return new UserInfo(
                chatId,
                "Группа",
                "Группа",
                "Группа",
                false
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    private UserInfo createPrivateUserInfo(Long chatId, org.telegram.telegrambots.meta.api.objects.User from) {
        return new UserInfo(
                chatId,
                from.getUserName(),
                from.getFirstName(),
                from.getLastName(),
                false
        );
    }
}
