package ru.stavarachi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class BotCommandService {
    private final Logger log = LoggerFactory.getLogger(BotCommandService.class);

    public void register(TelegramLongPollingBot bot) {
        List<BotCommand> botCommands = List.of(
                new BotCommand("/start", "Запуск бота"),
                new BotCommand("/rasp", "Получить расписание"),
                new BotCommand("/nextrasp", "Получить расписание на день вперед"),
                new BotCommand("/setdefaultgroup", "Выбрать группу"),
                new BotCommand("/zvonki", "Получить расписание звонков"),
                new BotCommand("/info", "Информация о боте")
        );

        try {
            bot.execute(new SetMyCommands(botCommands, null, null));
        } catch (TelegramApiException e) {
            log.error("Telegram Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
