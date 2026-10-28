package ru.stavarachi.application;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.stavarachi.config.AppConfig;
import ru.stavarachi.config.ConfigLoader;
import ru.stavarachi.config.DatabaseConfig;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.database.DatabaseInitializer;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.factory.BotApplicationFactory;
import ru.stavarachi.factory.DatabaseFactory;
import ru.stavarachi.factory.components.DatabaseComponent;

@Slf4j
public class Application {
    public void initialize() {
        try {
            final Logger log = LoggerFactory.getLogger(Application.class);
            StorageConfig.initialize();
            AppConfig appConfig = new AppConfig();

            ConfigLoader configLoader = new ConfigLoader();
            DatabaseConfig databaseConfig = configLoader.database();

            DatabaseComponent databaseComponent = DatabaseFactory.create(databaseConfig);

            databaseComponent.databaseInitializer().initialize();

            log.info("initializing telegram bot...");

            String token = appConfig.getTokenMain();
            String userName = appConfig.getUserNameMain();

            BotApplication bot = BotApplicationFactory.create(token, userName, databaseComponent);

            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);

            log.info("bot success initialize");
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
