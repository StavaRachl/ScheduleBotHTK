package ru.stavarachi.infrastructure.telegram.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class FeedbackKeyboardService {
    public InlineKeyboardMarkup build() {
        InlineKeyboardButton approved = InlineKeyboardButton.builder()
                .text("✅Принять заявку")
                .callbackData("feedback:accept")
                .build();

        InlineKeyboardButton reject = InlineKeyboardButton.builder()
                .text("❌Отклонить заявку")
                .callbackData("feedback:reject")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(approved, reject)
                        )
                )
                .build();
    }
}
