package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

/**
 * Обработчик бота. Реализует {@link HandlerFunction}.<br>
 * Обрабатывает сообщения без текста.
 */
public final class NullCommandHandlerFunction implements HandlerFunction {

    @Override
    public SendMessage handle(BotRequest botRequest) {
        return new SendMessage(botRequest.tgChatId().value(), "Я принимаю только текстовые сообщения.");
    }
}
