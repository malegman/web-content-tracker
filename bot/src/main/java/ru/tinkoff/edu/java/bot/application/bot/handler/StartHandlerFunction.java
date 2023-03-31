package ru.tinkoff.edu.java.bot.application.bot.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.bot.api.StartApi;
import ru.tinkoff.edu.java.bot.application.bot.api.StartApi.Result.*;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.telegram.bot.CommandInnerHandler;
import ru.tinkoff.edu.java.bot.common.telegram.bot.HandlerFunction;

import java.util.Objects;

public final class StartHandlerFunction implements CommandInnerHandler {

    private final StartApi startApi;

    public StartHandlerFunction(final StartApi startApi) {
        this.startApi = Objects.requireNonNull(startApi);
    }

    @Override
    public SendMessage handle(final TgChatId tgChatId, final Message message) {

        final var resultMapper = new ResultMapperToSendMessage(tgChatId);

        this.startApi.invoke(builder -> builder
                .tgChatId(tgChatId))
                .visit(resultMapper);

        return resultMapper.sendMessage;
    }

    @Override
    public Result innerHandle(TgChatId tgChatId, Message message) {
        return null;
    }

    private static final class ResultMapperToSendMessage implements Visitor {

        private final TgChatId tgChatId;
        private SendMessage sendMessage;

        private ResultMapperToSendMessage(final TgChatId tgChatId) {
            this.tgChatId = tgChatId;
        }

        @Override
        public void onSuccess(Success result) {
            this.sendMessage = new SendMessage(this.tgChatId.value(), "Start Command");
        }

        @Override
        public void onValidationFailed(ValidationFailed result) {

        }

        @Override
        public void onAlreadyExists(AlreadyExists result) {

        }

        @Override
        public void onExecutionFailed(ExecutionFailed result) {

        }
    }
}
