package ru.tinkoff.edu.java.scrapper.application.links.api.spring.web;

import org.springframework.http.MediaType;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.common.spring.mvc.AbstractScrapperHandlerFunction;
import ru.tinkoff.edu.java.scrapper.application.links.api.FindLinksApi;
import ru.tinkoff.edu.java.scrapper.application.links.api.FindLinksApi.Result.*;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

import java.util.Objects;

/**
 * Обработчик запроса на получение отслеживаемых ссылок чата телеграмма
 */
public final class FindLinksHandlerFunction extends AbstractScrapperHandlerFunction {

    private static final TransactionDefinition TRANSACTION_DEFINITION;

    static {
        final var transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("scrapper.links.find");

        TRANSACTION_DEFINITION = transactionDefinition;
    }

    private final FindLinksApi findLinksApi;

    public FindLinksHandlerFunction(final FindLinksApi findLinksApi) {
        super(TRANSACTION_DEFINITION);
        this.findLinksApi = Objects.requireNonNull(findLinksApi);
    }

    @Override
    protected ServerResponse handleInternal(ServerRequest request, TransactionStatus status) {

        final var resultMapper = new ResultToServerResponseMapper();

        this.findLinksApi.invoke(builder -> builder
                        .id(this.extractHeader(request, "Tg-Chat-Id")
                                .map(TgChatId::valueOf).orElse(null)))
                .onFailed(status::setRollbackOnly)
                .visit(resultMapper);

        return resultMapper.serverResponse;
    }

    private final class ResultToServerResponseMapper implements Visitor {

        private ServerResponse serverResponse;

        @Override
        public void onSuccess(Success result) {

            final var links = result.links();

            this.serverResponse = ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ListLinksResponse(
                            links.stream()
                                    .map(linkDto -> new LinkResponse(
                                            linkDto.id().value(),
                                            linkDto.url()))
                                    .toList(),
                            links.size()));
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
