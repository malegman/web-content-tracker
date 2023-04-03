package ru.tinkoff.edu.java.bot.common.bot;

import com.pengrad.telegrambot.model.Update;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

public class BotUtils {

    public static BotRequest requestFromUpdate(Update update) {

        final var callbackQuery = update.callbackQuery();
        final var message = callbackQuery != null ? callbackQuery.message() : update.message();
        final var tgChatId = TgChatId.valueOf(message.chat().id());
        final var messageText = callbackQuery != null ? callbackQuery.data() : message.text();

        return new BotRequest(tgChatId, messageText);
    }
}
