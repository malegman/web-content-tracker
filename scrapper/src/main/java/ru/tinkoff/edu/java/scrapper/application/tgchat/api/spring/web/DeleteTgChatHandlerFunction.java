package ru.tinkoff.edu.java.scrapper.application.tgchat.api.spring.web;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.common.spring.web.AbstractScrapperHandlerFunction;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.DeleteTgChatApi;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.DeleteTgChatApi.Payload;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.DeleteTgChatApi.Result.*;

import java.util.Objects;

/**
 * Обработчик запроса на удаление чата телеграмма
 */
public final class DeleteTgChatHandlerFunction extends AbstractScrapperHandlerFunction {

    private static final TransactionDefinition TRANSACTION_DEFINITION;

    static {
        final var transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("scrapper.tg_chat.delete");

        TRANSACTION_DEFINITION = transactionDefinition;
    }
    
    private final DeleteTgChatApi deleteTgChatApi;
    
    public DeleteTgChatHandlerFunction(final DeleteTgChatApi deleteTgChatApi) {
        super(TRANSACTION_DEFINITION);
        this.deleteTgChatApi = Objects.requireNonNull(deleteTgChatApi);
    }
    
    @Override
    protected ServerResponse handleInternal(ServerRequest request, TransactionStatus status) {

        final var resultMapper = new ResultToServerResponseMapper();

        this.deleteTgChatApi.invoke(builder -> builder
                        .id(TgChatId.valueOf(request.pathVariable(Payload.PROP_ID))))
                .onFailed(status::setRollbackOnly)
                .visit(resultMapper);

        return resultMapper.serverResponse;
    }

    private final class ResultToServerResponseMapper implements Visitor {

        private ServerResponse serverResponse;

        @Override
        public void onSuccess(Success result) {
            this.serverResponse = SR_NO_CONTENT;
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
