package ru.stavarachi.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stavarachi.config.ScheduleConfig;
import ru.stavarachi.service.GroupKeyboardService;
import ru.stavarachi.service.UserSettingService;
import ru.stavarachi.util.MessageUtil;

public class GroupCallbackHandler {
    GroupKeyboardService groupKeyboardService = new GroupKeyboardService();
    ScheduleConfig scheduleConfig = new ScheduleConfig();
    MessageUtil messageUtil = new MessageUtil();
    private final UserSettingService userSettingService;

    public GroupCallbackHandler(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    private static final Logger log = LoggerFactory.getLogger(GroupCallbackHandler.class);

    public String handle(Update update, TelegramLongPollingBot bot) {

        try {

            if (!update.hasCallbackQuery()) return "";

            String data = update.getCallbackQuery().getData();

            long chatId = update.getCallbackQuery().getMessage().getChatId();

            int messageId = update.getCallbackQuery().getMessage().getMessageId();

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

                return "";
            }

            if (data.startsWith("group:")) {

                String group = data.substring("group:".length());

                userSettingService.setDefaultGroup(chatId, group);

                bot.execute(
                        org.telegram.telegrambots.meta.api.methods.send.SendMessage
                                .builder()
                                .chatId(String.valueOf(chatId))
                                .text("✅ Группа " + group + " выбрана по умолчанию")
                                .build()
                );

                return group;
            }

        } catch (TelegramApiException e) {

            log.error("Telegram Error: ", e);

        } catch (Exception e) {

            log.error("Error: ", e);
        }

        return "";
    }
}
