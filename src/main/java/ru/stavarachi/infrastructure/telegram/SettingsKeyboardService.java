package ru.stavarachi.infrastructure.telegram;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class SettingsKeyboardService {
    public InlineKeyboardMarkup buildKeyboard() {
        InlineKeyboardButton theme = InlineKeyboardButton.builder()
                .text("Переключить тему")
                .callbackData("settings:theme")
                .build();

        InlineKeyboardButton notification = InlineKeyboardButton.builder()
                .text("Переключить уведомления")
                .callbackData("settings:notification")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(theme),
                                List.of(notification)
                        )
                )
                .build();
    }
}
