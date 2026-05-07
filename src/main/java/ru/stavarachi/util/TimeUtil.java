package ru.stavarachi.util;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class TimeUtil {
    public String getDayOfWeek() {
        ZoneId zone = ZoneId.of("Asia/Vladivostok");
        ZonedDateTime now = ZonedDateTime.now(zone);

        DayOfWeek day = now.getDayOfWeek();

        if (now.getHour() > 14) {
            day = day.plus(1);
        }

        if (day == DayOfWeek.SUNDAY) {
            day = DayOfWeek.MONDAY;
        }

        String dayOfWeek = day.getDisplayName(TextStyle.FULL, new Locale("ru"));

        return dayOfWeek.substring(0,1).toUpperCase() + dayOfWeek.substring(1);
    }
}
