package ru.stavarachi.handler;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.infrastructure.telegram.keyboards.DayOfWeekKeyboardService;
import ru.stavarachi.port.Handler;

public class DayOfWeekCallbackHandler implements Handler<String> {
    private final MessageUtil messageUtil;
    private final DayOfWeekKeyboardService dayOfWeekKeyboardService;

    public DayOfWeekCallbackHandler(MessageUtil messageUtil, DayOfWeekKeyboardService dayOfWeekKeyboardService) {
        this.messageUtil = messageUtil;
        this.dayOfWeekKeyboardService = dayOfWeekKeyboardService;
    }

    @Override
    public String handle(@NotNull Update update, TelegramLongPollingBot bot) throws Exception {
        if (!update.hasCallbackQuery()) {
            return null;
        }

        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();

        if (!data.startsWith("day:")) {
            return null;
        }

        messageUtil.answerCallbackQuery(bot, callbackQuery, null, false);

        switch (data) {
            case "day:Понедельник" -> {
                return "Понедельник";
            }

            case "day:Вторник" -> {
                return "Вторник";
            }

            case "day:Среда" -> {
                return "Среда";
            }

            case "day:Четверг" -> {
                return "Четверг";
            }

            case "day:Пятница" -> {
                return "Пятница";
            }

            case "day:Суббота" -> {
                return "Суббота";
            }
        }

        return null;
    }
}
