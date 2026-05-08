package ru.stavarachi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stavarachi.service.GroupKeyboardService;

import java.io.File;

public class MessageUtil {
    private static final Logger log = LoggerFactory.getLogger(MessageUtil.class);
    public void sendMessage(TelegramLongPollingBot bot, long chatId, String text) {
        try {
            SendMessage sendMessage = new SendMessage();

            sendMessage.setChatId(chatId);
            sendMessage.setText(text);

            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    public void sendPhoto(TelegramLongPollingBot bot, long chatId, String text, String pathToFile) {
        try {
            SendPhoto sendPhoto = new SendPhoto();

            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(new File(pathToFile)));
            sendPhoto.setCaption(text);

            bot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    public void sendDocument(TelegramLongPollingBot bot, long chatId, String text, String pathToFile) {
        try {
            SendDocument sendDocument = new SendDocument();

            sendDocument.setChatId(chatId);

            sendDocument.setDocument(
                    new InputFile(pathToFile)
            );

            sendDocument.setCaption(text);

            bot.execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    public void sendKeyboard(TelegramLongPollingBot bot, long chatId, String text, InlineKeyboardMarkup markup) {
        try {
          SendMessage sendMessage = new SendMessage();

          sendMessage.setChatId(chatId);
          sendMessage.setText(text);
          sendMessage.setReplyMarkup(markup);

          bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
