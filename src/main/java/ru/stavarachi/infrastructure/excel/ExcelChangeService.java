package ru.stavarachi.infrastructure.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.domain.schedule.Change;
import ru.stavarachi.port.ScheduleChangeReader;
import ru.stavarachi.port.ScheduleReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelChangeService implements ScheduleChangeReader<Change> {
    private final Path path;
    private final DataFormatter dataFormatter = new DataFormatter();

    public ExcelChangeService(Path path) {
        this.path = path;
    }

    @Override
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
            log.error("Error: ", e);
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
}
