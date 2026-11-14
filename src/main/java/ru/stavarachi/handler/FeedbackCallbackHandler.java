package ru.stavarachi.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.stavarachi.application.service.FeedbackService;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.domain.feedback.RequestStatus;
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

        if (data.isBlank() || !data.startsWith("feedback:")) {
            return null;
        }

        Message message = getMessage(update);

        if (message == null || message.getText().isEmpty()) {
            messageUtil.answerCallbackQuery(bot, callbackQuery, "Сообщение недоступно", true);

            return null;
        }

        int messageId = message.getMessageId();
        Long groupChatId = message.getChatId();

        String[] lines = message.getText().split("\\R");

        if (lines.length != 2) {
            messageUtil.answerCallbackQuery(bot, callbackQuery, "Некорректный формат заявки", true);
            return null;
        }

        try {
            Long chatId = Long.parseLong(lines[0].split(": ", 2)[1].trim());
            Integer id = Integer.parseInt(lines[1].split(": ", 2)[1].trim());

            messageUtil.answerCallbackQuery(bot, callbackQuery, null, false);

            switch (data) {
                case "feedback:approved" -> {
                    feedbackService.setRequestStatus(id, RequestStatus.APPROVED);

                    InlineKeyboardMarkup markup = feedbackKeyboardService.build();
                    messageUtil.editMessage(bot, groupChatId, messageId, "✅Заявка одобрена", markup);

                    messageUtil.sendMessage(bot, chatId, "✅Ваша заявка принята и скоро поступит в работу");
                }

                case "feedback:reject" -> {
                    feedbackService.setRequestStatus(id, RequestStatus.REJECTED);

                    InlineKeyboardMarkup markup = feedbackKeyboardService.build();
                    messageUtil.editMessage(bot, groupChatId, messageId, "❌Заявка отклонена", markup);

                    messageUtil.sendMessage(bot, chatId, "❌Ваша заявка отклонена");
                }

                default -> {}
            }
        } catch (Exception e) {
            messageUtil.answerCallbackQuery(bot, callbackQuery, "Не удалось обработать данные заявки", true);
            throw new RuntimeException(e);
        }

        return null;
    }

    @Nullable
    private Message getMessage(@NotNull Update update) {
        if (!update.hasCallbackQuery()) {
            return null;
        }

        MaybeInaccessibleMessage maybeInaccessibleMessage = update
                .getCallbackQuery()
                .getMessage();

        return maybeInaccessibleMessage instanceof Message message
                ? message
                : null;
    }
}
