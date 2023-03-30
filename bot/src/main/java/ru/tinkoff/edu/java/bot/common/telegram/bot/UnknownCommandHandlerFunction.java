package ru.tinkoff.edu.java.bot.common.telegram.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

public class UnknownCommandHandlerFunction implements HandlerFunction {

    @Override
    public SendMessage handle(TgChatId tgChatId, Message message) {
        return new SendMessage(tgChatId.value(), "Unknown command");
    }
}
