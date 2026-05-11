package ru.stavarachi.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;
import ru.stavarachi.service.BotCommandService;
import ru.stavarachi.service.UserSettingService;

public class BotApplication extends TelegramLongPollingBot {
    private String userName;
    private final CommandHandler commandHandler;
    private final GroupCallbackHandler groupCallbackHandler;
    private final UserSettingService userSettingService;
    private final Logger log = LoggerFactory.getLogger(BotApplication.class);

    public BotApplication(String botToken, String userName) {
        this.userName = userName;
        super(botToken);
        new BotCommandService().register(this);
        this.userSettingService = new UserSettingService();
        this.commandHandler = new CommandHandler(userSettingService);
        this.groupCallbackHandler = new GroupCallbackHandler(userSettingService);
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
