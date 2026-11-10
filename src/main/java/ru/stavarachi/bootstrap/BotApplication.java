package ru.stavarachi.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.config.*;
import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;
import ru.stavarachi.handler.SettingsCallbackHandler;

import java.io.IOException;

public class BotApplication extends TelegramLongPollingBot {
    private String userName;
    private final CommandHandler commandHandler;
    private final GroupCallbackHandler groupCallbackHandler;
    private final SettingsCallbackHandler settingsCallbackHandler;

    private final Logger log = LoggerFactory.getLogger(BotApplication.class);
    private final ScheduleConfig scheduleConfig = new ScheduleConfig();

    public BotApplication(String botToken, String userName, CommandHandler commandHandler, GroupCallbackHandler groupCallbackHandler, SettingsCallbackHandler settingsCallbackHandler) throws IOException {
        super(botToken);

        this.userName = userName;
        this.commandHandler = commandHandler;
        this.groupCallbackHandler = groupCallbackHandler;
        this.settingsCallbackHandler = settingsCallbackHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                commandHandler.handle(update, this);
            }

            if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();

                if (data.startsWith("group:") || data.startsWith("group_page:")) {
                    groupCallbackHandler.handle(update, this);
                }

                if (data.startsWith("settings:")) {
                    settingsCallbackHandler.handle(update, this);
                }
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
