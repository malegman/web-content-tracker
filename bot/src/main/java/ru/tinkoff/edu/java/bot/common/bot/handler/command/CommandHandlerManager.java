package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Компонент для управления обработки команд бота.
 */
public final class CommandHandlerManager {

    private final Map<String, Function<CommandHandlerManager, CommandHandler>> commandHandlerFactories;
    private final Map<TgChatId, HandlerFunction> tgChatCommandHandlers = new HashMap<>();

    public CommandHandlerManager(final Map<String, Function<CommandHandlerManager, CommandHandler>> commandHandlerFactories) {
        this.commandHandlerFactories = Objects.requireNonNull(commandHandlerFactories);
    }

    /**
     * Возвращает обработчик для данного чата и данной команды.
     *
     * @param tgChatId идентификатор чата
     * @param command  команда для выполнения
     *
     * @return обработчик команды чата
     */
    public HandlerFunction getHandler(TgChatId tgChatId, String command) {

        if (Objects.isNull(command)) {
            return new NullCommandHandlerFunction();
        }

        if (command.equals("/exit")) {
            return new ExitHandlerFunction(Objects.nonNull(this.tgChatCommandHandlers.remove(tgChatId)));
        }

        if (this.tgChatCommandHandlers.containsKey(tgChatId)) {
            if (this.commandHandlerFactories.containsKey(command)) {
                // Если отправлена команда во время исполнения команды
                return new ExecuteAnotherCommandHandlerFunction();
            }
        } else {
            if (this.commandHandlerFactories.containsKey(command)) {
                // Если отправлена команда во время простоя текущего чата
                this.tgChatCommandHandlers.put(tgChatId, this.commandHandlerFactories.get(command).apply(this));
            } else {
                // Если отправлен неизвестная команда во время простоя текущего чата
                return new UnknownCommandHandlerFunction();
            }
        }

        return this.tgChatCommandHandlers.get(tgChatId);
    }

    /**
     * Удалят обработчик из текущих обработчиков чата
     *
     * @param tgChatId        идентификатор чата
     * @param handlerFunction обработчик, который необходимо удалить
     */
    public void removeHandler(TgChatId tgChatId, HandlerFunction handlerFunction) {
        this.tgChatCommandHandlers.remove(tgChatId, handlerFunction);
    }
}
