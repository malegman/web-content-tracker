package ru.tinkoff.edu.java.bot.application.client.api.spring.web;

import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.bot.application.client.api.SendUpdatesApi;
import ru.tinkoff.edu.java.bot.application.client.api.SendUpdatesApi.Result.ExecutionFailed;
import ru.tinkoff.edu.java.bot.application.client.api.SendUpdatesApi.Result.Success;
import ru.tinkoff.edu.java.bot.application.client.api.SendUpdatesApi.Result.ValidationFailed;
import ru.tinkoff.edu.java.bot.application.client.api.SendUpdatesApi.Result.Visitor;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.errors.ValidationFailedException;
import ru.tinkoff.edu.java.bot.common.spring.web.AbstractBotHandlerFunction;

import java.util.Objects;

/**
 * Обработчик запроса на рассылку обновления ссылки в указанные телеграмм чаты
 */
public final class SendUpdatesHandlerFunction extends AbstractBotHandlerFunction {

    private final SendUpdatesApi sendUpdatesApi;

    public SendUpdatesHandlerFunction(final SendUpdatesApi sendUpdatesApi) {
        this.sendUpdatesApi = Objects.requireNonNull(sendUpdatesApi);
    }

    @Override
    protected ServerResponse handleInternal(ServerRequest request) throws Exception {

        final var requestPayload = request.body(LinkUpdateRequest.class);
        final var resultMapper = new ResultToServerResponseMapper();

        this.sendUpdatesApi.invoke(builder -> builder
                        .id(LinkId.valueOf(requestPayload.id()))
                        .url(requestPayload.url())
                        .description(requestPayload.description())
                        .tgChatIds(requestPayload.tgChatIds().stream()
                                .map(TgChatId::valueOf)
                                .toList()))
                .visit(resultMapper);

        return resultMapper.serverResponse;
    }

    private static final class ResultToServerResponseMapper implements Visitor {

        private ServerResponse serverResponse;

        @Override
        public void onSuccess(Success result) {
            this.serverResponse = SR_NO_CONTENT;
        }

        @Override
        public void onValidationFailed(ValidationFailed result) {
            throw new ValidationFailedException(result.validation());
        }

        @Override
        public void onExecutionFailed(ExecutionFailed result) {

            final var exception = result.exception();

            // TODO обработка ошибки выполнения операции

            throw new RuntimeException(exception);
        }
    }
}
