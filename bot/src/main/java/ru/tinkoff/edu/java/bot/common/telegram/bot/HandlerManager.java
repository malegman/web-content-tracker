package ru.tinkoff.edu.java.bot.common.telegram.bot;

import com.pengrad.telegrambot.model.Update;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HandlerManager {

    private final Map<String, Handler> commandHandlers;
    private final Map<TgChatId, Handler> tgChatHandlers = new HashMap<>();

    public HandlerManager() {
        this.commandHandlers = new HashMap<>();
    }

    public HandlerManager(final Map<String, Handler> commandHandlers) {
        this.commandHandlers = Objects.requireNonNull(commandHandlers);
    }

    public void addCommandHandler(String command, Handler handler) {
        this.commandHandlers.put(command, handler);
    }

    public Handler getHandler(Update update) {

        final var tgChatId = TgChatId.valueOf(update.message().chat().id());

        this.tgChatHandlers.putIfAbsent(tgChatId, this.commandHandlers.getOrDefault(
                update.message().caption(), new UnknownCommandHandler()));

        return this.tgChatHandlers.get(tgChatId);
    }
}
