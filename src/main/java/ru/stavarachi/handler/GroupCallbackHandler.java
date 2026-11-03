package ru.stavarachi.handler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stavarachi.application.service.UserService;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.infrastructure.telegram.GroupKeyboardService;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.port.Handler;

@Slf4j
public class GroupCallbackHandler implements Handler<String> {
    private final GroupKeyboardService groupKeyboardService;
    private final UserService userService;
    private final ScheduleConfig scheduleConfig = new ScheduleConfig();
    private final MessageUtil messageUtil;

    public GroupCallbackHandler(UserService userService, GroupKeyboardService groupKeyboardService, MessageUtil messageUtil) {
        this.userService = userService;
        this.groupKeyboardService = groupKeyboardService;
        this.messageUtil = messageUtil;
    }

    @Override
    public String handle(Update update, TelegramLongPollingBot bot) {

        try {
            if (!update.hasCallbackQuery()) return null;

            CallbackQuery callbackQuery = update.getCallbackQuery();

            String data = update.getCallbackQuery().getData();

            long chatId = update.getCallbackQuery().getMessage().getChatId();

            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            messageUtil.answerCallbackQuery(bot, callbackQuery, null, false);

            if (data.startsWith("group_page:")) {

                int page = Integer.parseInt(
                        data.substring("group_page:".length())
                );

                if (page < 0) page = 0;

                EditMessageReplyMarkup edit = new EditMessageReplyMarkup();

                edit.setChatId(chatId);

                edit.setMessageId(messageId);

                edit.setReplyMarkup(
                        groupKeyboardService.buildKeyboardForGroup(
                                scheduleConfig.getGROUP_NAMES(),
                                page
                        )
                );

                bot.execute(edit);

                return null;
            }

            if (data.startsWith("group:")) {

                String group = data.substring("group:".length());

                userService.setDefaultGroup(chatId, group);

                bot.execute(
                        org.telegram.telegrambots.meta.api.methods.send.SendMessage
                                .builder()
                                .chatId(String.valueOf(chatId))
                                .text("✅ Группа " + group + " выбрана по умолчанию")
                                .build()
                );

            }

        } catch (TelegramApiException e) {

            log.error("Telegram Error: ", e);

        } catch (Exception e) {

            log.error("Error: ", e);
        }

        return null;
    }
}
