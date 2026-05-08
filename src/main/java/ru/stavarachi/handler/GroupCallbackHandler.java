package ru.stavarachi.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.service.GroupKeyboardService;

public class GroupCallbackHandler {
    GroupKeyboardService groupKeyboardService = new GroupKeyboardService();
    ScheduleConfig scheduleConfig = new ScheduleConfig();
    private static final Logger log = LoggerFactory.getLogger(GroupCallbackHandler.class);

    public String handle(Update update, TelegramLongPollingBot bot) {
        try {
            String group;
            if (!update.hasCallbackQuery()) return "";

            String data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (data.startsWith("group:")) {
                int page = Integer.parseInt(data.substring(11));

                if (page < 0) page = 0;

                EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
                edit.setChatId(chatId);
                edit.setMessageId(messageId);
                edit.setReplyMarkup(groupKeyboardService.buildKeyboardForGroup(scheduleConfig.getGROUP_NAMES(), page));

                bot.execute(edit);
            }

            if (data.startsWith("group:")) {
                group = data.substring(6);
                return group;
            }
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
            return "";
        } catch (Exception e) {
            log.error("Error: ", e);
            return "";
        }
        return "";
    }
}
