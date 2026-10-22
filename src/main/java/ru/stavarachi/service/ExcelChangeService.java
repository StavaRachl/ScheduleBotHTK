package ru.stavarachi.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stavarachi.model.Change;
import ru.stavarachi.model.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExcelChangeService {
    private final Path path;
    private final DataFormatter dataFormatter = new DataFormatter();
    private static final Logger logger = LoggerFactory.getLogger(ExcelChangeService.class);
    public ExcelChangeService(Path path) {
        this.path = path;
    }

    public List<Change> getChangeOfPair(String targetGroup, String typeOfChange, int startRow) {
        List<Change> listOfChangePair = new ArrayList<>();
        Object pairNum = null;

        try (FileInputStream fileInputStream = new FileInputStream(path.toFile()); Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = getSheduleSheet(workbook);
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell cell = row.getCell(0);
                String text = dataFormatter.formatCellValue(cell);

                if (!text.equals(targetGroup)) {
                    break;
                }

                Cell pairCell = row.getCell(1);

                switch (pairCell.getCellType()) {
                    case NUMERIC -> pairNum = (int) pairCell.getNumericCellValue();
                    case STRING -> pairNum = pairCell.getStringCellValue();
                    default -> dataFormatter.formatCellValue(cell);
                }

                String pairName = row.getCell(2).getStringCellValue();
                String pairAuthor = row.getCell(3).getStringCellValue();

                Change change = new Change(pairNum, pairName, pairAuthor);
                listOfChangePair.add(change);
            }
            if (listOfChangePair.isEmpty()) {
                listOfChangePair.add(new Change(null, typeOfChange.toLowerCase() + " - нет ", null));
            } else {
                listOfChangePair.addFirst(new Change(null, typeOfChange.toLowerCase(), null));
            }
        } catch (IOException e) {
            logger.error("Error: ", e);
        }
        return listOfChangePair;
    }

    private @NotNull Sheet getSheduleSheet(@NotNull Workbook workbook) {
        Sheet sheet = workbook.getSheet("замена");

        if (sheet == null) {
            sheet = workbook.getSheet("Лист1");
        }

        if (sheet == null) {
            throw new NullPointerException("Не удалось найти ни один лист");
        }

        return sheet;
    }

    private static boolean isNum(@NotNull Row row) {
        String value = row.getCell(1).getStringCellValue();

        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
