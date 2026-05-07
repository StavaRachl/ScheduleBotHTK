package ru.stavarachi.repository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.ScheduleConfig;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelRepository {
    ScheduleConfig scheduleConfig = new ScheduleConfig();
    String[] listOfSheet = scheduleConfig.getLIST_OF_SHEET();
    private static final Logger log = LoggerFactory.getLogger(ExcelRepository.class);

    public int findTargetDay(String path, String sheetName, String targetDay) {
        try (FileInputStream fileInputStream = new FileInputStream(path)){
            int rowIndex = 0;
            DataFormatter formatter = new DataFormatter();
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);

                String text = formatter.formatCellValue(cell);

                if (text.equals(targetDay)) {
                    rowIndex = row.getRowNum();
                }
            }
            return rowIndex;
        } catch (IOException e) {
            log.error("Error: ", e);
            return 0;
        }
    }

    public int findTargetGroup(String path, String sheetName, String targetGroup) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            int colIndex = 0;
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            DataFormatter formatter = new DataFormatter();

            Sheet sheet = workbook.getSheet(sheetName);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    String group = formatter.formatCellValue(cell);

                    if (group.equals(targetGroup)) {
                        colIndex = cell.getColumnIndex();
                    }
                }
            }
            return colIndex;
        } catch (Exception e) {
            log.error("Error", e);
            return 0;
        }
    }

    public String findTargetSheet(String path, String targetGroup) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            String targetSheet = "";

            Workbook workbook = new XSSFWorkbook(fileInputStream);
            DataFormatter formatter = new DataFormatter();

            for (String sheetName : listOfSheet) {
                Sheet sheet = workbook.getSheet(sheetName);

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        String group = formatter.formatCellValue(cell);

                        if (group.equals(targetGroup)) {
                            return targetSheet = sheetName;
                        }
                    }
                }
            }
            return targetSheet;
        } catch (Exception e) {
            log.error("Error: ", e);
            return "";
        }
    }
}
