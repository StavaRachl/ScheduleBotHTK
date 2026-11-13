package ru.stavarachi.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.config.PathConfig;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.factory.components.ExcelComponent;
import ru.stavarachi.factory.components.ScheduleComponent;
import ru.stavarachi.handler.ClientHandler;
import ru.stavarachi.application.service.schedule.ScheduleDataService;
import ru.stavarachi.application.service.schedule.ScheduleService;
import ru.stavarachi.application.util.HtmlDarkThemeUtil;
import ru.stavarachi.application.util.HtmlUtil;

import java.io.IOException;

public final class ScheduleFactory {
    private ScheduleFactory() {}

    @NotNull
    @Contract("_, _, _ -> new")
    public static ScheduleComponent create(PathConfig pathConfig, ScheduleConfig scheduleConfig, @NotNull ExcelComponent excelComponent) throws Exception {

        ClientHandler clientHandler = new ClientHandler();
        clientHandler.start();

        HtmlUtil htmlUtil = new HtmlUtil();
        HtmlDarkThemeUtil htmlDarkThemeUtil = new HtmlDarkThemeUtil();

        ScheduleDataService scheduleDataService = new ScheduleDataService(
                clientHandler,
                scheduleConfig,
                excelComponent.apachePoiChangeReader(),
                excelComponent.apachePoiScheduleReader(),
                excelComponent.excelService(),
                excelComponent.excelChangeService()
        );

        ScheduleService scheduleService = new ScheduleService(pathConfig, scheduleDataService, htmlUtil, htmlDarkThemeUtil);

        return new ScheduleComponent(
                scheduleDataService,
                scheduleService
        );
    }
}
