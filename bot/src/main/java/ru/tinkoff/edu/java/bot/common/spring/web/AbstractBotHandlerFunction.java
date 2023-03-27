package ru.tinkoff.edu.java.bot.common.spring.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.bot.common.errors.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.common.errors.ErrorType;
import ru.tinkoff.edu.java.bot.common.errors.ValidationFailedException;

import java.util.Arrays;
import java.util.UUID;

/**
 * Абстрактный обработчик HTTP запросов, реализует spring интерфейс {@link HandlerFunction}
 */
@Slf4j
public abstract class AbstractBotHandlerFunction implements HandlerFunction<ServerResponse> {

    public static final ServerResponse SR_NO_CONTENT = ServerResponse.noContent().build();

    /**
     * Обрабатывает http запрос
     *
     * @param request обрабатываемый запрос
     *
     * @return http ответ
     */
    protected abstract ServerResponse handleInternal(ServerRequest request) throws Exception;

    /**
     * Обрабатывает http запрос
     *
     * @param request обрабатываемый запрос
     *
     * @return http ответ
     */
    @Override
    public ServerResponse handle(final ServerRequest request) {

        try {
            return this.handleInternal(request);

        } catch (Exception exception) {
            final var exceptionId = UUID.randomUUID();
            final var exceptionMessage = exception.getMessage();
            log.error("Exception id: {}. Exception message: {}", exceptionId, exceptionMessage);

            return this.handleException(exception);
        }
    }

    private ServerResponse handleException(final Exception exception) {

        final var apiErrorResponseBuilder = ApiErrorResponse.builder();

        if (exception instanceof ValidationFailedException) {
            apiErrorResponseBuilder.errorType(ErrorType.VALIDATION_FAILED);
        } else {
            apiErrorResponseBuilder.errorType(ErrorType.EXECUTION_FAILED);
        }

        return ServerResponse
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiErrorResponseBuilder
                        .exceptionMessage(exception.getMessage())
                        .exceptionName(exception.getClass().getName())
                        .stacktrace(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList())
                        .build());
    }
}
