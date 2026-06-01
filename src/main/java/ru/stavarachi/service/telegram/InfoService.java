package ru.stavarachi.service.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stavarachi.config.BotConfig;

public class InfoService {
    private static final Logger log = LoggerFactory.getLogger(InfoService.class);
    private final BotConfig botConfig = new BotConfig();
    public void sendInfo(TelegramLongPollingBot bot, long chatId) {
        String info = botConfig.getINFO_MESSAGE();

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
