package ru.stavarachi.excel;

public interface ChangeReader {
    int getChangeVariable(String typeOfChange);

    int getTargetGroup(String targetGroup, int startRow);
}
