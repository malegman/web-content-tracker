package ru.tinkoff.edu.java.bot.common.spring.web;

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
import ru.tinkoff.edu.java.bot.common.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.common.validation.Validation;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private ServerResponse serverApiErrorResponse(ApiErrorResponse apiErrorResponse) {
        return ServerResponse
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiErrorResponse);
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
                    return this.handleInternal(request, status);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
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

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.transactionOperations = new TransactionTemplate(beanFactory
                .getBean(PlatformTransactionManager.class), this.transactionDefinition);
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
