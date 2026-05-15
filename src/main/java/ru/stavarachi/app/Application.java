package ru.stavarachi.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.stavarachi.config.AppConfig;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public void initialize() {
        try {
            final Logger log = LoggerFactory.getLogger(Application.class);
            AppConfig appConfig = new AppConfig();

            log.info("initializing telegram bot...");

            String token = appConfig.getTokenMain();
            String userName = appConfig.getUserNameMain();

            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new BotApplication(token, userName));

            log.info("bot success initialize");
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
