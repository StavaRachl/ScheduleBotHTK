package ru.stavarachi.factory.components;

import ru.stavarachi.service.schedule.ScheduleDataService;
import ru.stavarachi.service.schedule.ScheduleService;

public record ScheduleComponent(
        ScheduleDataService scheduleDataService,
        ScheduleService scheduleService
) {
}
