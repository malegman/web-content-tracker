package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

/**
 * Обработчик бота. Реализует {@link HandlerFunction}.<br>
 * Обрабатывает неизвестные команды.
 */
public final class UnknownCommandHandlerFunction implements HandlerFunction {

    @Override
    public SendMessage handle(BotRequest botRequest) {
        return new SendMessage(botRequest.tgChatId().value(), "У меня нет такой команды. /help");
    }
}
