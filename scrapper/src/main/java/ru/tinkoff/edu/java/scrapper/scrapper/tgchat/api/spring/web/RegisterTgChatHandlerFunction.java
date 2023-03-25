package ru.tinkoff.edu.java.scrapper.scrapper.tgchat.api.spring.web;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.common.spring.mvc.AbstractScrapperHandlerFunction;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.scrapper.tgchat.api.RegisterTgChatApi;
import ru.tinkoff.edu.java.scrapper.scrapper.tgchat.api.RegisterTgChatApi.Payload;
import ru.tinkoff.edu.java.scrapper.scrapper.tgchat.api.RegisterTgChatApi.Result.*;

import java.util.Objects;

/**
 * Обработчик запроса на регистрацию чата телеграмма
 */
public final class RegisterTgChatHandlerFunction extends AbstractScrapperHandlerFunction {

    private static final TransactionDefinition TRANSACTION_DEFINITION;

    static {
        final var transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("scrapper.tg_chat.register");

        TRANSACTION_DEFINITION = transactionDefinition;
    }

    private final RegisterTgChatApi registerTgChatApi;

    public RegisterTgChatHandlerFunction(final RegisterTgChatApi registerTgChatApi) {
        super(TRANSACTION_DEFINITION);
        this.registerTgChatApi = Objects.requireNonNull(registerTgChatApi);
    }

    @Override
    protected ServerResponse handleInternal(ServerRequest request, TransactionStatus status) {

        final var resultMapper = new ResultToServerResponseMapper();

        this.registerTgChatApi.invoke(builder -> builder
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
