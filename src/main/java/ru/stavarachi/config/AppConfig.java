package ru.stavarachi.config;

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {
    private final Dotenv dotenv = Dotenv.configure()
            .directory("./")
            .load();

    private final String tokenMain = dotenv.get("TELEGRAM_TOKEN");
    private final String tokenDev = dotenv.get("TELEGRAM_TOKEN_DEV");
    private final String userNameMain = dotenv.get("BOT_USERNAME_MAIN");
    private final String userNameDev = dotenv.get("BOT_USERNAME_DEV");
    private final long adminId = Long.parseLong(dotenv.get("ADMIN_ID"));

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
}