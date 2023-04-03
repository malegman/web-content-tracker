package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

/**
 * Обработчик бота. Реализует {@link HandlerFunction}.<br>
 * Обрабатывает завершение текущей команды.
 */
public final class ExitHandlerFunction implements HandlerFunction {

    private final boolean isExistCommand;

    public ExitHandlerFunction(final boolean isExistCommand) {
        this.isExistCommand = isExistCommand;
    }

    @Override
    public SendMessage handle(BotRequest botRequest) {
        return new SendMessage(botRequest.tgChatId().value(), isExistCommand ? "Команда прервана." : "Я ничего сейчас не выполняю.");
    }
}
