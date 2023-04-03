package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

/**
 * Интерфейс фабрики обработчиков команд бота
 */
public interface CommandHandlerFactory extends Function<CommandHandlerManager, CommandHandler> {

    String getCommand();

    /**
     * Создает обработчика команды.
     *
     * @param commandHandlerManager менеджер обработчиков команд
     *
     * @return обработчик команды
     */
    @Override
    CommandHandler apply(final CommandHandlerManager commandHandlerManager);

    static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class Builder {

        protected final Queue<CommandInnerHandler> commandInnerHandlerQueue = new PriorityQueue<>();
        protected String command;

        public Builder command(String value) {
            this.command = value;
            return this;
        }

        public Builder addHandlerFunction(final CommandInnerHandler commandInnerHandler) {
            this.commandInnerHandlerQueue.add(commandInnerHandler);
            return this;
        }

        public CommandHandlerFactory build() {
            return new DefaultCommandHandlerFactory(this);
        }
    }
}
