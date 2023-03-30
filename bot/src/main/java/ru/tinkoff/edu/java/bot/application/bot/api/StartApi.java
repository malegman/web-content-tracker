package ru.tinkoff.edu.java.bot.application.bot.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.api.invocation.Invocation;
import ru.tinkoff.edu.java.bot.common.api.validation.Validation;

import java.util.Objects;

public abstract class StartApi extends Invocation <StartApi.Payload,
        StartApi.Payload.Builder, StartApi.Result, StartApi.Result.Visitor>{

    @Override
    protected Result newValidationFailed(Validation validation) {
        return Result.validationFailed(validation);
    }

    @Override
    protected Payload.Builder newPayloadBuilder() {
        return new Payload.Builder();
    }

    public record Payload(TgChatId tgChatId) implements Invocation.Payload {

        public static final String PROP_TG_CHAT_ID = "tgChatId";

        public static final String ERROR_TG_CHAT_ID = "tg_chat.register.errors.tg_chat_id";

        private Payload(Builder builder) {
            this(builder.tgChatId);
        }

        @Override
        public Validation validate() {

            final var validation = new Validation();

            if (Objects.isNull(this.tgChatId)) {
                validation.registerFieldError(PROP_TG_CHAT_ID, ERROR_TG_CHAT_ID);
            }

            return validation;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Builder implements Invocation.Payload.Builder<Payload> {

            private TgChatId tgChatId;

            public Builder tgChatId(TgChatId value) {
                this.tgChatId = value;
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

            void onAlreadyExists(AlreadyExists result);

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

        enum AlreadyExists implements Result {
            INSTANCE;

            @Override
            public void visit(Visitor visitor) {

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

        static AlreadyExists alreadyExists() {
            return AlreadyExists.INSTANCE;
        }

        static ExecutionFailed executionFailed(final Exception exception) {
            return new ExecutionFailed(exception);
        }
    }
}
