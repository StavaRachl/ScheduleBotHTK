package ru.stavarachi.repository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class ExcelChangeRepository {
    private final FileInputStream fileInputStream;
    private final Workbook workbook;
    private final DataFormatter dataFormatter;
    private final Sheet sheet;

    public ExcelChangeRepository(Path path) throws IOException {
        fileInputStream = new FileInputStream(path.toFile());
        workbook = new XSSFWorkbook(fileInputStream);
        dataFormatter = new DataFormatter();
        sheet = workbook.getSheet("замена");
    }

    public int getChangeVariable(String typeOfChange) {
        for (Row row : sheet) {

            if (row == null) {
                continue;
            }

            String text = dataFormatter.formatCellValue(row.getCell(0));

            if (text.equals(typeOfChange)) {
                return row.getRowNum();
            }
        }

        return -1;
    }

    public int getTargetGroup(String targetGroup, int startRow) {
        int rowIndex = 0;

        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String text = dataFormatter.formatCellValue(row.getCell(0));

            if (text.equals(targetGroup)) {
                return row.getRowNum();
            }
        }
        return -1;
    }
}
