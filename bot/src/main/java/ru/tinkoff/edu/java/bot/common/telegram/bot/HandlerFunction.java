package ru.tinkoff.edu.java.bot.common.telegram.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

@FunctionalInterface
public interface HandlerFunction extends Comparable<HandlerFunction> {

    SendMessage handle(TgChatId tgChatId, Message message);

    @Override
    default int compareTo(HandlerFunction o) {
        return 0;
    }
}
