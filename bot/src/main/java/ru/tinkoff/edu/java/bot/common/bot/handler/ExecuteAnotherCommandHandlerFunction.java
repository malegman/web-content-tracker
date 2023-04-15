package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

/**
 * Обработчик бота. Реализует {@link HandlerFunction}.<br>
 * Обрабатывает попытку выполнить команду при текущем выполнении команды.
 */
public final class ExecuteAnotherCommandHandlerFunction implements HandlerFunction {

    @Override
    public SendMessage handle(BotRequest botRequest) {
        return new SendMessage(botRequest.tgChatId().value(), """
                                Сейчас выполняется другая команда.
                                Отправьте /exit, чтобы прервать текущую команду.""");
    }
}
