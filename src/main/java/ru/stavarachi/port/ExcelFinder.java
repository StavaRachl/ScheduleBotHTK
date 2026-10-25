package ru.stavarachi.port;

public interface ExcelFinder {
    int findTargetGroup(String sheetName, String targetGroup);

    int findTargetDay(String sheetName, String targetDay);

    String findTargetSheet(String targetGroup);
}
