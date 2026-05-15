package ru.stavarachi.handler;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.stavarachi.config.AppConfig;
import ru.stavarachi.config.BotConfig;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.model.User;
import ru.stavarachi.repository.ExcelRepository;
import ru.stavarachi.repository.UserGroupRepository;
import ru.stavarachi.service.GroupKeyboardService;
import ru.stavarachi.service.ScheduleService;
import ru.stavarachi.service.UserSettingService;
import ru.stavarachi.service.telegram.CallService;
import ru.stavarachi.service.telegram.InfoService;
import ru.stavarachi.service.telegram.SixSevenService;
import ru.stavarachi.util.MessageUtil;
import ru.stavarachi.util.TimeUtil;

public class CommandHandler {
    private final UserSettingService userSettingService;

    public CommandHandler(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    PathConfig pathConfig = new PathConfig();
    ScheduleConfig scheduleConfig = new ScheduleConfig();
    BotConfig botConfig = new BotConfig();
    AppConfig appConfig = new AppConfig();

    String callsPath = pathConfig.getCallsPath();
    String catPath = pathConfig.getCatPath();

    MessageUtil messageUtil = new MessageUtil();
    TimeUtil timeUtil = new TimeUtil();

    ScheduleService scheduleService = new ScheduleService();
    InfoService infoService = new InfoService();
    CallService callService = new CallService();
    SixSevenService sixSevenService = new SixSevenService();
    GroupKeyboardService groupKeyboardService = new GroupKeyboardService();

    public void handle(Update update, TelegramLongPollingBot bot) {
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
                String path = scheduleService.generateScheduleImage(pathConfig.getExcelPath(), group, timeUtil.getDayOfWeek(), user);

                messageUtil.sendPhoto(bot, chatId, "Расписание для " + group + " на " + timeUtil.getDayOfWeek() + "\n<a href=\"" + appConfig.getChangeInSchedule() + "\">Изменения в расписании</a>", path);
                break;
            case "/nextrasp":
                if (!userSettingService.hasDefaultGroup(chatId)) {
                    messageUtil.sendMessage(bot, chatId, "Сначала выберите группу через /setdefaultgroup");
                    break;
                }

                messageUtil.sendMessage(bot, chatId, "Идёт получение расписания, пожалуйста подождите⌛");


                String groupForNextDay = userSettingService.getDefaultGroup(chatId);
                String pathForNextDay = scheduleService.generateScheduleImage(pathConfig.getExcelPath(), groupForNextDay, timeUtil.getDayOfWeekPlusDay(), user);

                messageUtil.sendPhoto(bot, chatId, "Расписание для " + groupForNextDay + " на " + timeUtil.getDayOfWeekPlusDay(), pathForNextDay);
                break;
            case "/setdefaultgroup":
                InlineKeyboardMarkup keyboardMarkup = groupKeyboardService.buildKeyboardForGroup(scheduleConfig.getGROUP_NAMES(), 0);
                messageUtil.sendKeyboard(bot, chatId, botConfig.getKeyboardMessage(), keyboardMarkup);
                break;
            case "/zvonki":
                callService.sendCall(bot, chatId, callsPath);
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
