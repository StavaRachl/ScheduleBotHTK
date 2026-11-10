package ru.stavarachi.handler;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.stavarachi.port.Handler;
import ru.stavarachi.application.service.UserSettingService;
import ru.stavarachi.application.util.MessageUtil;
import ru.stavarachi.domain.user.UserSettings;
import ru.stavarachi.infrastructure.telegram.SettingsKeyboardService;

import java.util.Optional;

public class SettingsCallbackHandler implements Handler<String> {
    private final MessageUtil messageUtil;
    private final UserSettingService userSettingService;
    private final SettingsKeyboardService settingsKeyboardService;

    public SettingsCallbackHandler(MessageUtil messageUtil, UserSettingService userSettingService, SettingsKeyboardService settingsKeyboardService) {
        this.messageUtil = messageUtil;
        this.userSettingService = userSettingService;
        this.settingsKeyboardService = settingsKeyboardService;
    }

    @Override
    public String handle(@NotNull Update update, TelegramLongPollingBot bot) throws Exception {
        if (!update.hasCallbackQuery()) {
            return null;
        }

        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();

        if (!data.startsWith("settings:")) {
            return null;
        }


        messageUtil.answerCallbackQuery(bot, callbackQuery, null, false);

        Long chatId = callbackQuery.getMessage().getChatId();
        Optional<UserSettings> settings = userSettingService.getSetting(chatId);
        UserSettings userSettings = settings.get();

        int messageId = callbackQuery.getMessage().getMessageId();

        switch (data) {
            case "settings:theme" -> {
                userSettingService.toggleTheme(chatId);

                String typeTheme = "🖼️Тема: " + (userSettings.isDarkTheme() ? "☀️Светлая" : "🌙Темная");
                String typeNotification = "🔔Уведомления: " + (userSettings.isNotification() ? "✅Включены" : "❌Отключены");

                InlineKeyboardMarkup markup = settingsKeyboardService.buildKeyboard();
                messageUtil.editMessage(bot, chatId, messageId, "⚙️Меню настроек\nтекущие значения\n" + typeTheme + "\n" + typeNotification, markup);
            }
            case "settings:notification" -> {
                userSettingService.toggleNotification(chatId);

                String typeTheme = "🖼️Тема: " + (userSettings.isDarkTheme() ? "🌙Темная" : "☀️Светлая");
                String typeNotification = "🔔Уведомления: " + (userSettings.isNotification() ? "❌Отключены" : "✅Включены");

                InlineKeyboardMarkup markup = settingsKeyboardService.buildKeyboard();
                messageUtil.editMessage(bot, chatId, messageId, "⚙️Меню настроек\nтекущие значения\n" + typeTheme + "\n" + typeNotification, markup);
            }
            default -> {}
        }

        return null;
    }
}
