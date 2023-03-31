package ru.tinkoff.edu.java.scrapper.common.errors;

import java.util.List;

/**
 * Ответ API для возврата ошибки выполнения
 *
 * @param description      описание ошибки
 * @param code             код ошибки (идентификатор)
 * @param exceptionName    наименование ошибки
 * @param exceptionMessage сообщение ошибки
 * @param stacktrace       stack trace ошибки
 */
public record ApiErrorResponse(String description, String code, String exceptionName,
                               String exceptionMessage, List<String> stacktrace) {

    public static Builder builder() {
        return new Builder();
    }

    private ApiErrorResponse(Builder builder) {
        this(builder.errorType.getDescription(), builder.errorType.getCode(), builder.exceptionName,
                builder.exceptionMessage, builder.stacktrace);
    }

    public static final class Builder {

        private ErrorType errorType;
        private String exceptionName;
        private String exceptionMessage;
        private List<String> stacktrace;

        public Builder errorType(ErrorType value) {
            this.errorType = value;
            return this;
        }

        public Builder exceptionName(String value) {
            this.exceptionName = value;
            return this;
        }

        public Builder exceptionMessage(String value) {
            this.exceptionMessage = value;
            return this;
        }

        public Builder stacktrace(List<String> value) {
            this.stacktrace = value;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(this);
        }
    }
}
