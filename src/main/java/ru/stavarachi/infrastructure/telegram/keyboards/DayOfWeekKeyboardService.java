package ru.stavarachi.infrastructure.telegram.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class DayOfWeekKeyboardService {
    public InlineKeyboardMarkup build() {
        InlineKeyboardButton monday = InlineKeyboardButton.builder()
                .text("Понедельник")
                .callbackData("day:Понедельник")
                .build();

        InlineKeyboardButton tuesday = InlineKeyboardButton.builder()
                .text("Вторник")
                .callbackData("day:Вторник")
                .build();

        InlineKeyboardButton wednesday = InlineKeyboardButton.builder()
                .text("Среда")
                .callbackData("day:Среда")
                .build();

        InlineKeyboardButton thursday = InlineKeyboardButton.builder()
                .text("Четверг")
                .callbackData("day:Четверг")
                .build();

        InlineKeyboardButton friday = InlineKeyboardButton.builder()
                .text("Пятница")
                .callbackData("day:Пятница")
                .build();

        InlineKeyboardButton saturday = InlineKeyboardButton.builder()
                .text("Суббота")
                .callbackData("day:Суббота")
                .build();

        return new InlineKeyboardMarkup(
                List.of(
                        List.of(monday, tuesday, wednesday),
                        List.of(thursday, friday, saturday)
                )
        );
    }
}
