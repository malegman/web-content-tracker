package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.HandlerFunction;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface CommandInnerHandler extends Comparable<CommandInnerHandler> {

    Result innerHandle(TgChatId tgChatId, Message message);

    @Override
    default int compareTo(CommandInnerHandler o) {
        return 0;
    }

    record Result(SendMessage sendMessage, ResultType resultType) {

        public static Builder sendMessage(TgChatId tgChatId, String text) {
            return new Builder(tgChatId, text);
        }

        enum ResultType {
            SUCCESS,
            REPEAT,
            ABORT
        }

        public static final class Builder {

            private final SendMessage sendMessage;

            public Builder(TgChatId tgChatId, String text) {
                this.sendMessage = new SendMessage(tgChatId.value(), text);
            }

            public Builder modifySendMessage(Consumer<SendMessage> sendMessageConsumer) {
                sendMessageConsumer.accept(this.sendMessage);
                return this;
            }

            public Result success() {
                return new Result(this.sendMessage, ResultType.SUCCESS);
            }

            public Result repeat() {
                return new Result(this.sendMessage, ResultType.REPEAT);
            }

            public Result abort() {
                return new Result(this.sendMessage, ResultType.ABORT);
            }
        }
    }
}
