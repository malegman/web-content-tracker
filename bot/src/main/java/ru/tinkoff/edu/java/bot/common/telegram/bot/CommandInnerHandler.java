package ru.tinkoff.edu.java.bot.common.telegram.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

@FunctionalInterface
public interface CommandInnerHandler {

    Result innerHandle(TgChatId tgChatId, Message message);

    record Result(SendMessage sendMessage, ResultType resultType) {

        enum ResultType {
            SUCCESS,
            REPEAT,
            ABORT
        }
    }
}
