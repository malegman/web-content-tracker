package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

public final class ExitHandlerFunction implements HandlerFunction {

    private final boolean isExistCommand;

    public ExitHandlerFunction(final boolean isExistCommand) {
        this.isExistCommand = isExistCommand;
    }

    @Override
    public SendMessage handle(TgChatId tgChatId, Message message) {
        return new SendMessage(tgChatId.value(), isExistCommand ? "Команда прервана." : "Я ничего сейчас не выполняю.");
    }
}
