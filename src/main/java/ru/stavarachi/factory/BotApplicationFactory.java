package ru.stavarachi.factory;

import org.jetbrains.annotations.NotNull;
import ru.stavarachi.application.BotApplication;
import ru.stavarachi.config.*;
import ru.stavarachi.factory.components.*;

import java.io.IOException;

public final class BotApplicationFactory {
    private BotApplicationFactory() {}

    @NotNull
    public static BotApplication create(String botToken, String userName, DatabaseComponent databaseComponent) throws IOException {
        ScheduleConfig scheduleConfig = new ScheduleConfig();
        PathConfig pathConfig = new PathConfig();

        ExcelComponent excelComponent = ExcelFactory.create(scheduleConfig);
        ScheduleComponent scheduleComponent = ScheduleFactory.create(pathConfig, scheduleConfig, excelComponent);
        TelegramComponent telegramComponent = TelegramFactory.create(databaseComponent, scheduleComponent);
        TelegramHandlerComponent telegramHandlerComponent = TelegramHandlerFactory.create(telegramComponent);

        return new BotApplication(
                botToken,
                userName,
                telegramHandlerComponent.commandHandler(),
                telegramHandlerComponent.groupCallbackHandler()
        );
    }
}
