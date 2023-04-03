package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
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
    public SendMessage handle(BotRequest botRequest) {
        final var result = this.commandInnerHandlerQueue.peek().innerHandle(botRequest);

        switch (result.resultType()) {
            case SUCCESS -> this.commandInnerHandlerQueue.remove();
            case REPEAT -> {}
            case ABORT -> this.commandInnerHandlerQueue.clear();
        }

        if (this.commandInnerHandlerQueue.isEmpty()) {
            this.commandHandlerManager.removeHandler(botRequest.tgChatId(), this);
        }

        return result.sendMessage();
    }
}
