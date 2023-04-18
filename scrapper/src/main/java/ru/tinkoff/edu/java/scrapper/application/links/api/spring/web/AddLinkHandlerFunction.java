package ru.tinkoff.edu.java.scrapper.application.links.api.spring.web;

import org.springframework.http.MediaType;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;
import ru.tinkoff.edu.java.scrapper.common.errors.ValidationFailedException;
import ru.tinkoff.edu.java.scrapper.common.spring.web.AbstractScrapperHandlerFunction;
import ru.tinkoff.edu.java.scrapper.application.links.api.AddLinkApi;
import ru.tinkoff.edu.java.scrapper.application.links.api.AddLinkApi.Result.*;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

import java.net.URI;
import java.util.Objects;

/**
 * Обработчик запроса на добавление отслеживаемой ссылки в чат телеграмма
 */
public final class AddLinkHandlerFunction extends AbstractScrapperHandlerFunction {

    private static final TransactionDefinition TRANSACTION_DEFINITION;

    static {
        final var transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("scrapper.links.add");

        TRANSACTION_DEFINITION = transactionDefinition;
    }
    
    private final AddLinkApi addLinkApi;
    
    public AddLinkHandlerFunction(final AddLinkApi addLinkApi) {
        super(TRANSACTION_DEFINITION);
        this.addLinkApi = Objects.requireNonNull(addLinkApi);
    }
    
    @Override
    protected ServerResponse handleInternal(ServerRequest request, TransactionStatus status) throws Exception {

        final var resultMapper = new ResultToServerResponseMapper();
        final var requestPayload = request.body(AddLinkRequest.class);

        this.addLinkApi.invoke(builder -> builder
                    .tgChatId(this.extractHeader(request, "Tg-Chat-Id")
                            .map(TgChatId::valueOf).orElse(null))
                    .link(URI.create(requestPayload.link()).toString()))
                .onFailed(status::setRollbackOnly)
                .visit(resultMapper);

        return resultMapper.serverResponse;
    }

    private static final class ResultToServerResponseMapper implements Visitor {

        private ServerResponse serverResponse;

        @Override
        public void onSuccess(Success result) {

            final var link = result.link();

            this.serverResponse = ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new LinkResponse(
                            link.id().value(),
                            link.url()));
        }

        @Override
        public void onValidationFailed(ValidationFailed result) {
            throw new ValidationFailedException(result.validation());
        }

        @Override
        public void onLinkAlreadyExists(LinkAlreadyExists result) {

            this.serverResponse = SR_CONFLICT;
        }
    }
}
