package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

/**
 * Интерфейс, описывающий обработчика бота
 */
@FunctionalInterface
public interface HandlerFunction {

    SendMessage handle(TgChatId tgChatId, Message message);
}
