package ru.stavarachi.repository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class ExcelChangeRepository {
    private final Path path;
    private final DataFormatter dataFormatter = new DataFormatter();

    public ExcelChangeRepository(Path path) {
        this.path = path;
    }

    public int getChangeVariable(String typeOfChange) {
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile()); Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheet("замена");
            for (Row row : sheet) {

                if (row == null) {
                    continue;
                }

                String text = dataFormatter.formatCellValue(row.getCell(0));

                if (text.equals(typeOfChange)) {
                    return row.getRowNum();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public int getTargetGroup(String targetGroup, int startRow) {
        int rowIndex = 0;
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile()); Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheet("замена");
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String text = dataFormatter.formatCellValue(row.getCell(0));

                if (text.equals(targetGroup)) {
                    return row.getRowNum();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
