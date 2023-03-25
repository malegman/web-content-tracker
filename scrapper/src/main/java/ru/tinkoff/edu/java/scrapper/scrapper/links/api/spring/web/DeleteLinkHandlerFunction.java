package ru.tinkoff.edu.java.scrapper.scrapper.links.api.spring.web;

import org.springframework.http.MediaType;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.common.spring.mvc.AbstractScrapperHandlerFunction;
import ru.tinkoff.edu.java.scrapper.scrapper.links.api.DeleteLinkApi;
import ru.tinkoff.edu.java.scrapper.scrapper.links.api.DeleteLinkApi.Result.*;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.domain.id.TgChatId;

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
    protected ServerResponse handleInternal(ServerRequest request, TransactionStatus status) throws Exception {

        final var resultMapper = new ResultToServerResponseMapper();
        final var requestPayload = request.body(RemoveLinkRequest.class);

        this.deleteLinkApi.invoke(builder -> builder
                        .id(this.extractHeader(request, "Tg-Chat-Id")
                                .map(TgChatId::valueOf).orElse(null))
                        .url(requestPayload.url()))
                .onFailed(status::setRollbackOnly)
                .visit(resultMapper);

        return resultMapper.serverResponse;
    }

    private final class ResultToServerResponseMapper implements Visitor {

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
            this.serverResponse = badRequestFromValidation(result.validation());
        }

        @Override
        public void onExecutionFailed(ExecutionFailed result) {

            final var exception = result.exception();

            // TODO обработка ошибки выполнения операции

            throw new RuntimeException(exception);
        }
    }
}
