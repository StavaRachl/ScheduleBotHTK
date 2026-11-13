package ru.stavarachi.handler;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.application.service.FeedbackService;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.infrastructure.telegram.FeedbackKeyboardService;
import ru.stavarachi.port.Handler;

public class FeedbackCallbackHandler implements Handler<String> {
    private final MessageUtil messageUtil;
    private final FeedbackKeyboardService feedbackKeyboardService;
    private final FeedbackService feedbackService;

    public FeedbackCallbackHandler(MessageUtil messageUtil, FeedbackKeyboardService feedbackKeyboardService, FeedbackService feedbackService) {
        this.messageUtil = messageUtil;
        this.feedbackKeyboardService = feedbackKeyboardService;
        this.feedbackService = feedbackService;
    }

    @Override
    public String handle(@NotNull Update update, TelegramLongPollingBot bot) throws Exception {
        if (!update.hasCallbackQuery()) {
            return null;
        }

        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();

        if (!data.startsWith("feedback:")) {
            return null;
        }

        return null;
    }
}
