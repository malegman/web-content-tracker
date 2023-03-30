package ru.tinkoff.edu.java.bot.application.bot.api.telegram.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.bot.api.StartApi;
import ru.tinkoff.edu.java.bot.application.bot.api.StartApi.Result.*;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.telegram.bot.HandlerCommand;

import java.util.Objects;

public final class StartHandlerCommand implements HandlerCommand{

    private final StartApi startApi;

    public StartHandlerCommand(final StartApi startApi) {
        this.startApi = Objects.requireNonNull(startApi);
    }

    @Override
    public SendMessage handle(Update update) {

        final var resultMapper = new ResultMapperToSendMessage();

        this.startApi.invoke(builder -> builder
                .tgChatId(TgChatId.valueOf(update.message().chat().id())))
                .visit(resultMapper);

        return resultMapper.sendMessage;
    }

    private static final class ResultMapperToSendMessage implements Visitor {

        private SendMessage sendMessage;

        @Override
        public void onSuccess(Success result) {

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
