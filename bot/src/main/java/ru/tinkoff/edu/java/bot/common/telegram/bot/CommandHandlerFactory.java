package ru.tinkoff.edu.java.bot.common.telegram.bot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

public interface CommandHandlerFactory extends Function<CommandHandlerManager, CommandHandler> {

    String getCommand();

    @Override
    CommandHandler apply(final CommandHandlerManager commandHandlerManager);

    static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class Builder {

        protected final Queue<HandlerFunction> handlerFunctionQueue = new PriorityQueue<>();
        protected String command;

        public Builder command(String value) {
            this.command = value;
            return this;
        }

        public Builder addHandlerFunction(final HandlerFunction handlerFunction) {
            this.handlerFunctionQueue.add(handlerFunction);
            return this;
        }

        public CommandHandlerFactory build() {
            return new DefaultCommandHandlerFactory(this);
        }
    }
}
