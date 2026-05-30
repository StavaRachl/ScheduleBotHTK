package ru.stavarachi.util;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
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

    public String getDayOfWeekPlusDay() {
        ZoneId zone = ZoneId.of("Asia/Vladivostok");
        ZonedDateTime now = ZonedDateTime.now(zone);

        DayOfWeek day = now.getDayOfWeek().plus(1);

        if (now.getHour() > 14) {
            day = day.plus(2);
        }

        if (day == DayOfWeek.SUNDAY) {
            day = DayOfWeek.TUESDAY;
        }

        String dayOfWeek = day.getDisplayName(TextStyle.FULL, new Locale("ru"));

        return dayOfWeek.substring(0,1).toUpperCase() + dayOfWeek.substring(1);
    }

    public String getNumeratorOrDenominator() {
        ZoneId zone = ZoneId.of("Asia/Vladivostok");
        ZonedDateTime now = ZonedDateTime.now(zone);

        int week = now.get(WeekFields.ISO.weekOfWeekBasedYear());

        if (week % 2 == 0) {
            return "Числитель";
        } else {
            return "Знаменатель";
        }
    }

    public String getMonth() {
        ZoneId zone = ZoneId.of("Asia/Vladivostok");
        ZonedDateTime now = ZonedDateTime.now(zone);
        return String.valueOf(now.getMonth());
    }
}
