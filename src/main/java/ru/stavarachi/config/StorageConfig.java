package ru.stavarachi.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageConfig {
    public static final Path ROOT = Paths.get("runtime");
    public static final Path DOWNLOADS = ROOT.resolve("downloaded");
    public static final Path DATA = ROOT.resolve("data");
    public static final Path GENERATED = ROOT.resolve("generated");
    public static final Path LOGS = ROOT.resolve("logs");

    public static void initialize() throws IOException {
        Files.createDirectories(DOWNLOADS);
        Files.createDirectories(DATA);
        Files.createDirectories(GENERATED);
        Files.createDirectories(LOGS);
    }

    public static Path userJson() {
        return DATA.resolve("users.json");
    }

    public static Path scheduleExcel() {
        return DOWNLOADS.resolve("РАСПИСАНИЕ 2 СЕМЕСТР 25-26 .xlsx");
    }

    public static Path changeExcel() {
        return DOWNLOADS.resolve("Новая таблица (25).xlsx");
    }

    public static Path scheduleImage() {
        return GENERATED.resolve("schedule.jpg");
    }

    public static Path logger() {
        return LOGS.resolve("error.log");
    }
}
