package ru.stavarachi.config;

public class PathConfig {
    private static final String catPath = "https://i.pinimg.com/736x/d4/b1/8c/d4b18c06cd194c64ef427fa4e678d3cd.jpg";
    private static final String callsPath = "/images/calls.jpg";
    private static final String callsPathJune = "/images/callsJune.jpg";

    public static String getCatPath() {
        return catPath;
    }

    public static String getCallsPath() {
        return callsPath;
    }

    public static String getCallsPathJune() {
        return callsPathJune;
    }
}
