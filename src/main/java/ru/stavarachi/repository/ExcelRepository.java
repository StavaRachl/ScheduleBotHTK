package ru.stavarachi.repository;

public interface ExcelRepository {
    int findTargetGroup(String sheetName, String targetGroup);

    int findTargetDay(String sheetName, String targetDay);

    String findTargetSheet(String targetGroup);
}
