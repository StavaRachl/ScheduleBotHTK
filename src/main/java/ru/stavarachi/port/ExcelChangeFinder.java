package ru.stavarachi.port;

public interface ExcelChangeFinder {
    int findChangeVariable(String typeOfChange);

    int findTargetGroup(String targetGroup, int startRow, int endRow);
}
