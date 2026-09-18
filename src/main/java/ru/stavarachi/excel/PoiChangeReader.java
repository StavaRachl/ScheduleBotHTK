package ru.stavarachi.excel;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class PoiChangeReader implements ChangeReader{
    private final Path path;
    private final DataFormatter dataFormatter;
    private static final Logger logger = LoggerFactory.getLogger(PoiChangeReader.class);

    public PoiChangeReader(Path path) {
        this.path = path;
        dataFormatter = new DataFormatter();
    }

    @Override
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
            logger.error("Error: ", e);
        }
        return -1;
    }

    @Override
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
            logger.error("Error: ", e);
        }
        return -1;
    }
}
