package ru.tinkoff.edu.java.bot.common.telegram.bot;

import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HandlerManager {

    private final Map<String, HandlerCommand> commandHandlers;
    private final Map<TgChatId, HandlerCommand> tgChatHandlers = new HashMap<>();

    public HandlerManager() {
        this.commandHandlers = new HashMap<>();
    }

    public HandlerManager(final Map<String, HandlerCommand> commandHandlers) {
        this.commandHandlers = Objects.requireNonNull(commandHandlers);
    }

    public HandlerCommand getHandler(TgChatId tgChatId, String command) {

        this.tgChatHandlers.putIfAbsent(tgChatId, this.commandHandlers.getOrDefault(
                command, new UnknownCommandHandlerCommand()));

        return this.tgChatHandlers.get(tgChatId);
    }
}
