package ru.tinkoff.edu.java.scrapper.application.links.api.spring.web;

import org.springframework.http.MediaType;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.application.links.api.DeleteLinkApi;
import ru.tinkoff.edu.java.scrapper.application.links.api.DeleteLinkApi.Payload;
import ru.tinkoff.edu.java.scrapper.application.links.api.DeleteLinkApi.Result.*;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.common.errors.ValidationFailedException;
import ru.tinkoff.edu.java.scrapper.common.spring.web.AbstractScrapperHandlerFunction;

import java.net.URI;
import java.util.Objects;

/**
 * Обработчик запроса на удаление отслеживаемой ссылки чата телеграмма
 */
public final class DeleteLinkHandlerFunction extends AbstractScrapperHandlerFunction {

    private static final TransactionDefinition TRANSACTION_DEFINITION;

    static {
        final var transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("scrapper.links.add");

        TRANSACTION_DEFINITION = transactionDefinition;
    }
    
    private final DeleteLinkApi deleteLinkApi;
    
    public DeleteLinkHandlerFunction(final DeleteLinkApi deleteLinkApi) {
        super(TRANSACTION_DEFINITION);
        this.deleteLinkApi = Objects.requireNonNull(deleteLinkApi);
    }
    
    @Override
    protected ServerResponse handleInternal(ServerRequest request, TransactionStatus status) {

        final var resultMapper = new ResultToServerResponseMapper();

        this.deleteLinkApi.invoke(builder -> builder
                        .tgChatId(this.extractHeader(request, "Tg-Chat-Id")
                                .map(TgChatId::valueOf).orElse(null))
                        .link(URI.create(request.param(Payload.PROP_LINK).orElse("")).toString()))
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
                            LinkId.valueFrom(link.id()),
                            link.url()));
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
