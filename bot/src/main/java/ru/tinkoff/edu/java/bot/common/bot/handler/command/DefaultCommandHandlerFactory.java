package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

final class DefaultCommandHandlerFactory implements CommandHandlerFactory {

    private final String command;
    private final Queue<CommandInnerHandler> commandInnerHandlerQueue;

    DefaultCommandHandlerFactory(final Builder builder) {
        this.command = Objects.requireNonNull(builder.command);
        this.commandInnerHandlerQueue = Objects.requireNonNull(builder.commandInnerHandlerQueue);
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public CommandHandler apply(final CommandHandlerManager commandHandlerManager) {
        return new CommandHandler(commandHandlerManager, new PriorityQueue<>(this.commandInnerHandlerQueue));
    }
}
