package ru.stavarachi.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.database.DatabaseInitializer;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.excel.PoiChangeReader;
import ru.stavarachi.excel.PoiScheduleReader;
import ru.stavarachi.handler.ClientHandler;
import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;
import ru.stavarachi.repository.SQLiteUserRepository;
import ru.stavarachi.service.*;
import ru.stavarachi.service.report.UserExcelGenerator;
import ru.stavarachi.service.report.UserReportService;
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
    private final ScheduleConfig scheduleConfig = new ScheduleConfig();

    public BotApplication(String botToken, String userName) throws IOException {
        super(botToken);
        this.userName = userName;
        new BotCommandService().register(this);

        DatabaseManager databaseManager = new DatabaseManager();
        SQLiteUserRepository sqLiteUserRepository = new SQLiteUserRepository(databaseManager);
        PoiScheduleReader poiScheduleReader = new PoiScheduleReader(StorageConfig.scheduleExcel(), scheduleConfig);
        PoiChangeReader poiChangeReader = new PoiChangeReader(StorageConfig.changeExcel());
        ExcelChangeService excelChangeService = new ExcelChangeService(StorageConfig.changeExcel());
        ClientHandler clientHandler = new ClientHandler();
        ExcelService excelService = new ExcelService();
        UserSettingService userSettingService = new UserSettingService(sqLiteUserRepository);
        PathConfig pathConfig = new PathConfig();
        HtmlUtil htmlUtil = new HtmlUtil();
        HtmlDarkThemeUtil htmlDarkThemeUtil = new HtmlDarkThemeUtil();
        ScheduleService scheduleService = new ScheduleService(clientHandler, pathConfig, scheduleConfig, htmlUtil, htmlDarkThemeUtil, poiChangeReader, poiScheduleReader, excelService, excelChangeService);
        GroupKeyboardService groupKeyboardService = new GroupKeyboardService();
        MessageUtil messageUtil = new MessageUtil();
        TimeUtil timeUtil = new TimeUtil();
        UserExcelGenerator excelGenerator = new UserExcelGenerator();
        UserReportService userReportService = new UserReportService(userSettingService, excelGenerator);
        CommandService commandService = new CommandService(messageUtil, timeUtil, userSettingService, scheduleService, groupKeyboardService, userReportService);
        this.commandHandler = new CommandHandler(userSettingService, commandService);
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
