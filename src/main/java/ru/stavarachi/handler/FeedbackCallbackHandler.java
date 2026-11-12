package ru.stavarachi.handler;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.infrastructure.telegram.FeedbackKeyboardService;
import ru.stavarachi.port.Handler;

public class FeedbackCallbackHandler implements Handler<String> {
    private final MessageUtil messageUtil;
    private final FeedbackKeyboardService feedbackKeyboardService;

    public FeedbackCallbackHandler(MessageUtil messageUtil, FeedbackKeyboardService feedbackKeyboardService) {
        this.messageUtil = messageUtil;
        this.feedbackKeyboardService = feedbackKeyboardService;
    }

    @Override
    public String handle(Update update, TelegramLongPollingBot bot) throws Exception {
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
