package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.HandlerFunction;

import java.util.Objects;
import java.util.Queue;

public class CommandHandler implements HandlerFunction {

    private final CommandHandlerManager commandHandlerManager;
    private final Queue<CommandInnerHandler> commandInnerHandlerQueue;

    public CommandHandler(final CommandHandlerManager commandHandlerManager,
                          final Queue<CommandInnerHandler> commandInnerHandlerQueue) {
        this.commandHandlerManager = Objects.requireNonNull(commandHandlerManager);
        this.commandInnerHandlerQueue = Objects.requireNonNull(commandInnerHandlerQueue);
    }

    @Override
    public SendMessage handle(TgChatId tgChatId, Message message) {
        final var result = this.commandInnerHandlerQueue.peek().innerHandle(tgChatId, message);

        switch (result.resultType()) {
            case SUCCESS -> this.commandInnerHandlerQueue.remove();
            case REPEAT -> {}
            case ABORT -> this.commandInnerHandlerQueue.clear();
        }

        if (this.commandInnerHandlerQueue.isEmpty()) {
            this.commandHandlerManager.removeHandler(tgChatId, this);
        }

        return result.sendMessage();
    }
}
