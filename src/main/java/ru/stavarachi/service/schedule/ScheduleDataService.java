package ru.stavarachi.service.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.infrastructure.excel.ApachePoiChangeReader;
import ru.stavarachi.infrastructure.excel.ApachePoiScheduleReader;
import ru.stavarachi.handler.ClientHandler;
import ru.stavarachi.domain.schedule.Change;
import ru.stavarachi.domain.schedule.Pair;
import ru.stavarachi.infrastructure.excel.ExcelChangeService;
import ru.stavarachi.infrastructure.excel.ExcelService;

import java.nio.file.Path;
import java.util.List;

public class ScheduleDataService {
    private static final Logger log = LoggerFactory.getLogger(ScheduleDataService.class);

    private final ClientHandler clientHandler;
    private final ScheduleConfig scheduleConfig;
    private final ApachePoiChangeReader poiChangeReader;
    private final ApachePoiScheduleReader apachePoiScheduleReader;
    private final ExcelService excelService;
    private final ExcelChangeService excelChangeService;

    public ScheduleDataService(ClientHandler clientHandler, ScheduleConfig scheduleConfig, ApachePoiChangeReader poiChangeReader, ApachePoiScheduleReader apachePoiScheduleReader, ExcelService excelService, ExcelChangeService excelChangeService) {
        this.clientHandler = clientHandler;
        this.scheduleConfig = scheduleConfig;
        this.poiChangeReader = poiChangeReader;
        this.apachePoiScheduleReader = apachePoiScheduleReader;
        this.excelService = excelService;
        this.excelChangeService = excelChangeService;
    }

    public ScheduleData loadScheduleData(Path excelPath, String group , String day, String month) throws Exception {
        log.info("Start loading schedule data...");


        String sheet = apachePoiScheduleReader.findTargetSheet(group);
        int row = apachePoiScheduleReader.findTargetDay(sheet, day);
        int col = apachePoiScheduleReader.findTargetGroup(sheet, group);

        List<Pair> pairList = excelService.loadPair(excelPath, sheet, day, group, month, row, col);

        clientHandler.getChange();

        int pairsChangeVariable = poiChangeReader.findChangeVariable(scheduleConfig.getPAIRS_CHANGE());
        int classroomChangeVariable = poiChangeReader.findChangeVariable(scheduleConfig.getCLASSROOM_CHANGE());

        int pairsEnd = sectionEnd(pairsChangeVariable, classroomChangeVariable);
        int classroomEnd = sectionEnd(classroomChangeVariable, pairsChangeVariable);

        int startRowPairsChange = poiChangeReader.findTargetGroup(group, pairsChangeVariable, pairsEnd);
        int startRowClassroomChange = poiChangeReader.findTargetGroup(group, classroomChangeVariable, classroomEnd);

        List<Change> changeOfPair = excelChangeService.getChangeOfPair(group, scheduleConfig.getPAIRS_CHANGE(), startRowPairsChange);
        List<Change> changeOfClassroom = excelChangeService.getChangeOfPair(group, scheduleConfig.getCLASSROOM_CHANGE(), startRowClassroomChange);

        List<Object> listOfPairsWithBreaks = excelService.loadPairWithBreaks(pairList, day);

        return new ScheduleData(
                listOfPairsWithBreaks,
                changeOfPair,
                changeOfClassroom
        );
    }

    private int sectionEnd(int start, int otherHeader) {
        return  otherHeader > start ? otherHeader : Integer.MAX_VALUE;
    }
}
