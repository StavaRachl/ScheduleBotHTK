package ru.stavarachi.service.schedule;

import ru.stavarachi.domain.schedule.Change;

import java.util.List;

public record ScheduleData(
        List<Object> pairsWithBreaks,
        List<Change> changePair,
        List<Change> changeClassroom
) {
}
