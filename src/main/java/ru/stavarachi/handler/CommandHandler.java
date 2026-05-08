package ru.stavarachi.handler;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.stavarachi.config.BotConfig;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.repository.ExcelRepository;
import ru.stavarachi.service.GroupKeyboardService;
import ru.stavarachi.service.ScheduleService;
import ru.stavarachi.service.telegram.CallService;
import ru.stavarachi.service.telegram.InfoService;
import ru.stavarachi.service.telegram.SixSevenService;
import ru.stavarachi.util.MessageUtil;
import ru.stavarachi.util.TimeUtil;

public class CommandHandler {
    PathConfig pathConfig = new PathConfig();
    ScheduleConfig scheduleConfig = new ScheduleConfig();
    BotConfig botConfig = new BotConfig();

    String callsPath = pathConfig.getCallsPath();
    String catPath = pathConfig.getCatPath();

    MessageUtil messageUtil = new MessageUtil();
    TimeUtil timeUtil = new TimeUtil();

    ExcelRepository excelRepository = new ExcelRepository();

    ScheduleService scheduleService = new ScheduleService();
    InfoService infoService = new InfoService();
    CallService callService = new CallService();
    SixSevenService sixSevenService = new SixSevenService();
    GroupKeyboardService groupKeyboardService = new GroupKeyboardService();

    public void handle(Update update, TelegramLongPollingBot bot) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String command = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        switch (command) {
            case "/start":
                messageUtil.sendMessage(bot, chatId, "Бот запущен.");
                messageUtil.sendMessage(bot, chatId, "ВНИМАНИЕ! Перед использованием бота просим вас установить необходимую вам группу командой /setdefaultgroup.");
                break;
            case "/rasp":
                String path = scheduleService.generateScheduleImage(pathConfig.getExcelPath(), "ИСП-Д52");
                messageUtil.sendPhoto(bot, chatId, "Расписание для ИСП-Д53", path);
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
