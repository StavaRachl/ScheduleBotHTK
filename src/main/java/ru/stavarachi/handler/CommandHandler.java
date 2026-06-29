package ru.stavarachi.handler;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.config.*;
import ru.stavarachi.model.User;
import ru.stavarachi.service.CommandService;
import ru.stavarachi.service.UserSettingService;

public class CommandHandler implements Handler<Void>{
    private final UserSettingService userSettingService;
    private final CommandService commandService;

    private final PathConfig pathConfig = new PathConfig();

    public CommandHandler(UserSettingService userSettingService, CommandService commandService) {
        this.userSettingService = userSettingService;
        this.commandService = commandService;
    }

    private final String callsPath = pathConfig.getCallsPath();

    public Void handle(Update update, TelegramLongPollingBot bot) throws Exception {
        if (!update.hasMessage() || !update.getMessage().hasText()) return null;

        String command = update.getMessage().getText().split(" ")[0];
        long chatId = update.getMessage().getChatId();

        User user = userSettingService.getUser(chatId);

        if (command.contains("@")) {
            command = command.substring(0, command.indexOf("@"));
        }

        switch (command) {
            case "/start":
                commandService.startCommand(bot, chatId);
                break;
            case "/rasp":
                commandService.raspCommand(bot, chatId, user);
                break;
            case "/nextrasp":
                commandService.nextRaspCommand(bot, chatId, user);
                break;
            case "/setdefaultgroup":
                commandService.setDefaultGroupCommand(bot, chatId);
                break;
            case "/settheme":
                commandService.setThemeCommand(bot, chatId);
                break;
            case "/zvonki":
                commandService.zvonkiCommand(bot, chatId, callsPath);
                break;
            case "/info":
                commandService.infoCommand(bot, chatId);
                break;
            case "/log":
                commandService.logCommand(bot, chatId);
                break;
            case "/users":
                commandService.usersCommand(bot, chatId);
                break;
        }
        return null;
    }
}
