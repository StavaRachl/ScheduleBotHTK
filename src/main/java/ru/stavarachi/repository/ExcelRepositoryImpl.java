package ru.stavarachi.repository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.config.ScheduleConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class ExcelRepositoryImpl implements ExcelRepository{
    private final FileInputStream fileInputStream;
    private final Workbook workbook;
    private final DataFormatter dataFormatter;
    ScheduleConfig scheduleConfig = new ScheduleConfig();
    String[] listOfSheet = scheduleConfig.getLIST_OF_SHEET();
    private static final Logger log = LoggerFactory.getLogger(ExcelRepositoryImpl.class);

    public ExcelRepositoryImpl(Path path) throws IOException {
        this.fileInputStream = new FileInputStream(path.toFile());
        dataFormatter = new DataFormatter();
        workbook = new XSSFWorkbook(fileInputStream);
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
    public int findTargetGroup(String sheetName, String targetGroup) {
        int colIndex = 0;

        Sheet sheet = workbook.getSheet(sheetName);
        for (Row row : sheet) {
            for (Cell cell : row) {
                String group = dataFormatter.formatCellValue(cell);

                if (group.equals(targetGroup)) {
                    colIndex = cell.getColumnIndex();
                }
            }
        }
        return colIndex;
    }

    @Override
    public String findTargetSheet(String targetGroup) {
        String targetSheet = "";

        for (String sheetName : listOfSheet) {
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
