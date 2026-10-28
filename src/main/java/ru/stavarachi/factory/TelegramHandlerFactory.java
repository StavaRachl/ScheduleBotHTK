package ru.stavarachi.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.factory.components.TelegramComponent;
import ru.stavarachi.factory.components.TelegramHandlerComponent;
import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;

public final class TelegramHandlerFactory {
    private TelegramHandlerFactory() {}

    @NotNull
    @Contract("_ -> new")
    public static TelegramHandlerComponent create(@NotNull TelegramComponent telegramComponent) {

        CommandHandler commandHandler = new CommandHandler(
                telegramComponent.userSettingService(),
                telegramComponent.commandService()
        );

        GroupCallbackHandler groupCallbackHandler = new GroupCallbackHandler(
                telegramComponent.userSettingService(),
                telegramComponent.groupKeyboardService(),
                telegramComponent.messageUtil()
        );

        return new TelegramHandlerComponent(
                commandHandler,
                groupCallbackHandler
        );
    }
}
