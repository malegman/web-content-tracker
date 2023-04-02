package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.HandlerFunction;

import java.util.Objects;

@FunctionalInterface
public interface CommandInnerHandler extends Comparable<HandlerFunction> {

    Result innerHandle(TgChatId tgChatId, Message message);

    @Override
    default int compareTo(HandlerFunction o) {
        return 0;
    }

    record Result(SendMessage sendMessage, ResultType resultType) {

        public static Result success(SendMessage sendMessage) {
            return new Result(sendMessage, ResultType.SUCCESS);
        }

        public static Result repeat(SendMessage sendMessage) {
            return new Result(sendMessage, ResultType.REPEAT);
        }

        public static Result abort(SendMessage sendMessage) {
            return new Result(sendMessage, ResultType.ABORT);
        }

        enum ResultType {
            SUCCESS,
            REPEAT,
            ABORT
        }
    }
}
