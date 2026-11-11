package ru.stavarachi.handler;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.application.service.UserSettingService;
import ru.stavarachi.config.*;
import ru.stavarachi.domain.user.UserSettings;
import ru.stavarachi.infrastructure.telegram.BotCommandService;
import ru.stavarachi.infrastructure.telegram.CommandService;
import ru.stavarachi.port.Handler;

import java.util.Optional;

public class CommandHandler implements Handler<Void> {
    private final BotCommandService botCommandService;
    private final UserSettingService userSettingService;
    private final CommandService commandService;

    private final PathConfig pathConfig = new PathConfig();

    public CommandHandler(BotCommandService botCommandService, UserSettingService userSettingService, CommandService commandService) {
        this.botCommandService = botCommandService;
        this.userSettingService = userSettingService;
        this.commandService = commandService;
    }

    private final String callsPath = pathConfig.getCallsPath();

    public Void handle(@NotNull Update update, TelegramLongPollingBot bot) throws Exception {
        botCommandService.register(bot);
        if (!update.hasMessage() || !update.getMessage().hasText()) return null;

        String command = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        Optional<UserSettings> optionalUserSettings = userSettingService.getSetting(chatId);

        if (optionalUserSettings.isEmpty()) {
            commandService.startCommand(bot, update, chatId);
            return null;
        }

        UserSettings userSettings = optionalUserSettings.get();

        String[] commandSplit = command.split(" ", 2);
        String cmd = commandSplit[0];

        if (cmd.contains("@")) {
            cmd = cmd.substring(0, cmd.indexOf("@"));
        }

        switch (cmd) {
            case "/start" -> commandService.startCommand(bot, update, chatId);
            case "/rasp" -> commandService.raspCommand(bot, chatId, userSettings);
            case "/nextrasp" -> commandService.nextRaspCommand(bot, chatId, userSettings);
            case "/setdefaultgroup" -> commandService.setDefaultGroupCommand(bot, chatId);
            case "/settings" -> commandService.settings(bot, chatId);
            case "/zvonki" -> commandService.zvonkiCommand(bot, chatId, callsPath);
            case "/info" -> commandService.infoCommand(bot, chatId);
            case "/log" -> commandService.logCommand(bot, chatId);
            case "/users" -> commandService.usersCommand(bot, chatId);
            case "/send" -> commandService.sendMessage(bot, chatId, userSettingService.getAll(), commandSplit[1]);
            default -> {}
        }
        return null;
    }
}
