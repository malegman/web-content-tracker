package ru.tinkoff.edu.java.scrapper.scrapper.tgchat.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.common.invocation.Invocation;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.domain.id.TgChatId;

import java.util.Objects;

/**
 * API для удаления чата телеграмма
 */
public abstract class DeleteTgChatApi extends Invocation<DeleteTgChatApi.Payload,
        DeleteTgChatApi.Payload.Builder, DeleteTgChatApi.Result, DeleteTgChatApi.Result.Visitor> {

    @Override
    protected Result newValidationFailed(Validation validation) {
        return Result.validationFailed(validation);
    }

    @Override
    protected Payload.Builder newPayloadBuilder() {
        return new Payload.Builder();
    }

    public record Payload(TgChatId id) implements Invocation.Payload {

        public static final String PROP_ID = "id";

        public static final String ERROR_ID = "tg_chat.delete.errors.id";

        private Payload(Builder builder) {
            this(builder.id);
        }

        @Override
        public Validation validate() {

            final var validation = new Validation();

            if (Objects.isNull(this.id)) {
                validation.registerFieldError(PROP_ID, ERROR_ID);
            }

            return validation;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Builder implements Invocation.Payload.Builder<Payload> {

            private TgChatId id;

            public Payload.Builder id(TgChatId value) {
                this.id = value;
                return this;
            }

            @Override
            public Payload build() {
                return new Payload(this);
            }
        }
    }

    public sealed interface Result extends Invocation.Result<Result, Result.Visitor> {

        interface Visitor {

            void onSuccess(Success result);

            void onValidationFailed(ValidationFailed result);

            void onExecutionFailed(ExecutionFailed result);
        }

        enum Success implements Result {
            INSTANCE;

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public void visit(Visitor visitor) {
                visitor.onSuccess(this);
            }
        }

        record ValidationFailed(Validation validation) implements Result {

            @Override
            public void visit(Visitor visitor) {
                visitor.onValidationFailed(this);
            }
        }

        record ExecutionFailed(Exception exception) implements Result {

            @Override
            public void visit(Visitor visitor) {
                visitor.onExecutionFailed(this);
            }
        }

        static Success success() {
            return Success.INSTANCE;
        }

        static ValidationFailed validationFailed(final Validation validation) {
            return new ValidationFailed(validation);
        }

        static ExecutionFailed executionFailed(final Exception exception) {
            return new ExecutionFailed(exception);
        }
    }
}
