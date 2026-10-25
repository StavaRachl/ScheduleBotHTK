package ru.stavarachi.config;

public class BotConfig {
    private final String KEYBOARD_MESSAGE = "выберите группу: ";
    private final String INFO_MESSAGE = """
                📋Информация о боте
                🧑‍💻Разработчик: @StavaRaChiii
                🛎️Версия: 7.0.3
                ❗Проект не является официальным продуктом КГБ ПОУ "Хабаровский Технический Колледж"
                📃[Политика конфиденциальности](https://telegra.ph/Politika-konfidencialnosti-Telegram-bota-10-01)
                """;

    public String getKeyboardMessage() {
        return KEYBOARD_MESSAGE;
    }

    public String getINFO_MESSAGE() {
        return INFO_MESSAGE;
    }
}
