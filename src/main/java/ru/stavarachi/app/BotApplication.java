package ru.stavarachi.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;
import ru.stavarachi.repository.ExcelRepository;
import ru.stavarachi.service.*;
import ru.stavarachi.service.telegram.CallService;
import ru.stavarachi.service.telegram.InfoService;
import ru.stavarachi.service.telegram.SixSevenService;
import ru.stavarachi.util.HtmlDarkThemeUtil;
import ru.stavarachi.util.HtmlUtil;
import ru.stavarachi.util.MessageUtil;
import ru.stavarachi.util.TimeUtil;

import java.io.IOException;

public class BotApplication extends TelegramLongPollingBot {
    private String userName;
    private final CommandHandler commandHandler;
    private final GroupCallbackHandler groupCallbackHandler;
    private final Logger log = LoggerFactory.getLogger(BotApplication.class);
    private final PathConfig pathConfig = new PathConfig();
    private final String path = pathConfig.getExcelPath();

    public BotApplication(String botToken, String userName) throws IOException {
        this.userName = userName;
        super(botToken);
        new BotCommandService().register(this);

        ExcelRepository excelRepository = new ExcelRepository(path);
        ExcelService excelService = new ExcelService();
        UserSettingService userSettingService = new UserSettingService();
        PathConfig pathConfig = new PathConfig();
        HtmlUtil htmlUtil = new HtmlUtil();
        HtmlDarkThemeUtil htmlDarkThemeUtil = new HtmlDarkThemeUtil();
        ScheduleService scheduleService = new ScheduleService(pathConfig, htmlUtil, htmlDarkThemeUtil, excelService, excelRepository);
        InfoService infoService = new InfoService();
        CallService callService = new CallService();
        SixSevenService sixSevenService = new SixSevenService();
        GroupKeyboardService groupKeyboardService = new GroupKeyboardService();
        MessageUtil messageUtil = new MessageUtil();
        TimeUtil timeUtil = new TimeUtil();
        this.commandHandler = new CommandHandler(userSettingService, scheduleService, infoService, callService, sixSevenService, groupKeyboardService, messageUtil, timeUtil);
        this.groupCallbackHandler = new GroupCallbackHandler(userSettingService, groupKeyboardService);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                commandHandler.handle(update, this);
            }

            if (update.hasCallbackQuery()) {
                groupCallbackHandler.handle(update, this);
            }
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
