package ru.stavarachi.excel;

public interface ScheduleReader {
    int findTargetGroup(String sheetName, String targetGroup);

    int findTargetDay(String sheetName, String targetDay);

    String findTargetSheet(String targetGroup);
}
