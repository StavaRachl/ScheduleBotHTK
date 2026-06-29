package ru.stavarachi.service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.stavarachi.config.*;
import ru.stavarachi.model.User;
import ru.stavarachi.util.MessageUtil;
import ru.stavarachi.util.TimeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class CommandService {
    private final MessageUtil messageUtil;
    private final TimeUtil timeUtil;
    private final UserSettingService userSettingService;
    private final ScheduleService scheduleService;
    private final GroupKeyboardService groupKeyboardService;

    private final ScheduleConfig scheduleConfig = new ScheduleConfig();
    private final BotConfig botConfig = new BotConfig();
    private final AppConfig appConfig = new AppConfig();

    public CommandService(MessageUtil messageUtil, TimeUtil timeUtil, UserSettingService userSettingService, ScheduleService scheduleService, GroupKeyboardService groupKeyboardService) {
        this.messageUtil = messageUtil;
        this.timeUtil = timeUtil;
        this.userSettingService = userSettingService;
        this.scheduleService = scheduleService;
        this.groupKeyboardService = groupKeyboardService;
    }

    public void startCommand(TelegramLongPollingBot bot, Long chatId) {
        messageUtil.sendMessage(bot, chatId, "✅Бот запущен.");
        messageUtil.sendMessage(bot, chatId, "⚠️ВНИМАНИЕ! Перед использованием бота просим вас установить необходимую вам группу командой /setdefaultgroup.");
    }

    public void raspCommand(TelegramLongPollingBot bot, Long chatId, User user) throws Exception {
        if (!userSettingService.hasDefaultGroup(chatId)) {
            messageUtil.sendMessage(bot, chatId, "⚠️Сначала выберите группу через /setdefaultgroup");
            return;
        }

        messageUtil.sendMessage(bot, chatId, "Идёт получение расписания, пожалуйста подождите⌛");

        String group = userSettingService.getDefaultGroup(chatId);
        Path path = scheduleService.generateScheduleImage(StorageConfig.scheduleExcel(), group, timeUtil.getDayOfWeek(), user, timeUtil.getMonth());

        messageUtil.sendPhoto(bot, chatId, "🗓️Расписание для " + group + "\n" + timeUtil.getDayOfWeek() + ": " + timeUtil.getNumeratorOrDenominator() + "\n<a href=\"" + appConfig.getChangeInSchedule() + "\">Изменения в расписании</a>", path);
    }

    public void nextRaspCommand(TelegramLongPollingBot bot, Long chatId, User user) throws Exception {
        if (!userSettingService.hasDefaultGroup(chatId)) {
            messageUtil.sendMessage(bot, chatId, "⚠️Сначала выберите группу через /setdefaultgroup");
            return;
        }

        messageUtil.sendMessage(bot, chatId, "Идёт получение расписания, пожалуйста подождите⌛");


        String groupForNextDay = userSettingService.getDefaultGroup(chatId);
        Path pathForNextDay = scheduleService.generateScheduleImage(StorageConfig.scheduleExcel(), groupForNextDay, timeUtil.getDayOfWeekPlusDay(), user, timeUtil.getMonth());

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

    public void setThemeCommand(TelegramLongPollingBot bot, Long chatId) {
        userSettingService.toggleTheme(chatId);
        messageUtil.sendMessage(bot, chatId, "Тема сменена");
    }

    public void infoCommand(TelegramLongPollingBot bot, Long chatId) {
        messageUtil.sendMessage(bot, chatId, botConfig.getINFO_MESSAGE());
    }

    public void logCommand(TelegramLongPollingBot bot, Long chatId) {
        if (chatId == appConfig.getAdminId()) {
            messageUtil.sendDocument(bot, chatId, "🗒️Отсчет о работе бота:", StorageConfig.logger());
        } else {
            messageUtil.sendMessage(bot, chatId, "❌У вас недостаточно прав!");
        }
    }

    public void usersCommand(TelegramLongPollingBot bot, Long chatId) {
        if (chatId == appConfig.getAdminId()) {
            messageUtil.sendDocument(bot, chatId, "🗒️Информация о пользователях:", StorageConfig.userJson());
        } else {
            messageUtil.sendMessage(bot, chatId, "❌У вас недостаточно прав!");
        }
    }
}
