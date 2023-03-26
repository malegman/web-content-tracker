package ru.tinkoff.edu.java.bot.common.dto.response;

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

    public ApiErrorResponse(String exceptionMessage) {
        this(null, null, null, exceptionMessage, null);
    }
}
