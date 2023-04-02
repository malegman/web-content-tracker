package ru.tinkoff.edu.java.bot.common.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler.Result;

import java.util.Objects;
import java.util.function.Function;

public final class WebClientBodyResponse<B> {

    private final HttpStatusCode httpStatusCode;
    private final B body;
    private Exception exception;

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

    public static <B> WebClientBodyResponse<B> fromException(Exception e) {
        return new WebClientBodyResponse<>(e);
    }

    public WebClientBodyResponse<B> mapResultOn2xxSuccessful(Function<B, Result> mapper) {
        if (Objects.nonNull(this.body) && Objects.isNull(this.result) && this.httpStatusCode.is2xxSuccessful()) {
            this.result = mapper.apply(this.body);
        }
        return this;
    }

    public WebClientBodyResponse<B> setResultOn2xxSuccessful(Result result) {
        if (Objects.isNull(this.body) && Objects.isNull(this.result) && this.httpStatusCode.is2xxSuccessful()) {
            this.result = result;
        }
        return this;
    }

    public WebClientBodyResponse<B> setResultOn4xxClientError(Result result) {
        if (Objects.isNull(this.result) && this.httpStatusCode.is4xxClientError()) {
            this.result = result;
        }
        return this;
    }

    public WebClientBodyResponse<B> setResultOnException(Result result) {
        if (Objects.isNull(this.result) && this.isException) {
            this.result = result;
        }
        return this;
    }

    public Result getResult() {
        return this.result;
    }
}
