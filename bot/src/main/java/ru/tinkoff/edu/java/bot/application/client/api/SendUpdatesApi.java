package ru.tinkoff.edu.java.bot.application.client.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.invocation.Invocation;
import ru.tinkoff.edu.java.bot.common.validation.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * API для рассылки обновления ссылки в указанные телеграмм чаты
 */
public abstract class SendUpdatesApi extends Invocation<SendUpdatesApi.Payload,
        SendUpdatesApi.Payload.Builder, SendUpdatesApi.Result, SendUpdatesApi.Result.Visitor>{

    @Override
    protected Result newValidationFailed(Validation validation) {
        return Result.validationFailed(validation);
    }

    @Override
    protected Payload.Builder newPayloadBuilder() {
        return new Payload.Builder();
    }

    public record Payload(LinkId id, String url, String description, List<TgChatId> tgChatIds)
            implements Invocation.Payload {

        public static final String PROP_ID = "id";
        public static final String PROP_URL = "url";
        public static final String PROP_DESCRIPTION = "description";
        public static final String PROP_TG_CHAT_IDS = "tgChatIds";

        public static final String ERROR_ID = "links.updates.errors.id";
        public static final String ERROR_URL = "links.updates.errors.url";
        public static final String ERROR_DESCRIPTION = "links.updates.errors.description";
        public static final String ERROR_TG_CHAT_IDS = "links.updates.errors.tg_chat_ids";

        private Payload(Builder builder) {
            this(builder.id, builder.url, builder.description, builder.tgChatIds);
        }

        @Override
        public Validation validate() {

            final var validation = new Validation();

            if (Objects.isNull(this.id)) {
                validation.registerFieldError(PROP_ID, ERROR_ID);
            }
            if (Objects.isNull(this.url) || this.url.isBlank()) {
                validation.registerFieldError(PROP_URL, ERROR_URL);
            }
            if (Objects.isNull(this.description) || this.description.isBlank()) {
                validation.registerFieldError(PROP_DESCRIPTION, ERROR_DESCRIPTION);
            }
            if (this.tgChatIds.isEmpty() || this.tgChatIds.stream().anyMatch(Objects::isNull)) {
                validation.registerFieldError(PROP_TG_CHAT_IDS, ERROR_TG_CHAT_IDS);
            }

            return validation;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Builder implements Invocation.Payload.Builder<Payload> {

            private LinkId id;
            private String url;
            private String description;
            private final List<TgChatId> tgChatIds = new ArrayList<>();

            public Builder id(LinkId value) {
                this.id = value;
                return this;
            }

            public Builder url(String value) {
                this.url = value;
                return this;
            }

            public Builder description(String value) {
                this.description = value;
                return this;
            }

            public Builder tgChatIds(List<TgChatId> values) {
                this.tgChatIds.addAll(values);
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
