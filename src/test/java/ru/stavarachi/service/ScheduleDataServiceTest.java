package ru.stavarachi.service;

import org.junit.jupiter.api.BeforeEach;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.excel.PoiChangeReader;
import ru.stavarachi.excel.PoiScheduleReader;
import ru.stavarachi.handler.ClientHandler;
import ru.stavarachi.service.schedule.ScheduleDataService;

import static org.mockito.Mockito.mock;

public class ScheduleDataServiceTest {

    private ClientHandler clientHandler;
    private ScheduleConfig scheduleConfig;
    private PoiChangeReader poiChangeReader;
    private PoiScheduleReader poiScheduleReader;
    private ExcelService excelService;
    private ExcelChangeService excelChangeService;

    private ScheduleDataService scheduleDataService;

    @BeforeEach
    void setUp() {
        clientHandler = mock(ClientHandler.class);
        scheduleConfig = mock(ScheduleConfig.class);
        poiChangeReader = mock(PoiChangeReader.class);
        poiScheduleReader = mock(PoiScheduleReader.class);
        excelService = mock(ExcelService.class);
        excelChangeService = mock(ExcelChangeService.class);

        scheduleDataService = new ScheduleDataService(
                clientHandler,
                scheduleConfig,
                poiChangeReader,
                poiScheduleReader,
                excelService,
                excelChangeService
        );
    }
}
