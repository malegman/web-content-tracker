package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

/**
 * Интерфейс обработчика бота.
 */
@FunctionalInterface
public interface HandlerFunction {

    /**
     * Обрабатывает запрос бота.
     *
     * @param botRequest запрос бота
     *
     * @return сообщение для отправки пользователю
     */
    SendMessage handle(BotRequest botRequest);
}
