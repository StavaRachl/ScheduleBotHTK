package ru.stavarachi.factory.components;

import ru.stavarachi.handler.CommandHandler;
import ru.stavarachi.handler.GroupCallbackHandler;
import ru.stavarachi.handler.SettingsCallbackHandler;

public record TelegramHandlerComponent(
        CommandHandler commandHandler,
        GroupCallbackHandler groupCallbackHandler,
        SettingsCallbackHandler settingsCallbackHandler
) {
}
