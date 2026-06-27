package ru.stavarachi.repository;

public interface ExcelChangeRepository {
    int getChangeVariable(String typeOfChange);

    int getTargetGroup(String targetGroup, int startRow);
}
