package ru.stavarachi.handler;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler<T> {
    T handle(Update update, TelegramLongPollingBot bot) throws Exception;
}
