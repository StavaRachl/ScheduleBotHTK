package ru.stavarachi.service.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.excel.PoiChangeReader;
import ru.stavarachi.excel.PoiScheduleReader;
import ru.stavarachi.handler.ClientHandler;
import ru.stavarachi.model.Change;
import ru.stavarachi.model.Pair;
import ru.stavarachi.service.ExcelChangeService;
import ru.stavarachi.service.ExcelService;

import java.nio.file.Path;
import java.util.List;

public class ScheduleDataService {
    private static final Logger log = LoggerFactory.getLogger(ScheduleDataService.class);

    private final ClientHandler clientHandler;
    private final ScheduleConfig scheduleConfig;
    private final PoiChangeReader poiChangeReader;
    private final PoiScheduleReader poiScheduleReader;
    private final ExcelService excelService;
    private final ExcelChangeService excelChangeService;

    public ScheduleDataService(ClientHandler clientHandler, ScheduleConfig scheduleConfig, PoiChangeReader poiChangeReader, PoiScheduleReader poiScheduleReader, ExcelService excelService, ExcelChangeService excelChangeService) {
        this.clientHandler = clientHandler;
        this.scheduleConfig = scheduleConfig;
        this.poiChangeReader = poiChangeReader;
        this.poiScheduleReader = poiScheduleReader;
        this.excelService = excelService;
        this.excelChangeService = excelChangeService;
    }

    public ScheduleData loadScheduleData(Path excelPath, String group , String day, String month) throws Exception {
        log.info("Start loading schedule data...");

        String sheet = poiScheduleReader.findTargetSheet(group);
        int row = poiScheduleReader.findTargetDay(sheet, day);
        int col = poiScheduleReader.findTargetGroup(sheet, group);

        List<Pair> pairList = excelService.loadPair(excelPath, sheet, day, group, month, row, col);

        clientHandler.getChange();

        int pairsChangeVariable = poiChangeReader.getChangeVariable(scheduleConfig.getPAIRS_CHANGE());
        int classroomChangeVariable = poiChangeReader.getChangeVariable(scheduleConfig.getCLASSROOM_CHANGE());

        int startRowPairsChange = poiChangeReader.getTargetGroup(group, pairsChangeVariable);
        int startRowClassroomChange = poiChangeReader.getTargetGroup(group, classroomChangeVariable);

        List<Change> changeOfPair = excelChangeService.getChangeOfPair(group, scheduleConfig.getPAIRS_CHANGE(), startRowPairsChange);
        List<Change> changeOfClassroom = excelChangeService.getChangeOfPair(group, scheduleConfig.getCLASSROOM_CHANGE(), startRowClassroomChange);

        List<Object> listOfPairsWithBreaks = excelService.loadPairWithBreaks(pairList, day);

        return new ScheduleData(
                listOfPairsWithBreaks,
                changeOfPair,
                changeOfClassroom
        );
    }
}
