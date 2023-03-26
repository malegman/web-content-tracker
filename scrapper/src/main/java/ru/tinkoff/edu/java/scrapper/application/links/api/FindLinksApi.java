package ru.tinkoff.edu.java.scrapper.application.links.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.common.invocation.Invocation;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

import java.util.List;
import java.util.Objects;

/**
 * API для получения отслеживаемых ссылок чата телеграмма
 */
public abstract class FindLinksApi extends Invocation<FindLinksApi.Payload,
        FindLinksApi.Payload.Builder, FindLinksApi.Result, FindLinksApi.Result.Visitor> {

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

        public static final String ERROR_TG_CHAT_ID = "links.find.errors.tg_chat_id";

        private Payload(Builder builder) {
            this(builder.id);
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

        record Success(List<LinkDto> links) implements Result {

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

        static Success success(final List<LinkDto> links) {
            return new Success(links);
        }

        static ValidationFailed validationFailed(final Validation validation) {
            return new ValidationFailed(validation);
        }

        static ExecutionFailed executionFailed(final Exception exception) {
            return new ExecutionFailed(exception);
        }
    }
}
