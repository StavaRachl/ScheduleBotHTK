package ru.stavarachi.service.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.stavarachi.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserExcelGenerator {
    public Path generateReport(List<User> userList) throws IOException {
        Path file = Files.createTempFile("user-reports-", ".xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();

            font.setBold(true);
            style.setFont(font);

            Row header = sheet.createRow(0);

            createCell(header, 0, "ID", style);
            createCell(header, 1, "Группа", style);
            createCell(header, 2, "Темная тема", style);
            createCell(header, 3, "User name", style);

            for (int i = 0; i < userList.size(); i++) {
                User user = userList.get(i);

                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getGroup());
                row.createCell(2).setCellValue(user.isDarkTheme() ? "Да" : "Нет");

            }

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            try (OutputStream outputStream = Files.newOutputStream(file)) {
                workbook.write(outputStream);
            };
        }
        return file;
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}
