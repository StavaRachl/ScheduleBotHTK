package ru.stavarachi.config;

public class ScheduleConfig {
    private final String[] LIST_OF_SHEET = {"1,2", "2,3", "3,4"};

    private final String[] TIME_SLOTS_FOR_MONDAY = {
            "8:30 - 9:15",
            "9:25 - 10:10 | 10:15 - 11:00",
            "11:10 - 11:55 | 12:00 - 12:45",
            "13:25 - 14:10 | 14:15 - 15:00"
    };

    private final String[] TIME_SLOTS = {
            "8:30 - 9:15 | 9:20 - 10:05",
            "10:15 - 11:00 | 11:05 - 11:50",
            "12:30 - 13:15 | 13:20 - 14:05"
    };

    private final String[] BREAKS_TIME_SLOTS = {
            "10:05 - 10:15",
            "11:50 - 12:30"
    };

    private final String[] BREAKS_TIME_SLOTS_FOR_MONDAY = {
            "9:15 - 9:25",
            "10:00 - 10:10",
            "12:45 - 13:25"
    };

    private final String[] GROUP_NAMES = {
            "ГС-Д21", "ГС-Д31", "ГС-Д41", "ГС-Д51",
            "ДОУ-Д31", "ДОУ-Д41", "ДОУ-Д51",
            "ЗУ-Д31", "ЗУ-Д41", "ЗУ-Д42", "ЗУ-Д51", "ЗУ-Д52",
            "ИСП-Д21", "ИСП-Д22", "ИСП-Д31", "ИСП-Д32", "ИСП-Д41", "ИСП-Д42", "ИСП-Д51", "ИСП-Д52", "ИСП-Д53",
            "МД-Д21", "МД-Д31", "МД-Д41", "МД-Д51",
            "МСУ-Д21", "МСУ-Д31", "МСУ-Д41", "МСУ-Д51",
            "МЭО-Д21", "МЭО-Д31", "МЭО-Д41", "МЭО-Д51",
            "ОМД-41", "ОМД-51", "ОМД-Д31",
            "ПГ-Д21", "ПГ-Д22", "ПГ-Д31", "ПГ-Д32", "ПГ-Д41", "ПГ-Д42", "ПГ-Д51", "ПГ-Д52", "ПГ-Д53",
            "СИС-Д31",
            "СЭЗ-31", "СЭЗ-41", "СЭЗ-51", "СЭЗ-Д21", "СЭЗ-Д22", "СЭЗ-Д31", "СЭЗ-Д41", "СЭЗ-Д42", "СЭЗ-Д51", "СЭЗ-Д52",
            "ЭГС-Д41", "ЭГС-Д51", "ЭМС-Д41", "ЭМС-Д51", "ЭУ-Д31", "ЭУ-Д41", "ЭУ-Д51"
    };

    public String[] getLIST_OF_SHEET() {
        return LIST_OF_SHEET;
    }

    public String[] getTIME_SLOTS_FOR_MONDAY() {
        return TIME_SLOTS_FOR_MONDAY;
    }

    public String[] getTIME_SLOTS() {
        return TIME_SLOTS;
    }

    public String[] getBREAKS_TIME_SLOTS() {
        return BREAKS_TIME_SLOTS;
    }

    public String[] getBREAKS_TIME_SLOTS_FOR_MONDAY() {
        return BREAKS_TIME_SLOTS_FOR_MONDAY;
    }

    public String[] getGROUP_NAMES() {
        return GROUP_NAMES;
    }
}
