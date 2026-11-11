package ru.stavarachi.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.factory.components.TelegramComponent;
import ru.stavarachi.factory.components.TelegramHandlerComponent;
import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;
import ru.stavarachi.handler.SettingsCallbackHandler;

public final class TelegramHandlerFactory {
    private TelegramHandlerFactory() {}

    @NotNull
    @Contract("_ -> new")
    public static TelegramHandlerComponent create(@NotNull TelegramComponent telegramComponent) {

        CommandHandler commandHandler = new CommandHandler(
                telegramComponent.botCommandService(),
                telegramComponent.userSettingService(),
                telegramComponent.commandService()
        );

        GroupCallbackHandler groupCallbackHandler = new GroupCallbackHandler(
                telegramComponent.userService(),
                telegramComponent.groupKeyboardService(),
                telegramComponent.messageUtil()
        );

        SettingsCallbackHandler settingsCallbackHandler = new SettingsCallbackHandler(
                telegramComponent.messageUtil(),
                telegramComponent.userSettingService(),
                telegramComponent.settingsKeyboardService()
        );

        return new TelegramHandlerComponent(
                commandHandler,
                groupCallbackHandler,
                settingsCallbackHandler
        );
    }
}
