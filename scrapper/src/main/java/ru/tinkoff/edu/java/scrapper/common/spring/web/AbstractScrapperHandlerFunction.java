package ru.tinkoff.edu.java.scrapper.common.spring.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.http.MediaType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.common.errors.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.common.errors.ErrorType;
import ru.tinkoff.edu.java.scrapper.common.errors.ValidationFailedException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Абстрактный обработчик HTTP запросов, реализует spring интерфейс {@link HandlerFunction}
 */
@Slf4j
public abstract class AbstractScrapperHandlerFunction implements HandlerFunction<ServerResponse>, BeanFactoryAware {

    public static final ServerResponse SR_NO_CONTENT = ServerResponse.noContent().build();

    private final TransactionDefinition transactionDefinition;
    private TransactionOperations transactionOperations;

    public AbstractScrapperHandlerFunction(final TransactionDefinition transactionDefinition) {
        this.transactionDefinition = Objects.requireNonNull(transactionDefinition);
    }

    /**
     * Обрабатывает http запрос в рамках транзакции
     *
     * @param request обрабатываемый запрос
     * @param status  статус транзакции
     *
     * @return http ответ
     *
     * @throws Exception если произошла ошибка обработки запроса
     */
    protected abstract ServerResponse handleInternal(ServerRequest request, TransactionStatus status) throws Exception;

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
            return this.transactionOperations.execute(status -> {
                try {
                    return handleInternal(request, status);
                } catch (Exception exception) {
                    return this.handleException(exception);
                }
            });
        } catch (Exception exception) {
            return this.handleException(exception);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.transactionOperations = new TransactionTemplate(beanFactory
                .getBean(PlatformTransactionManager.class), this.transactionDefinition);
    }

    private ServerResponse handleException(final Exception exception) {

        final var exceptionId = UUID.randomUUID();
        final var exceptionMessage = exception.getMessage();
        log.error("Exception id: {}. Exception message: {}", exceptionId, exceptionMessage);

        final var apiErrorResponseBuilder = ApiErrorResponse.builder();

        if (exception instanceof ValidationFailedException) {
            apiErrorResponseBuilder.errorType(ErrorType.VALIDATION_FAILED);
        } else if (exception instanceof IllegalArgumentException) {
            apiErrorResponseBuilder.errorType(ErrorType.VALIDATION_FAILED);
        } else {
            apiErrorResponseBuilder.errorType(ErrorType.EXECUTION_FAILED);
        }

        return ServerResponse
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiErrorResponseBuilder
                        .exceptionMessage(exceptionMessage)
                        .exceptionName(exception.getClass().getName())
                        .stacktrace(Arrays.stream(exception.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList())
                        .build());
    }

    /**
     * Извлекает заголовок по его названию из http-запроса
     *
     * @param request    запрос
     * @param headerName название заголовка
     *
     * @return {@code empty}, если заголовок не найден
     */
    protected Optional<String> extractHeader(ServerRequest request, String headerName) {
        return Optional.ofNullable(request.headers().firstHeader(headerName));
    }
}
