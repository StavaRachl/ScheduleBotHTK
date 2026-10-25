package ru.stavarachi.port;

import java.util.List;

public interface ScheduleChangeReader<T> {
    List<T> getChangeOfPair(
            String targetGroup,
            String typeOfChange,
            int startRow
    );
}
