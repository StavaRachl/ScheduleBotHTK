package ru.stavarachi.config;

public class PathConfig {
    private final String catPath = "https://i.pinimg.com/736x/d4/b1/8c/d4b18c06cd194c64ef427fa4e678d3cd.jpg";

    private final String callsPath = "./resources/images/calls.jpg";
    private final String excelPath = "./resources/tabels/РАСПИСАНИЕ 2 СЕМЕСТР 25-26 .xlsx";
    private final String jsonPath = "./resources/data/users.json";

    //private final String callsPath = "src/main/resources/images/calls.jpg";
    //private final String excelPath = "src/main/resources/tabels/РАСПИСАНИЕ 2 СЕМЕСТР 25-26 .xlsx";

    //private final String jsonPath = "src/main/resources/data/users.json";

    public String getCallsPath() {
        return callsPath;
    }

    public String getCatPath() {
        return catPath;
    }

    public String getExcelPath() {
        return excelPath;
    }

    public String getJsonPath() {
        return jsonPath;
    }
}
