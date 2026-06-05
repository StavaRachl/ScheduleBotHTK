package ru.stavarachi.config;

public class BotConfig {
    private final String KEYBOARD_MESSAGE = "выберите группу: ";
    private final String INFO_MESSAGE = """
                📋Информация о боте
                🧑‍💻Разработчик: @StavaRaChiii
                🛎️Версия: 6.6
                ❗Проект не является официальным продуктом КГБ ПОУ "Хабаровский Технический Колледж"
                """;

    public String getKeyboardMessage() {
        return KEYBOARD_MESSAGE;
    }

    public String getINFO_MESSAGE() {
        return INFO_MESSAGE;
    }
}
