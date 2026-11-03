package ru.stavarachi.factory.components;

import ru.stavarachi.application.service.schedule.ScheduleDataService;
import ru.stavarachi.application.service.schedule.ScheduleService;

public record ScheduleComponent(
        ScheduleDataService scheduleDataService,
        ScheduleService scheduleService
) {
}
