package ru.stavarachi.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.config.*;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.infrastructure.excel.ApachePoiChangeReader;
import ru.stavarachi.infrastructure.excel.ApachePoiScheduleReader;
import ru.stavarachi.handler.ClientHandler;
import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;
import ru.stavarachi.infrastructure.excel.ExcelChangeService;
import ru.stavarachi.infrastructure.excel.ExcelService;
import ru.stavarachi.infrastructure.repository.PostgresUserRepository;
import ru.stavarachi.infrastructure.telegram.BotCommandService;
import ru.stavarachi.infrastructure.telegram.CommandService;
import ru.stavarachi.infrastructure.telegram.GroupKeyboardService;
import ru.stavarachi.service.*;
import ru.stavarachi.service.report.UserExcelGenerator;
import ru.stavarachi.service.report.UserReportService;
import ru.stavarachi.service.report.UserStatsService;
import ru.stavarachi.service.schedule.ScheduleDataService;
import ru.stavarachi.service.schedule.ScheduleService;
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

        ConfigLoader configLoader = new ConfigLoader();
        DatabaseConfig databaseConfig = configLoader.database();
        DatabaseManager databaseManager = new DatabaseManager(databaseConfig);
        PostgresUserRepository postgresUserRepository = new PostgresUserRepository(databaseManager);
        ApachePoiScheduleReader apachePoiScheduleReader = new ApachePoiScheduleReader(StorageConfig.scheduleExcel(), scheduleConfig);
        ApachePoiChangeReader poiChangeReader = new ApachePoiChangeReader(StorageConfig.changeExcel());
        ExcelChangeService excelChangeService = new ExcelChangeService(StorageConfig.changeExcel());
        ClientHandler clientHandler = new ClientHandler();
        ExcelService excelService = new ExcelService();
        UserSettingService userSettingService = new UserSettingService(postgresUserRepository);
        PathConfig pathConfig = new PathConfig();
        HtmlUtil htmlUtil = new HtmlUtil();
        ScheduleDataService scheduleDataService = new ScheduleDataService(clientHandler, scheduleConfig, poiChangeReader, apachePoiScheduleReader, excelService, excelChangeService);
        HtmlDarkThemeUtil htmlDarkThemeUtil = new HtmlDarkThemeUtil();
        ScheduleService scheduleService = new ScheduleService(pathConfig, scheduleDataService, htmlUtil, htmlDarkThemeUtil);
        GroupKeyboardService groupKeyboardService = new GroupKeyboardService();
        MessageUtil messageUtil = new MessageUtil();
        UserStatsService userStatsService = new UserStatsService(userSettingService);
        TimeUtil timeUtil = new TimeUtil();
        UserExcelGenerator excelGenerator = new UserExcelGenerator();
        UserReportService userReportService = new UserReportService(userSettingService, excelGenerator);
        CommandService commandService = new CommandService(messageUtil, timeUtil, userSettingService, scheduleService, groupKeyboardService, userReportService, userStatsService);
        this.commandHandler = new CommandHandler(userSettingService, commandService);
        this.groupCallbackHandler = new GroupCallbackHandler(userSettingService, groupKeyboardService, messageUtil);
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
