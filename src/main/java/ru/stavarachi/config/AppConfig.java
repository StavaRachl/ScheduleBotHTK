package ru.stavarachi.config;

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {
    private final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private String get(String key) {
        String value = System.getenv(key);

        if (value != null && !value.isBlank()) {
            return value;
        }

        return dotenv.get(key);
    }

    private final String urlToChange = get("FILE_URL");
    private final String tokenMain = get("TELEGRAM_TOKEN");
    private final String tokenDev = get("TELEGRAM_TOKEN_DEV");
    private final String userNameMain = get("BOT_USERNAME");
    private final String userNameDev = get("BOT_USERNAME_DEV");
    private final long adminId = Long.parseLong(get("ADMIN_ID"));
    private final String changeInSchedule = get("FILE_URL");

    public DatabaseConfig getDatabaseConfig() {
        return new DatabaseConfig(
                get("POSTGRES_HOST"),
                Integer.parseInt(get("POSTGRES_PORT")),
                get("POSTGRES_DB"),
                get("POSTGRES_USER"),
                get("POSTGRES_PASSWORD")
        );
    }

    public String getTokenMain() {
        return tokenMain;
    }

    public String getTokenDev() {
        return tokenDev;
    }

    public String getUserNameMain() {
        return userNameMain;
    }

    public String getUserNameDev() {
        return userNameDev;
    }

    public long getAdminId() {
        return adminId;
    }

    public String getChangeInSchedule() {
        return changeInSchedule;
    }
}