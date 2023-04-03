package ru.tinkoff.edu.java.bot.common.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler.Result;

import java.util.Objects;
import java.util.function.Function;

/**
 * Обертка ответа web-клиента для дальнейшего преобразования в {@link Result}.
 * @param <B> тип тела ответа web-клиента
 */
public final class WebClientBodyResponse<B> {

    private final HttpStatusCode httpStatusCode;
    private final B body;
    private Exception exception; // Возможно пригодится

    private Result result;
    private boolean isException;

    private WebClientBodyResponse(final HttpStatusCode httpStatusCode,
                                  final B body) {
        this.httpStatusCode = Objects.requireNonNull(httpStatusCode);
        this.body = body;
    }

    private WebClientBodyResponse(final Exception exception) {
        this.httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.body = null;
        this.exception = exception;
        this.isException = true;
    }

    public static <B> WebClientBodyResponse<B> withBody(ResponseEntity<B> entity) {
        return new WebClientBodyResponse<>(entity.getStatusCode(), entity.getBody());
    }

    public static <B> WebClientBodyResponse<B> bodiless(ResponseEntity<B> entity) {
        return new WebClientBodyResponse<>(entity.getStatusCode(), null);
    }

    public static <B> WebClientBodyResponse<B> fromWebClientResponseException(WebClientResponseException e) {
        return new WebClientBodyResponse<>(e.getStatusCode(), null);
    }

    public static <B> WebClientBodyResponse<B> fromException(Exception e) {
        return new WebClientBodyResponse<>(e);
    }

    /**
     * Инициализирует {@link Result} из указанной функции, если статус ответа вида 2xx
     *
     * @param mapper функция, для создания {@link Result}
     *
     * @return данная обертка ответа
     */
    public WebClientBodyResponse<B> mapResultOn2xxSuccessful(Function<B, Result> mapper) {
        if (Objects.isNull(this.result) && !this.isException && this.httpStatusCode.is2xxSuccessful()) {
            this.result = mapper.apply(this.body);
        }
        return this;
    }

    /**
     * Инициализирует {@link Result} из указанного результата, если статус ответа вида 2xx
     *
     * @param result результат
     *
     * @return данная обертка ответа
     */
    public WebClientBodyResponse<B> setResultOn2xxSuccessful(Result result) {
        if (Objects.isNull(this.result) && !this.isException && this.httpStatusCode.is2xxSuccessful()) {
            this.result = result;
        }
        return this;
    }

    /**
     * Инициализирует {@link Result} из указанной функции, если статус ответа вида 4xx
     *
     * @param mapper функция, для создания {@link Result}
     *
     * @return данная обертка ответа
     */
    public WebClientBodyResponse<B> mapResultOn4xxClientError(Function<HttpStatusCode, Result> mapper) {
        if (Objects.isNull(this.result) && !this.isException && this.httpStatusCode.is4xxClientError()) {
            this.result = mapper.apply(this.httpStatusCode);
        }
        return this;
    }

    /**
     * Инициализирует {@link Result} из указанного результата, если статус ответа вида 4xx
     *
     * @param result результат
     *
     * @return данная обертка ответа
     */
    public WebClientBodyResponse<B> setResultOn4xxClientError(Result result) {
        if (Objects.isNull(this.result) && !this.isException && this.httpStatusCode.is4xxClientError()) {
            this.result = result;
        }
        return this;
    }

    /**
     * Инициализирует {@link Result} из указанного результата, если выполнение web-клиента завершилось ошибкой
     *
     * @param result результат
     *
     * @return данная обертка ответа
     */
    public WebClientBodyResponse<B> setResultOnException(Result result) {
        if (Objects.isNull(this.result) && this.isException) {
            this.result = result;
        }
        return this;
    }

    /**
     * Возвращает результат, ранее инициализированный методами set... и map...
     *
     * @return результат
     */
    public Result getResult() {
        return this.result;
    }
}
