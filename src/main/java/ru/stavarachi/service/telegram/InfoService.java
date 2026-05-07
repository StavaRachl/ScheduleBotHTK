package ru.stavarachi.service.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class InfoService {
    private static final Logger log = LoggerFactory.getLogger(InfoService.class);
    public void sendInfo(TelegramLongPollingBot bot, long chatId) {
        String info = """
                Информация о боте
                Разработчик: @StavaRaChiii
                Версия: 1.0
                """;

        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(info);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
