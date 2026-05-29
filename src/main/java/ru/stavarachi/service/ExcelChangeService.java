package ru.stavarachi.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.stavarachi.model.Change;
import ru.stavarachi.model.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelChangeService {
    private final FileInputStream fileInputStream;
    private final Workbook workbook;
    private final DataFormatter dataFormatter;
    private final Sheet sheet;

    public ExcelChangeService(String path) throws IOException {
        fileInputStream = new FileInputStream(path);
        workbook = new XSSFWorkbook(fileInputStream);
        dataFormatter = new DataFormatter();
        sheet = workbook.getSheet("замена");
    }

    public List<Change> getChangeOfPair(String targetGroup, String typeOfChange, int startRow) {
        List<Change> listOfChangePair = new ArrayList<>();
        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Cell cell = row.getCell(0);
            String text = dataFormatter.formatCellValue(cell);

            if (!text.equals(targetGroup)) {
                break;
            }

            int pairNum = (int) row.getCell(1).getNumericCellValue();
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
        return listOfChangePair;
    }
}
