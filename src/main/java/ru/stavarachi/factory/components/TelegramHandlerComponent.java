package ru.stavarachi.factory.components;

import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;

public record TelegramHandlerComponent(
        CommandHandler commandHandler,
        GroupCallbackHandler groupCallbackHandler
) {
}
