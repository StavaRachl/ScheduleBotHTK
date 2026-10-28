package ru.stavarachi.factory.components;

import ru.stavarachi.infrastructure.excel.ApachePoiChangeReader;
import ru.stavarachi.infrastructure.excel.ApachePoiScheduleReader;
import ru.stavarachi.infrastructure.excel.ExcelChangeService;
import ru.stavarachi.infrastructure.excel.ExcelService;

public record ExcelComponent(
        ApachePoiScheduleReader apachePoiScheduleReader,
        ApachePoiChangeReader apachePoiChangeReader,
        ExcelService excelService,
        ExcelChangeService excelChangeService
) {
}
