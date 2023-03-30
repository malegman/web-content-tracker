package ru.tinkoff.edu.java.bot.common.telegram.bot;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

class DefaultCommandHandlerFactory implements CommandHandlerFactory {

    private final String command;
    private final Queue<HandlerFunction> handlerFunctionQueue;

    DefaultCommandHandlerFactory(final Builder builder) {
        this.command = Objects.requireNonNull(builder.command);
        this.handlerFunctionQueue = Objects.requireNonNull(builder.handlerFunctionQueue);
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public CommandHandler apply(final CommandHandlerManager commandHandlerManager) {
        return new CommandHandler(commandHandlerManager, new PriorityQueue<>(this.handlerFunctionQueue));
    }
}
