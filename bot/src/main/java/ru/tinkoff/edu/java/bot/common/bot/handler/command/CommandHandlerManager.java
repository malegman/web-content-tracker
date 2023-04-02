package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.ExitHandlerFunction;
import ru.tinkoff.edu.java.bot.common.bot.handler.HandlerFunction;
import ru.tinkoff.edu.java.bot.common.bot.handler.UnknownCommandHandlerFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class CommandHandlerManager {

    private final Map<String, Function<CommandHandlerManager, CommandHandler>> commandHandlerFactories;
    private final Map<TgChatId, HandlerFunction> tgChatCommandHandlers = new HashMap<>();

    public CommandHandlerManager(final Map<String, Function<CommandHandlerManager, CommandHandler>> commandHandlerFactories) {
        this.commandHandlerFactories = Objects.requireNonNull(commandHandlerFactories);
    }

    public HandlerFunction getHandler(TgChatId tgChatId, String command) {

        if (command.equals("/exit")) {
            return new ExitHandlerFunction(Objects.nonNull(this.tgChatCommandHandlers.remove(tgChatId)));
        }

        if (!this.tgChatCommandHandlers.containsKey(tgChatId) && !this.commandHandlerFactories.containsKey(command)) {
            return new UnknownCommandHandlerFunction();
        }

        if (!this.tgChatCommandHandlers.containsKey(tgChatId)) {
            this.tgChatCommandHandlers.put(tgChatId, this.commandHandlerFactories.get(command).apply(this));
        }

        return this.tgChatCommandHandlers.get(tgChatId);
    }

    public void removeHandler(TgChatId tgChatId, HandlerFunction handlerFunction) {
        this.tgChatCommandHandlers.remove(tgChatId, handlerFunction);
    }
}
