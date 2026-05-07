package ru.stavarachi.app;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.handler.CommandHandler;

public class BotApplication extends TelegramLongPollingBot {
    private String userName;
    private final CommandHandler commandHandler;

    public BotApplication(String botToken, String userName) {
        this.userName = userName;
        super(botToken);
        this.commandHandler = new CommandHandler();
    }

    @Override
    public void onUpdateReceived(Update update) {
        commandHandler.handle(update, this);
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
