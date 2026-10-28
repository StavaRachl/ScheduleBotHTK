package ru.stavarachi.infrastructure.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.domain.schedule.Break;
import ru.stavarachi.domain.schedule.Change;
import ru.stavarachi.domain.schedule.Pair;
import ru.stavarachi.port.ScheduleReader;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelService implements ScheduleReader<Pair> {
    ScheduleConfig scheduleConfig = new ScheduleConfig();

    private static final String MONDAY = "Понедельник";
    private static final String TALK_ABOUT_IMPORTANT = "Разговоры о важном";
    private static final String BREAK = "Перемена";
    private static final String FIRS_COURSE_SHEET = "1,2";

    String[] timeSlotsForMonth = scheduleConfig.getTIME_SLOTS_FOR_JUNE();
    String[] timeSlotsForMonday = scheduleConfig.getTIME_SLOTS_FOR_MONDAY();
    String[] timeSlots = scheduleConfig.getTIME_SLOTS();
    String[] breaksSlotsForMonday = scheduleConfig.getBREAKS_TIME_SLOTS_FOR_MONDAY();
    String[] breaksSlots = scheduleConfig.getBREAKS_TIME_SLOTS();

    @Override
    public List<Pair> loadPair(Path path, String targetSheet, String targetDay, String targetGroup, String targetMonth, int rowIndex, int colIndex) {
        try (FileInputStream fileInputStream = new FileInputStream(String.valueOf(path))) {
            List<Pair> listOfPair = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheet(targetSheet);

            if (targetDay.equals(MONDAY) && !targetMonth.equals("JUNE")) {
                Pair pair = new Pair(1, TALK_ABOUT_IMPORTANT, timeSlotsForMonday[0]);
                listOfPair.add(pair);
            }

            if (targetDay.equals(MONDAY) && !targetMonth.equals("JUNE")) {
                for (int i = 1; i <= maxPairCount(targetSheet, targetDay); i++) {
                    System.out.println(maxPairCount(targetSheet, targetDay));

                    Row row = sheet.getRow(rowIndex - 1 + i);
                    Cell cell = row.getCell(colIndex);

                    String textOfPair = formatter.formatCellValue(cell);

                    if (!textOfPair.isBlank()) {
                        Pair pair = new Pair(i, textOfPair, timeSlotsForMonday[i]);
                        listOfPair.add(pair);
                    }
                }
            } else {
                for (int i = 1; i <= maxPairCount(targetSheet, targetDay); i++) {
                    System.out.println(maxPairCount(targetSheet, targetDay));

                    Row row = sheet.getRow(rowIndex -1 + i);
                    Cell cell = row.getCell(colIndex);

                    String pairText = formatter.formatCellValue(cell);

                    if (!pairText.isBlank()) {
                        Pair pair = new Pair(i, pairText, timeSlots[i - 1]);
                        listOfPair.add(pair);
                    }
                }
            }
            return listOfPair;
        } catch (Exception e) {
            log.error("Error: ", e);
            return null;
        }
    }

    public List<Object> loadPairWithBreaks(@NotNull List<Pair> listOfPair, String targetDay) {

        List<Object> listOfSchedule = new ArrayList<>();

        for (int i = 0; i < listOfPair.size(); i++) {

            Pair currentPair = listOfPair.get(i);

            listOfSchedule.add(currentPair);

            if (targetDay.equals(MONDAY)) {

                if (i < listOfPair.size() - 1) {

                    Break breaks = new Break(
                            BREAK,
                            breaksSlotsForMonday[i]
                    );

                    listOfSchedule.add(breaks);
                }

            } else if (i < listOfPair.size() - 1) {

                Break breaks = new Break(
                        BREAK,
                        breaksSlots[i]
                );

                listOfSchedule.add(breaks);
            }
        }

        return listOfSchedule;
    }

    public List<Object> loadPairWithChange(List<Change> listPairWithPairChange, List<Change> listPairWithChangeClassroom) {
        List<Object> listPairWithChange = new ArrayList<>();

        listPairWithChange.addAll(listPairWithPairChange);

        listPairWithChange.addAll(listPairWithChangeClassroom);

        return listPairWithChange;
    }

    @Contract(pure = true)
    private int maxPairCount(@NotNull String targetSheet, String targetDay) {
        return (targetSheet.equals(FIRS_COURSE_SHEET) && targetDay.equals(MONDAY)) ? 3 : 4;
    }
}
