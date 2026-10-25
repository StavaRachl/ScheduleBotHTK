package ru.stavarachi.port;

import java.nio.file.Path;
import java.util.List;

public interface ScheduleReader<T> {
    List<T> loadPair(
            Path path,
            String targetSheet,
            String targetDay,
            String targetGroup,
            String targetMonth,
            int rowIndex,
            int colIndex
    );
}
