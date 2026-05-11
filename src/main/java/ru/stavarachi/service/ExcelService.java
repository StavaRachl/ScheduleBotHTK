package ru.stavarachi.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.model.Break;
import ru.stavarachi.model.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelService {
    ScheduleConfig scheduleConfig = new ScheduleConfig();

    String[] timeSlotsForMonday = scheduleConfig.getTIME_SLOTS_FOR_MONDAY();
    String[] timeSlots = scheduleConfig.getTIME_SLOTS();
    String[] breaksSlotsForMonday = scheduleConfig.getBREAKS_TIME_SLOTS_FOR_MONDAY();
    String[] breaksSlots = scheduleConfig.getBREAKS_TIME_SLOTS();

    private static final Logger log = LoggerFactory.getLogger(ExcelService.class);

    public List<Pair> loadPair(String path, String targetSheet, String targetDay, String targetGroup, int rowIndex, int colIndex) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            List<Pair> listOfPair = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheet(targetSheet);

            if (targetDay.equals("Понедельник")) {
                Pair pair = new Pair(1, "Разговоры о важном", timeSlotsForMonday[0]);
                listOfPair.add(pair);
            }

            if (targetDay.equals("Понедельник")) {
                for (int i = 1; i <= 3; i++) {
                    Row row = sheet.getRow(rowIndex - 1 + i);
                    Cell cell = row.getCell(colIndex);

                    String textOfPair = formatter.formatCellValue(cell);
                    Pair pair = new Pair(i, textOfPair, timeSlotsForMonday[i]);
                    listOfPair.add(pair);
                }
            } else {
                for (int i = 1; i <= 3; i++) {
                    Row row = sheet.getRow(rowIndex -1 + i);
                    Cell cell = row.getCell(colIndex);

                    String pairText = formatter.formatCellValue(cell);
                    Pair pair = new Pair(i, pairText, timeSlots[i - 1]);
                    listOfPair.add(pair);
                }
            }
            return listOfPair;
        } catch (Exception e) {
            log.error("Error: ", e);
            return null;
        }
    }

    public List<Object> loadPairWithBreaks(List<Pair> listOfPair, String targetDay) {

        List<Object> listOfSchedule = new ArrayList<>();

        for (int i = 0; i < listOfPair.size(); i++) {

            Pair currentPair = listOfPair.get(i);

            listOfSchedule.add(currentPair);

            if (targetDay.equals("Понедельник")) {

                if (i < listOfPair.size() - 1) {

                    Break breaks = new Break(
                            "Перемена",
                            breaksSlotsForMonday[i]
                    );

                    listOfSchedule.add(breaks);
                }

            } else if (i < listOfPair.size() - 1) {

                Break breaks = new Break(
                        "Перемена",
                        breaksSlots[i]
                );

                listOfSchedule.add(breaks);
            }
        }

        return listOfSchedule;
    }
}
