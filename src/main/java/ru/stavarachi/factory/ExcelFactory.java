package ru.stavarachi.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.config.StorageConfig;
import ru.stavarachi.factory.components.ExcelComponent;
import ru.stavarachi.infrastructure.excel.ApachePoiChangeReader;
import ru.stavarachi.infrastructure.excel.ApachePoiScheduleReader;
import ru.stavarachi.infrastructure.excel.ExcelChangeService;
import ru.stavarachi.infrastructure.excel.ExcelService;

import java.io.IOException;

public final class ExcelFactory {
    private ExcelFactory() {}

    @NotNull
    @Contract("_ -> new")
    public static ExcelComponent create(ScheduleConfig scheduleConfig) throws IOException {
        ApachePoiChangeReader apachePoiChangeReader = new ApachePoiChangeReader(StorageConfig.changeExcel());
        ApachePoiScheduleReader apachePoiScheduleReader = new ApachePoiScheduleReader(StorageConfig.scheduleExcel(), scheduleConfig);

        ExcelService excelService = new ExcelService();
        ExcelChangeService excelChangeService = new ExcelChangeService(StorageConfig.changeExcel());

        return new ExcelComponent(
                apachePoiScheduleReader,
                apachePoiChangeReader,
                excelService,
                excelChangeService
        );
    }
}
