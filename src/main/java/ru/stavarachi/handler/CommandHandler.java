package ru.stavarachi.handler;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.stavarachi.config.*;
import ru.stavarachi.model.User;
import ru.stavarachi.service.GroupKeyboardService;
import ru.stavarachi.service.ScheduleService;
import ru.stavarachi.service.UserSettingService;
import ru.stavarachi.service.telegram.CallService;
import ru.stavarachi.service.telegram.InfoService;
import ru.stavarachi.service.telegram.SixSevenService;
import ru.stavarachi.util.MessageUtil;
import ru.stavarachi.util.TimeUtil;

import java.nio.file.Path;

public class CommandHandler {
    private final UserSettingService userSettingService;
    private final ScheduleService scheduleService;
    private final InfoService infoService;
    private final CallService callService;
    private final SixSevenService sixSevenService;
    private final GroupKeyboardService groupKeyboardService;
    private final MessageUtil messageUtil;
    private final TimeUtil timeUtil;

    private final PathConfig pathConfig = new PathConfig();
    private final ScheduleConfig scheduleConfig = new ScheduleConfig();
    private final BotConfig botConfig = new BotConfig();
    private final AppConfig appConfig = new AppConfig();

    public CommandHandler(UserSettingService userSettingService, ScheduleService scheduleService, InfoService infoService, CallService callService, SixSevenService sixSevenService, GroupKeyboardService groupKeyboardService, MessageUtil messageUtil, TimeUtil timeUtil) {
        this.userSettingService = userSettingService;
        this.scheduleService = scheduleService;
        this.infoService = infoService;
        this.callService = callService;
        this.sixSevenService = sixSevenService;
        this.groupKeyboardService = groupKeyboardService;
        this.messageUtil = messageUtil;
        this.timeUtil = timeUtil;
    }
    private final String callsPath = pathConfig.getCallsPath();
    private final String callsJunePath = pathConfig.getCallsPathJune();
    private final String catPath = pathConfig.getCatPath();

    public void handle(Update update, TelegramLongPollingBot bot) throws Exception {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String command = update.getMessage().getText().split(" ")[0];
        long chatId = update.getMessage().getChatId();

        User user = userSettingService.getUser(chatId);

        if (command.contains("@")) {
            command = command.substring(0, command.indexOf("@"));
        }

        switch (command) {
            case "/start":
                messageUtil.sendMessage(bot, chatId, "Бот запущен.");
                messageUtil.sendMessage(bot, chatId, "ВНИМАНИЕ! Перед использованием бота просим вас установить необходимую вам группу командой /setdefaultgroup.");
                break;
            case "/rasp":
                if (!userSettingService.hasDefaultGroup(chatId)) {
                    messageUtil.sendMessage(bot, chatId, "Сначала выберите группу через /setdefaultgroup");
                    break;
                }

                messageUtil.sendMessage(bot, chatId, "Идёт получение расписания, пожалуйста подождите⌛");

                String group = userSettingService.getDefaultGroup(chatId);
                Path path = scheduleService.generateScheduleImage(StorageConfig.scheduleExcel(), group, timeUtil.getDayOfWeek(), user, timeUtil.getMonth());

                messageUtil.sendPhoto(bot, chatId, "Расписание для " + group + " на " + timeUtil.getDayOfWeek() + ": " + timeUtil.getNumeratorOrDenominator() + "\n<a href=\"" + appConfig.getChangeInSchedule() + "\">Изменения в расписании</a>", path);
                break;
            case "/nextrasp":
                if (!userSettingService.hasDefaultGroup(chatId)) {
                    messageUtil.sendMessage(bot, chatId, "Сначала выберите группу через /setdefaultgroup");
                    break;
                }

                messageUtil.sendMessage(bot, chatId, "Идёт получение расписания, пожалуйста подождите⌛");


                String groupForNextDay = userSettingService.getDefaultGroup(chatId);
                Path pathForNextDay = scheduleService.generateScheduleImage(StorageConfig.scheduleExcel(), groupForNextDay, timeUtil.getDayOfWeekPlusDay(), user, timeUtil.getMonth());

                messageUtil.sendPhoto(bot, chatId, "Расписание для " + groupForNextDay + " на " + timeUtil.getDayOfWeekPlusDay() + ": " + timeUtil.getNumeratorOrDenominator(), pathForNextDay);
                break;
            case "/setdefaultgroup":
                InlineKeyboardMarkup keyboardMarkup = groupKeyboardService.buildKeyboardForGroup(scheduleConfig.getGROUP_NAMES(), 0);
                messageUtil.sendKeyboard(bot, chatId, botConfig.getKeyboardMessage(), keyboardMarkup);
                break;
            case "/settheme":
                userSettingService.toggleTheme(chatId);
                messageUtil.sendMessage(bot, chatId, "Тема сменена");
                break;
            case "/zvonki":
                callService.sendCall(bot, chatId, callsPath);
                break;
            case "/zvonkijune":
                callService.sendCall(bot, chatId, callsJunePath);
                break;
            case "/info":
                infoService.sendInfo(bot, chatId);
                break;
            case "/67":
                sixSevenService.sendSixSeven(bot, chatId, catPath);
                break;
        }
    }
}
