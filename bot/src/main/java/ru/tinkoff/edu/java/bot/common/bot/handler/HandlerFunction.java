package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

/**
 * Интерфейс, описывающий обработчика бота
 */
@FunctionalInterface
public interface HandlerFunction {

    SendMessage handle(BotRequest botRequest);
}
