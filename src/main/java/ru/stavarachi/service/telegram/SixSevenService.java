package ru.stavarachi.service.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SixSevenService {
    private static final Logger log = LoggerFactory.getLogger(SixSevenService.class);
    public void sendSixSeven(TelegramLongPollingBot bot, long chatId, String path) {
        try {
            SendPhoto photo = new SendPhoto();

            photo.setPhoto(new InputFile(path));
            photo.setCaption("Котость");
            photo.setChatId(chatId);

            bot.execute(photo);
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
