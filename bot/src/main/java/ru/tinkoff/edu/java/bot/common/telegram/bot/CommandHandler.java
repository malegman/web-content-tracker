package ru.tinkoff.edu.java.bot.common.telegram.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

import java.util.Objects;
import java.util.Queue;

public class CommandHandler implements HandlerFunction {

    private final CommandHandlerManager commandHandlerManager;
    private final Queue<HandlerFunction> handlerFunctionQueue;

    public CommandHandler(final CommandHandlerManager commandHandlerManager,
                          final Queue<HandlerFunction> handlerFunctionQueue) {
        this.commandHandlerManager = Objects.requireNonNull(commandHandlerManager);
        this.handlerFunctionQueue = Objects.requireNonNull(handlerFunctionQueue);
    }

    @Override
    public SendMessage handle(TgChatId tgChatId, Message message) {
        final var sendMessage = this.handlerFunctionQueue.poll().handle(tgChatId, message);

        if (this.handlerFunctionQueue.isEmpty()) {
            this.commandHandlerManager.removeHandler(tgChatId, this);
        }

        return sendMessage;
    }
}
