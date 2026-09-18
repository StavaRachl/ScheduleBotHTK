package ru.stavarachi.config;

public class ScheduleConfig {
    private final String[] LIST_OF_SHEET = {"1,2", "2,3", "3,4"};

    private final String[] TIME_SLOTS_FOR_JUNE = {
            "8:30 - 9:15 | 9:20 - 10:05",
            "10:15 - 11:00 | 11:05 - 11:50",
            "12:20 - 13:05 | 13:10 - 13:55"
    };

    private final String[] TIME_SLOTS_FOR_MONDAY = {
            "8:30 - 9:15",
            "9:25 - 10:10 | 10:15 - 11:00",
            "11:10 - 11:55 | 12:00 - 12:45",
            "13:25 - 14:10 | 14:15 - 15:00",
            "15:10 - 15:55 | 16:00 - 16:45"
    };

    private final String[] TIME_SLOTS = {
            "8:30 - 9:15 | 9:20 - 10:05",
            "10:15 - 11:00 | 11:05 - 11:50",
            "12:30 - 13:15 | 13:20 - 14:05",
            "14:15 - 15:00 | 15:05 - 15:50"
    };

    private final String[] BREAKS_TIME_SLOTS = {
            "10:05 - 10:15",
            "11:50 - 12:30",
            "14:05 - 14:15"
    };

    private final String[] BREAKS_TIME_SLOTS_FOR_MONDAY = {
            "9:15 - 9:25",
            "10:00 - 10:10",
            "12:45 - 13:25",
            "15:00 - 15:10"
    };

    private final String[] GROUP_NAMES = {
            "ГС-Д41", "ГС-Д51", "ГС-Д61",
            "ДОУ-Д41", "ДОУ-Д51", "ДОУ-Д61",
            "ЗУ-Д31", "ЗУ-Д41", "ЗУ-Д51", "ЗУ-Д52", "ЗУ-Д61", "ЗУ-Д62",
            "ИСП-Д31", "ИСП-Д32", "ИСП-Д41", "ИСП-Д42", "ИСП-Д51", "ИСП-Д52", "ИСП-Д53",
            "МД-Д31", "МД-Д41", "МД-Д51", "МД-Д61",
            "МСУ-Д41", "МСУ-Д51", "МСУ-Д61",
            "МЭО-Д31", "МЭО-Д41", "МЭО-Д51", "МЭО-Д61",
            "ОМД-51", "ОМД-61",
            "ПГ-Д31", "ПГ-Д32", "ПГ-Д41", "ПГ-Д42", "ПГ-Д51", "ПГ-Д52", "ПГ-Д53",
            "ПГ-Д61", "ПГ-Д62", "ПГ-Д63", "ПГ-Д64",
            "РУПО-Д61", "РУПО-Д62", "РУПО-Д63",
            "СИС-Д31",
            "СЭЗ-41", "СЭЗ-51", "СЭЗ-61",
            "СЭЗ-Д31", "СЭЗ-Д41", "СЭЗ-Д42", "СЭЗ-Д51", "СЭЗ-Д52", "СЭЗ-Д61", "СЭЗ-Д62",
            "ТТО-Д61",
            "ЭГС-Д51", "ЭГС-Д61",
            "ЭМС-Д51", "ЭМС-Д61",
            "ЭУ-Д41", "ЭУ-Д51", "ЭУ-Д61"
    };

    private final String PAIRS_CHANGE = "ЗАМЕНА ПАР";

    private final String CLASSROOM_CHANGE = "ЗАМЕНА КАБИНЕТОВ";

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

    public String getPAIRS_CHANGE() {
        return PAIRS_CHANGE;
    }

    public String getCLASSROOM_CHANGE() {
        return CLASSROOM_CHANGE;
    }

    public String[] getTIME_SLOTS_FOR_JUNE() {
        return TIME_SLOTS_FOR_JUNE;
    }
}
