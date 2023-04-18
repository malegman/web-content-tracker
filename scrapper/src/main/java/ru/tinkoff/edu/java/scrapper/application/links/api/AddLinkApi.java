package ru.tinkoff.edu.java.scrapper.application.links.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.common.invocation.Invocation;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.TgChatLinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

import java.util.Objects;

/**
 * API для добавления отслеживаемой ссылки в чат телеграмма
 */
public abstract class AddLinkApi extends Invocation<AddLinkApi.Payload,
        AddLinkApi.Payload.Builder, AddLinkApi.Result, AddLinkApi.Result.Visitor> {

    @Override
    protected Result newValidationFailed(Validation validation) {
        return Result.validationFailed(validation);
    }

    @Override
    protected Payload.Builder newPayloadBuilder() {
        return new Payload.Builder();
    }

    public record Payload(TgChatId tgChatId, String link) implements Invocation.Payload {

        public static final String PROP_TG_CHAT_ID = "tgChatId";
        public static final String PROP_LINK = "link";

        public static final String ERROR_TG_CHAT_ID = "links.add.errors.tg_chat_id";
        public static final String ERROR_LINK = "links.add.errors.link";

        private Payload(Builder builder) {
            this(builder.tgChatId, builder.link);
        }

        @Override
        public Validation validate() {

            final var validation = new Validation();

            if (Objects.isNull(this.tgChatId)) {
                validation.registerFieldError(PROP_TG_CHAT_ID, ERROR_TG_CHAT_ID);
            }

            if (Objects.isNull(this.link) || this.link.isBlank()) {
                validation.registerFieldError(PROP_LINK, ERROR_LINK);
            }

            return validation;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Builder implements Invocation.Payload.Builder<Payload> {

            private TgChatId tgChatId;
            private String link;

            public Builder tgChatId(TgChatId value) {
                this.tgChatId = value;
                return this;
            }

            public Builder link(String value) {
                this.link = value;
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

            void onLinkAlreadyExists(LinkAlreadyExists result);
        }

        record Success(TgChatLinkDto link) implements Result {

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

        enum LinkAlreadyExists implements Result {
            INSTANCE;

            @Override
            public void visit(Visitor visitor) {
                visitor.onLinkAlreadyExists(this);
            }
        }

        static Success success(final TgChatLinkDto link) {
            return new Success(link);
        }

        static ValidationFailed validationFailed(final Validation validation) {
            return new ValidationFailed(validation);
        }

        static LinkAlreadyExists linkAlreadyExists() {
            return LinkAlreadyExists.INSTANCE;
        }
    }
}
