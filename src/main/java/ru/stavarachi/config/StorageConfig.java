package ru.stavarachi.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

    @Contract(pure = true)
    public static @NotNull Path userJson() {
        return DATA.resolve("users.json");
    }

    @Contract(pure = true)
    public static @NotNull Path scheduleExcel() {
        return DOWNLOADS.resolve("schedule.xlsx");
    }

    @Contract(pure = true)
    public static @NotNull Path changeExcel() {
        return DOWNLOADS.resolve("Книга.xlsx");
    }

    @Contract(pure = true)
    public static @NotNull Path scheduleImage() {
        return GENERATED.resolve("schedule.jpg");
    }

    @Contract(pure = true)
    public static @NotNull Path logger() {
        return LOGS.resolve("error.log");
    }
}
