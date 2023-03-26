package ru.tinkoff.edu.java.bot.common.spring.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.bot.common.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.common.validation.Validation;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Абстрактный обработчик HTTP запросов, реализует spring интерфейс {@link HandlerFunction}
 */
@Slf4j
public abstract class AbstractBotHandlerFunction implements HandlerFunction<ServerResponse> {

    public static final ServerResponse SR_NO_CONTENT = ServerResponse.noContent().build();

    private ServerResponse serverApiErrorResponse(ApiErrorResponse apiErrorResponse) {
        return ServerResponse
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiErrorResponse);
    }

    /**
     * Обрабатывает http запрос
     *
     * @param request обрабатываемый запрос
     *
     * @return http ответ
     *
     * @throws Exception если произошла ошибка обработки запроса
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

            return this.serverApiErrorResponse(new ApiErrorResponse(
                    exceptionMessage,
                    exceptionId.toString(),
                    exception.getClass().getName(),
                    exceptionMessage,
                    Arrays.stream(exception.getStackTrace())
                            .map(StackTraceElement::toString)
                            .toList()));
        }
    }

    /**
     * Преобразует объект валидации в http ответ со статусом 400
     *
     * @param validation объект валидации
     *
     * @return http ответ со статусом 400
     */
    protected ServerResponse badRequestFromValidation(final Validation validation) {
        return this.serverApiErrorResponse(new ApiErrorResponse(
                validation.getFieldErrors()
                        .entrySet()
                        .stream()
                        .map(entry -> String.format("Field: %s. Errors: %s.",
                                entry.getKey(),
                                entry.getValue()))
                        .collect(Collectors.joining("],[", "[", "]"))));
    }
}
