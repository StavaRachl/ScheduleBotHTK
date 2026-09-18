package ru.stavarachi.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.config.ScheduleConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class PoiScheduleReader implements ScheduleReader{
    private final FileInputStream fileInputStream;
    private final Workbook workbook;
    private final DataFormatter dataFormatter;
    private final ScheduleConfig scheduleConfig;

    public PoiScheduleReader(@NotNull Path path, ScheduleConfig scheduleConfig) throws IOException {
        this.fileInputStream = new FileInputStream(path.toFile());
        this.scheduleConfig = scheduleConfig;
        dataFormatter = new DataFormatter();
        workbook = new XSSFWorkbook(fileInputStream);
    }

    @Override
    public int findTargetGroup(String sheetName, String targetGroup) {
        int collIndex = 0;

        Sheet sheet = workbook.getSheet(sheetName);

        for (Row row : sheet) {
            for (Cell cell : row) {
                String group = dataFormatter.formatCellValue(cell);

                if (group.equals(targetGroup)) {
                    collIndex = cell.getColumnIndex();
                }
            }
        }
        return collIndex;
    }

    @Override
    public int findTargetDay(String sheetName, String targetDay) {
        int rowIndex = 0;

        Sheet sheet = workbook.getSheet(sheetName);

        for (Row row : sheet) {
            Cell cell = row.getCell(0);

            String text = dataFormatter.formatCellValue(cell);

            if (text.equals(targetDay)) {
                rowIndex = row.getRowNum();
            }
        }
        return rowIndex;
    }

    @Override
    public String findTargetSheet(String targetGroup) {
        String targetSheet = "";

        for (String sheetName : scheduleConfig.getLIST_OF_SHEET()) {
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    String group = dataFormatter.formatCellValue(cell);

                    if (group.equals(targetGroup)) {
                        return targetSheet = sheetName;
                    }
                }
            }
        }

        return targetSheet;
    }
}
